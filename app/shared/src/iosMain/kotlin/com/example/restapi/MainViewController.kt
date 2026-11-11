package com.example.restapi

import androidx.compose.ui.window.ComposeUIViewController
import com.example.restapi.di.initKoin

fun MainViewController() = ComposeUIViewController(configure = { initKoin() }) { App() }
