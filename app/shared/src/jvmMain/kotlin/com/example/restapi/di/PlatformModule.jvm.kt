package com.example.restapi.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import org.koin.dsl.module
import java.io.File

internal actual val platformModule = module {
    single {
        KSafe(
            baseDir = File(System.getProperty("user.home"), ".recipe_book"),
            config = KSafeConfig(appNamespace = "com.example.restapi"),
        )
    }
}
