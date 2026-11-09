package com.example.restapi.domain.onboarding

internal class ResetOnboardingUseCase(
    private val onboardingRepository: OnboardingRepository,
) {
    operator fun invoke(): Unit = onboardingRepository.setOnboardingCompleted(false)
}
