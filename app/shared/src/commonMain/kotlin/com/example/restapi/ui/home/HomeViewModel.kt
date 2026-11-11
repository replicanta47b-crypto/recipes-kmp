package com.example.restapi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.restapi.domain.auth.GetCurrentUserUseCase
import com.example.restapi.domain.auth.LogoutUseCase
import com.example.restapi.domain.common.AppError
import com.example.restapi.domain.common.AppResult
import com.example.restapi.domain.onboarding.ResetOnboardingUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal data class HomeState(
    /** Name of the signed-in user; null until the profile is loaded. */
    val userName: String? = null,
)

internal sealed interface HomeAction {
    data object ShowIntroAgain : HomeAction
    data object SignOut : HomeAction
}

internal sealed interface HomeEvent {
    data object NavigateToOnboarding : HomeEvent
    data object NavigateToAuth : HomeEvent
}

internal class HomeViewModel(
    private val resetOnboarding: ResetOnboardingUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val logout: LogoutUseCase,
) : ViewModel() {

    private val log = Logger.withTag("HomeViewModel")

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events: Flow<HomeEvent> = _events.receiveAsFlow()

    init {
        loadUser()
    }

    /**
     * An authorized request: the HTTP client adds the access token and, if the server says it has expired,
     * refreshes it by itself. Only when even the refresh fails the user has to sign in again.
     */
    private fun loadUser() {
        viewModelScope.launch {
            when (val result = getCurrentUser()) {
                is AppResult.Success -> _state.update { it.copy(userName = result.data.displayName) }
                is AppResult.Failure -> {
                    log.w { "Profile was not loaded: ${result.error}" }
                    if (result.error == AppError.Unauthorized) signOut()
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.ShowIntroAgain -> {
                log.i { "Intro requested again" }
                resetOnboarding()
                _events.trySend(HomeEvent.NavigateToOnboarding)
            }
            HomeAction.SignOut -> viewModelScope.launch { signOut() }
        }
    }

    private suspend fun signOut() {
        logout()
        _events.send(HomeEvent.NavigateToAuth)
    }
}
