package com.example.restapi.data.onboarding

import co.touchlab.kermit.Logger
import com.example.restapi.domain.onboarding.OnboardingRepository
import com.russhwolf.settings.Settings

/** Stores the onboarding result with multiplatform-settings (SharedPreferences, NSUserDefaults, localStorage, ...). */
internal class SettingsOnboardingRepository(
    private val settings: Settings,
) : OnboardingRepository {

    private val log = Logger.withTag("OnboardingRepository")

    override fun isOnboardingCompleted(): Boolean =
        settings.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    override fun setOnboardingCompleted(completed: Boolean) {
        log.i { "onboardingCompleted = $completed" }
        settings.putBoolean(KEY_ONBOARDING_COMPLETED, completed)
    }

    private companion object {
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}
