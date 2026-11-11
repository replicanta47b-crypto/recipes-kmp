package com.example.restapi

import androidx.compose.runtime.Composable
import com.example.restapi.ui.main.MainScreen
import com.example.restapi.ui.theme.AppTheme

/** Koin must be started by the platform entry point with `initKoin()` before this is shown. */
@Composable
fun App() {
    AppTheme {
        MainScreen()
    }
}
