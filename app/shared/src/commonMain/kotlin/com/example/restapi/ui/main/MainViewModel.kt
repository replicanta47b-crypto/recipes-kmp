package com.example.restapi.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.restapi.domain.auth.IsAuthorizedUseCase
import com.example.restapi.domain.onboarding.IsOnboardingCompletedUseCase
import com.example.restapi.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Decides which screen the app opens first: onboarding for a new user, the login for a returning
 * user who is signed out and the home screen for a user whose tokens are stored on the device.
 */
internal class MainViewModel(
    private val isOnboardingCompleted: IsOnboardingCompletedUseCase,
    private val isAuthorized: IsAuthorizedUseCase,
) : ViewModel() {

    private val log = Logger.withTag("MainViewModel")

    // The secure storage is read asynchronously, so the answer is not ready at the first frame: null = loading.
    private val _startDestination = MutableStateFlow<Route?>(null)
    val startDestination: StateFlow<Route?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val route = resolveRoute()
            log.i { "Start destination: $route" }
            _startDestination.value = route
        }
    }

    /** Where to go when the onboarding is over (it can also be opened again from the home screen). */
    suspend fun routeAfterOnboarding(): Route = if (isAuthorized()) Route.Home else Route.Auth

    private suspend fun resolveRoute(): Route = when {
        !isOnboardingCompleted() -> Route.Onboarding
        isAuthorized() -> Route.Home
        else -> Route.Auth
    }
}
