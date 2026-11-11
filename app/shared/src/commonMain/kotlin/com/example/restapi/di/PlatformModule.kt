package com.example.restapi.di

import org.koin.core.module.Module

/** Things that cannot be created in common code. Here: KSafe, which needs a Context on Android and a folder on desktop. */
internal expect val platformModule: Module
