package com.example.restapi.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/** Screens of the app. Keys must be serializable so the back stack survives process death. */
@Serializable
internal sealed interface Route : NavKey {
    @Serializable
    data object Onboarding : Route

    @Serializable
    data object Auth : Route

    @Serializable
    data object Home : Route

    @Serializable
    data class RecipeDetail(val id: Int) : Route
}

/** Non-Android targets have no reflection, so every route is registered explicitly. */
internal val navSavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route.Onboarding::class, Route.Onboarding.serializer())
            subclass(Route.Auth::class, Route.Auth.serializer())
            subclass(Route.Home::class, Route.Home.serializer())
            subclass(Route.RecipeDetail::class, Route.RecipeDetail.serializer())
        }
    }
}
