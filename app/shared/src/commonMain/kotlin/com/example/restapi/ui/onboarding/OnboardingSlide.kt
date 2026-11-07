package com.example.restapi.ui.onboarding

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import restapi.app.shared.generated.resources.Res
import restapi.app.shared.generated.resources.ic_favorite
import restapi.app.shared.generated.resources.ic_restaurant
import restapi.app.shared.generated.resources.ic_schedule
import restapi.app.shared.generated.resources.ic_search
import restapi.app.shared.generated.resources.onboarding_badge_dishes
import restapi.app.shared.generated.resources.onboarding_badge_time
import restapi.app.shared.generated.resources.onboarding_badge_world
import restapi.app.shared.generated.resources.onboarding_cook_description
import restapi.app.shared.generated.resources.onboarding_cook_title
import restapi.app.shared.generated.resources.onboarding_favorites_description
import restapi.app.shared.generated.resources.onboarding_favorites_title
import restapi.app.shared.generated.resources.onboarding_search_description
import restapi.app.shared.generated.resources.onboarding_search_title
import restapi.app.shared.generated.resources.onboarding_welcome_description
import restapi.app.shared.generated.resources.onboarding_welcome_title

data class OnboardingSlide(
    val icon: DrawableResource,
    val title: StringResource,
    val description: StringResource,
    val badge: StringResource? = null,
)

val onboardingSlides: List<OnboardingSlide> = listOf(
    OnboardingSlide(Res.drawable.ic_restaurant, Res.string.onboarding_welcome_title, Res.string.onboarding_welcome_description, Res.string.onboarding_badge_world),
    OnboardingSlide(Res.drawable.ic_search, Res.string.onboarding_search_title, Res.string.onboarding_search_description, Res.string.onboarding_badge_dishes),
    OnboardingSlide(Res.drawable.ic_schedule, Res.string.onboarding_cook_title, Res.string.onboarding_cook_description, Res.string.onboarding_badge_time),
    OnboardingSlide(Res.drawable.ic_favorite, Res.string.onboarding_favorites_title, Res.string.onboarding_favorites_description),
)
