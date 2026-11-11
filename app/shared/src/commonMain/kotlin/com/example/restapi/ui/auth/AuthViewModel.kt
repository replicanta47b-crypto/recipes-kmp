package com.example.restapi.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.restapi.domain.auth.LoginUseCase
import com.example.restapi.domain.auth.ValidatePasswordUseCase
import com.example.restapi.domain.auth.ValidateUsernameUseCase
import com.example.restapi.domain.common.AppResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class AuthViewModel(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val login: LoginUseCase,
) : ViewModel() {

    private val log = Logger.withTag("AuthViewModel")

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _events = Channel<AuthEvent>(Channel.BUFFERED)
    val events: Flow<AuthEvent> = _events.receiveAsFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.UsernameChanged -> _state.update {
                it.copy(
                    username = action.value,
                    usernameError = if (it.submitAttempted) validateUsername(action.value) else null,
                )
            }
            is AuthAction.PasswordChanged -> _state.update {
                it.copy(
                    password = action.value,
                    passwordError = if (it.submitAttempted) validatePassword(action.value) else null,
                )
            }
            AuthAction.TogglePasswordVisibility -> _state.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            AuthAction.SignIn -> signIn()
        }
    }

    private fun signIn() {
        val current = _state.value
        val usernameError = validateUsername(current.username)
        val passwordError = validatePassword(current.password)
        _state.update {
            it.copy(
                submitAttempted = true,
                usernameError = usernameError,
                passwordError = passwordError,
            )
        }
        if (usernameError == null && passwordError == null) {
            if (current.isLoading) return // a double tap must not send two requests
            log.i { "Sign in requested for '${current.username}'" }
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                val result = login(current.username, current.password)
                _state.update { it.copy(isLoading = false) }
                when (result) {
                    is AppResult.Success -> _events.send(AuthEvent.SignedIn)
                    is AppResult.Failure -> {
                        log.w { "Sign in failed: ${result.error}" }
                        _events.send(AuthEvent.ShowError(result.error))
                    }
                }
            }
        } else {
            log.d { "Sign in rejected: username=$usernameError, password=$passwordError" }
        }
    }
}
