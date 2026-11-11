package com.example.restapi.di

import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Starts Koin. It is called by every platform entry point (Android `Application`, desktop `main`, web `main`,
 * iOS `MainViewController`), because on Android KSafe needs the application Context before the first screen.
 * Safe to call twice: iOS may create the view controller more than once.
 */
fun initKoin(config: KoinAppDeclaration? = null) {
    if (GlobalContext.getOrNull() != null) return
    startKoin {
        config?.invoke(this)
        modules(platformModule, appModule)
    }
}
