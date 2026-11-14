package com.example.restapi.ui.recipes

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import restapi.app.shared.generated.resources.Res
import restapi.app.shared.generated.resources.difficulty_easy
import restapi.app.shared.generated.resources.difficulty_hard
import restapi.app.shared.generated.resources.difficulty_medium
import restapi.app.shared.generated.resources.meal_appetizer
import restapi.app.shared.generated.resources.meal_beverage
import restapi.app.shared.generated.resources.meal_breakfast
import restapi.app.shared.generated.resources.meal_dessert
import restapi.app.shared.generated.resources.meal_dinner
import restapi.app.shared.generated.resources.meal_lunch
import restapi.app.shared.generated.resources.meal_side_dish
import restapi.app.shared.generated.resources.meal_snack
import restapi.app.shared.generated.resources.sort_default
import restapi.app.shared.generated.resources.sort_lightest
import restapi.app.shared.generated.resources.sort_quickest
import restapi.app.shared.generated.resources.sort_rating

private val mealTypeLabels: Map<String, StringResource> = mapOf(
    "Breakfast" to Res.string.meal_breakfast,
    "Lunch" to Res.string.meal_lunch,
    "Dinner" to Res.string.meal_dinner,
    "Snack" to Res.string.meal_snack,
    "Dessert" to Res.string.meal_dessert,
    "Appetizer" to Res.string.meal_appetizer,
    "Side Dish" to Res.string.meal_side_dish,
    "Beverage" to Res.string.meal_beverage,
)

private val difficultyLabels: Map<String, StringResource> = mapOf(
    "Easy" to Res.string.difficulty_easy,
    "Medium" to Res.string.difficulty_medium,
    "Hard" to Res.string.difficulty_hard,
)

/** The API speaks English; known values are translated, the unknown ones are shown as they are. */
@Composable
internal fun mealTypeLabel(mealType: String): String =
    mealTypeLabels[mealType]?.let { stringResource(it) } ?: mealType

@Composable
internal fun difficultyLabel(difficulty: String): String =
    difficultyLabels[difficulty]?.let { stringResource(it) } ?: difficulty

/** `4.6`: one digit after the point, without String.format (it does not exist in common code). */
internal fun formatRating(rating: Double): String {
    val tenths = kotlin.math.round(rating * 10).toInt()
    return "${tenths / 10}.${tenths % 10}"
}

@Composable
internal fun sortLabel(sort: com.example.restapi.domain.recipes.RecipeSort): String = stringResource(
    when (sort) {
        com.example.restapi.domain.recipes.RecipeSort.Default -> Res.string.sort_default
        com.example.restapi.domain.recipes.RecipeSort.Rating -> Res.string.sort_rating
        com.example.restapi.domain.recipes.RecipeSort.Quickest -> Res.string.sort_quickest
        com.example.restapi.domain.recipes.RecipeSort.Lightest -> Res.string.sort_lightest
    },
)
