package com.example.restapi.ui.common

import com.example.restapi.domain.common.AppError
import org.jetbrains.compose.resources.StringResource
import restapi.app.shared.generated.resources.Res
import restapi.app.shared.generated.resources.error_invalid_credentials
import restapi.app.shared.generated.resources.error_network
import restapi.app.shared.generated.resources.error_server
import restapi.app.shared.generated.resources.error_session_expired
import restapi.app.shared.generated.resources.error_timeout
import restapi.app.shared.generated.resources.error_unknown

/** The domain knows only the kind of the error; the words are chosen here, in the language of the user. */
internal fun AppError.message(): StringResource = when (this) {
    AppError.InvalidCredentials -> Res.string.error_invalid_credentials
    AppError.Unauthorized -> Res.string.error_session_expired
    AppError.Network -> Res.string.error_network
    AppError.Timeout -> Res.string.error_timeout
    is AppError.Server -> Res.string.error_server
    AppError.Unknown -> Res.string.error_unknown
}
