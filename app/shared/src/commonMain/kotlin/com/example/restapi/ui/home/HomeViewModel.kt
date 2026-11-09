package com.example.restapi.ui.home

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import com.example.restapi.domain.onboarding.ResetOnboardingUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

internal sealed interface HomeAction {
    data object ShowIntroAgain : HomeAction
}

internal sealed interface HomeEvent {
    data object NavigateToOnboarding : HomeEvent
}

internal class HomeViewModel(
    private val resetOnboarding: ResetOnboardingUseCase,
) : ViewModel() {

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events: Flow<HomeEvent> = _events.receiveAsFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.ShowIntroAgain -> {
                Logger.withTag("HomeViewModel").i { "Intro requested again" }
                resetOnboarding()
                _events.trySend(HomeEvent.NavigateToOnboarding)
            }
        }
    }
}
