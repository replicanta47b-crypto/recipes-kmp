package com.example.restapi.ui.onboarding

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import com.example.restapi.domain.onboarding.CompleteOnboardingUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

internal class OnboardingViewModel(
    private val completeOnboarding: CompleteOnboardingUseCase,
) : ViewModel() {

    private val log = Logger.withTag("OnboardingViewModel")

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _events = Channel<OnboardingEvent>(Channel.BUFFERED)
    val events: Flow<OnboardingEvent> = _events.receiveAsFlow()

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.PageChanged -> {
                log.d { "Slide ${action.page + 1} shown" }
                _state.update { it.copy(currentPage = action.page) }
            }
            OnboardingAction.Skip -> complete("skipped")
            OnboardingAction.Finish -> complete("finished")
        }
    }

    private fun complete(reason: String) {
        if (_state.value.isCompleting) return
        _state.update { it.copy(isCompleting = true) }
        completeOnboarding()
        log.i { "Onboarding $reason on slide ${_state.value.currentPage + 1}" }
        _events.trySend(OnboardingEvent.NavigateToAuth)
    }
}
