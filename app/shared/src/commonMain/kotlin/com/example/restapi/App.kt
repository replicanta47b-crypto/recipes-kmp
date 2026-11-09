package com.example.restapi

import androidx.compose.runtime.Composable
import com.example.restapi.di.appModule
import com.example.restapi.ui.main.MainScreen
import com.example.restapi.ui.theme.AppTheme
import org.koin.compose.KoinApplication

@Composable
fun App() {
    // Koin is started from Compose, so no platform entry point needs to know about it.
    KoinApplication(application = { modules(appModule) }) {
        AppTheme {
            MainScreen()
        }
    }
}
