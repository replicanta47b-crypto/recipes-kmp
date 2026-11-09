package com.example.restapi.domain.onboarding

internal class CompleteOnboardingUseCase(
    private val onboardingRepository: OnboardingRepository,
) {
    operator fun invoke(): Unit = onboardingRepository.setOnboardingCompleted(true)
}
