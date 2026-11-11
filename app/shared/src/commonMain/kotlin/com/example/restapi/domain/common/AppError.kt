package com.example.restapi.domain.common

/** Everything that can go wrong, in terms the UI can translate to a human message. */
internal sealed interface AppError {
    /** Wrong username or password. */
    data object InvalidCredentials : AppError

    /** The session is gone: the access token is rejected and it could not be refreshed. */
    data object Unauthorized : AppError

    /** No internet, DNS failure, connection refused... */
    data object Network : AppError

    data object Timeout : AppError

    data class Server(val code: Int) : AppError

    /** Bad response body, unexpected exception, storage failure. */
    data object Unknown : AppError
}
