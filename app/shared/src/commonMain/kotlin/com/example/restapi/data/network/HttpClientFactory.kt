package com.example.restapi.data.network

import co.touchlab.kermit.Logger as Kermit
import com.example.restapi.data.auth.model.RefreshRequestDto
import com.example.restapi.data.auth.model.RefreshResponseDto
import com.example.restapi.domain.auth.AuthTokens
import com.example.restapi.domain.auth.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal val AppJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

/** Sends the log of the HTTP client to Kermit. */
private object KermitKtorLogger : Logger {
    private val log = Kermit.withTag("Http")
    override fun log(message: String) = log.d { message }
}

private fun String.isAuthEndpoint() =
    endsWith(ApiConfig.LOGIN_PATH) || endsWith(ApiConfig.REFRESH_PATH)

/**
 * The only HTTP client of the app.
 *
 * - every request except login/refresh gets `Authorization: Bearer <access token>`;
 * - when the server answers 401 the [Auth] plugin refreshes the tokens once and repeats the request;
 * - the log is INFO level with the Authorization header masked: bodies (passwords!) never reach the log.
 */
internal fun createHttpClient(tokenStorage: TokenStorage): HttpClient = HttpClient {
    expectSuccess = false

    install(ContentNegotiation) { json(AppJson) }

    install(HttpTimeout) {
        requestTimeoutMillis = ApiConfig.REQUEST_TIMEOUT_MS
        connectTimeoutMillis = ApiConfig.REQUEST_TIMEOUT_MS
    }

    install(Logging) {
        logger = KermitKtorLogger
        level = LogLevel.INFO
        sanitizeHeader { header -> header.equals(HttpHeaders.Authorization, ignoreCase = true) }
    }

    install(Auth) {
        bearer {
            loadTokens {
                tokenStorage.getTokens()?.let { BearerTokens(it.accessToken, it.refreshToken) }
            }

            refreshTokens {
                val current = tokenStorage.getTokens() ?: return@refreshTokens null
                val response: HttpResponse = client.post(ApiConfig.REFRESH_PATH) {
                    // Without this mark a 401 on the refresh request would start another refresh: endless loop.
                    markAsRefreshTokenRequest()
                    contentType(ContentType.Application.Json)
                    setBody(RefreshRequestDto(current.refreshToken, ApiConfig.ACCESS_TOKEN_LIFETIME_MINS))
                }
                when {
                    response.status.isSuccess() -> {
                        val body = response.body<RefreshResponseDto>()
                        // The refresh token rotates too, so both are stored.
                        tokenStorage.saveTokens(AuthTokens(body.accessToken, body.refreshToken))
                        BearerTokens(body.accessToken, body.refreshToken)
                    }
                    response.status.value in 400..499 -> {
                        // The refresh token is rejected: the session is over.
                        Kermit.withTag("Http").w { "Refresh token rejected (${response.status.value}), session cleared" }
                        tokenStorage.clear()
                        null
                    }
                    else -> null // Server trouble: keep the session, the user may try later.
                }
            }

            sendWithoutRequest { request -> !request.url.build().encodedPath.isAuthEndpoint() }
        }
    }

    defaultRequest {
        url(ApiConfig.BASE_URL)
    }
}
