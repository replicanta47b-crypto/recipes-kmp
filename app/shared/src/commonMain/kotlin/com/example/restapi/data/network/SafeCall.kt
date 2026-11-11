package com.example.restapi.data.network

import com.example.restapi.domain.common.AppError
import com.example.restapi.domain.common.AppResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

/** Default mapping of a failed HTTP status to an [AppError]. */
internal fun httpStatusToError(status: Int): AppError = when (status) {
    401, 403 -> AppError.Unauthorized
    else -> AppError.Server(status)
}

/**
 * Executes [request] and converts the response (or any exception) to an [AppResult],
 * so that the layers above never have to deal with exceptions of the HTTP client.
 */
internal suspend inline fun <reified T> HttpClient.safeCall(
    noinline httpError: (Int) -> AppError = ::httpStatusToError,
    request: suspend HttpClient.() -> HttpResponse,
): AppResult<T> = try {
    val response = request()
    if (response.status.isSuccess()) {
        AppResult.Success(response.body<T>())
    } else {
        AppResult.Failure(httpError(response.status.value))
    }
} catch (e: CancellationException) {
    throw e
} catch (e: HttpRequestTimeoutException) {
    AppResult.Failure(AppError.Timeout)
} catch (e: SerializationException) {
    AppResult.Failure(AppError.Unknown)
} catch (e: Exception) {
    // Connection refused, no internet, DNS failure, TLS errors... differ per platform, so they are grouped.
    AppResult.Failure(AppError.Network)
}
