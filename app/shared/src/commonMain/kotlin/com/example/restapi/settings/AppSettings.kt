package com.example.restapi.settings

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings

/** User preferences stored with multiplatform-settings (SharedPreferences, NSUserDefaults, localStorage, ...). */
class AppSettings(private val settings: Settings) {

    private val log = Logger.withTag("AppSettings")

    var onboardingCompleted: Boolean
        get() = settings.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        set(value) {
            log.i { "onboardingCompleted = $value" }
            settings.putBoolean(KEY_ONBOARDING_COMPLETED, value)
        }

    private companion object {
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}
