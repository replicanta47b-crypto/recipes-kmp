package com.example.restapi

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.example.restapi.ui.main.MainScreen
import com.example.restapi.ui.theme.AppTheme

/** Koin must be started by the platform entry point with `initKoin()` before this is shown. */
@Composable
fun App() {
    // Coil downloads the photos of the recipes through Ktor on every platform.
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory()) }
            .build()
    }
    AppTheme {
        MainScreen()
    }
}
