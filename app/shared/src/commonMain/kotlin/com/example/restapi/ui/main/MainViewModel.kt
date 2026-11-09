package com.example.restapi.ui.main

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import com.example.restapi.domain.onboarding.IsOnboardingCompletedUseCase
import com.example.restapi.navigation.Route

/** Decides which screen the app opens first, depending on whether the user has passed the onboarding. */
internal class MainViewModel(
    isOnboardingCompleted: IsOnboardingCompletedUseCase,
) : ViewModel() {

    val startDestination: Route =
        if (isOnboardingCompleted()) Route.Auth else Route.Onboarding

    init {
        Logger.withTag("MainViewModel").i { "Start destination: $startDestination" }
    }
}
