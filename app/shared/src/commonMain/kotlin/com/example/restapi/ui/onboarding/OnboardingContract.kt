package com.example.restapi.ui.onboarding

internal data class OnboardingState(
    val currentPage: Int = 0,
    val isCompleting: Boolean = false,
)

internal sealed interface OnboardingAction {
    data class PageChanged(val page: Int) : OnboardingAction
    data object Skip : OnboardingAction
    data object Finish : OnboardingAction
}

internal sealed interface OnboardingEvent {
    data object NavigateToAuth : OnboardingEvent
}
