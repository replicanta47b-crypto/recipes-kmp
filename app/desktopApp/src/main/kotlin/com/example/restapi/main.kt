package com.example.restapi

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.restapi.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Recipe Book",
        ) {
            App()
        }
    }
}
