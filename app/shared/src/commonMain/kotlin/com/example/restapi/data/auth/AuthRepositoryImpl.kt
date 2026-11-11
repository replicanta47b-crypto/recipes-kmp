package com.example.restapi.data.auth

import com.example.restapi.data.auth.model.LoginRequestDto
import com.example.restapi.data.auth.model.LoginResponseDto
import com.example.restapi.data.auth.model.UserDto
import com.example.restapi.data.auth.model.toDomain
import com.example.restapi.data.auth.model.toUser
import com.example.restapi.data.network.ApiConfig
import com.example.restapi.data.network.httpStatusToError
import com.example.restapi.data.network.safeCall
import com.example.restapi.domain.auth.AuthRepository
import com.example.restapi.domain.auth.AuthTokens
import com.example.restapi.domain.auth.TokenStorage
import com.example.restapi.domain.auth.User
import com.example.restapi.domain.common.AppError
import com.example.restapi.domain.common.AppResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CancellationException

internal class AuthRepositoryImpl(
    private val client: HttpClient,
    private val tokenStorage: TokenStorage,
) : AuthRepository {

    override suspend fun login(username: String, password: String): AppResult<User> {
        val result = client.safeCall<LoginResponseDto>(
            // dummyjson answers 400 to wrong credentials.
            httpError = { status -> if (status in 400..403) AppError.InvalidCredentials else httpStatusToError(status) },
        ) {
            post(ApiConfig.LOGIN_PATH) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequestDto(username, password, ApiConfig.ACCESS_TOKEN_LIFETIME_MINS))
            }
        }
        return when (result) {
            is AppResult.Failure -> result
            is AppResult.Success -> try {
                tokenStorage.saveTokens(AuthTokens(result.data.accessToken, result.data.refreshToken))
                // The client caches the tokens it has loaded: forget them so the new ones are used.
                forgetCachedTokens()
                AppResult.Success(result.data.toUser())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                AppResult.Failure(AppError.Unknown)
            }
        }
    }

    override suspend fun getCurrentUser(): AppResult<User> {
        val result = client.safeCall<UserDto> { get(ApiConfig.ME_PATH) }
        return when (result) {
            is AppResult.Success -> AppResult.Success(result.data.toDomain())
            is AppResult.Failure -> result
        }
    }

    override suspend fun isAuthorized(): Boolean = try {
        tokenStorage.getTokens() != null
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        false
    }

    override suspend fun logout() {
        tokenStorage.clear()
        forgetCachedTokens()
    }

    private fun forgetCachedTokens() {
        client.authProvider<BearerAuthProvider>()?.clearToken()
    }
}
