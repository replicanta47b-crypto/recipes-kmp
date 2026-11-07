package com.example.restapi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import co.touchlab.kermit.Logger
import com.example.restapi.settings.AppSettings
import com.example.restapi.ui.home.HomeScreen
import com.example.restapi.ui.onboarding.OnboardingScreen
import com.example.restapi.ui.theme.AppTheme
import com.russhwolf.settings.Settings

private val log = Logger.withTag("App")

@Composable
fun App() {
    AppTheme {
        val appSettings = remember { AppSettings(Settings()) }
        var onboardingCompleted by remember { mutableStateOf(appSettings.onboardingCompleted) }

        if (onboardingCompleted) {
            HomeScreen(
                onShowIntroAgain = {
                    log.i { "Intro requested again" }
                    appSettings.onboardingCompleted = false
                    onboardingCompleted = false
                },
            )
        } else {
            OnboardingScreen(
                onFinished = {
                    appSettings.onboardingCompleted = true
                    onboardingCompleted = true
                },
            )
        }
    }
}
