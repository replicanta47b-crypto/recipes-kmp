package com.example.restapi.di

import eu.anifantakis.lib.ksafe.KSafe
import org.koin.dsl.module

internal actual val platformModule = module {
    single { KSafe() }
}
