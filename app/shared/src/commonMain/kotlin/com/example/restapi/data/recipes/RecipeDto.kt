package com.example.restapi.data.recipes

import com.example.restapi.domain.recipes.Recipe
import kotlinx.serialization.Serializable

@Serializable
internal data class RecipesResponseDto(val recipes: List<RecipeDto> = emptyList())

/** One recipe of https://dummyjson.com/recipes. Unknown fields (tags, userId...) are ignored. */
@Serializable
internal data class RecipeDto(
    val id: Int,
    val name: String,
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val prepTimeMinutes: Int = 0,
    val cookTimeMinutes: Int = 0,
    val servings: Int = 0,
    val difficulty: String = "",
    val cuisine: String = "",
    val caloriesPerServing: Int = 0,
    val mealType: List<String> = emptyList(),
    val image: String = "",
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
)

internal fun RecipeDto.toDomain() = Recipe(
    id = id,
    name = name,
    ingredients = ingredients,
    instructions = instructions,
    prepTimeMinutes = prepTimeMinutes,
    cookTimeMinutes = cookTimeMinutes,
    servings = servings,
    difficulty = difficulty,
    cuisine = cuisine,
    caloriesPerServing = caloriesPerServing,
    mealTypes = mealType,
    image = image,
    rating = rating,
    reviewCount = reviewCount,
)
