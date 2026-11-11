package com.example.restapi.ui.auth

import com.example.restapi.domain.auth.PasswordError
import com.example.restapi.domain.auth.UsernameError
import com.example.restapi.domain.common.AppError

internal data class AuthState(
    val username: String = "",
    val password: String = "",
    val usernameError: UsernameError? = null,
    val passwordError: PasswordError? = null,
    val isPasswordVisible: Boolean = false,
    /** Errors are shown only after the first attempt to sign in, then they follow the input live. */
    val submitAttempted: Boolean = false,
    /** A request is in flight: the button shows a progress indicator and the form is locked. */
    val isLoading: Boolean = false,
)

internal sealed interface AuthAction {
    data class UsernameChanged(val value: String) : AuthAction
    data class PasswordChanged(val value: String) : AuthAction
    data object TogglePasswordVisibility : AuthAction
    data object SignIn : AuthAction
}

internal sealed interface AuthEvent {
    data object SignedIn : AuthEvent

    /** One-off message for the snackbar. */
    data class ShowError(val error: AppError) : AuthEvent
}
