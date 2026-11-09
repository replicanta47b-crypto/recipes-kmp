package com.example.restapi.domain.onboarding

internal class IsOnboardingCompletedUseCase(
    private val onboardingRepository: OnboardingRepository,
) {
    operator fun invoke(): Boolean = onboardingRepository.isOnboardingCompleted()
}
