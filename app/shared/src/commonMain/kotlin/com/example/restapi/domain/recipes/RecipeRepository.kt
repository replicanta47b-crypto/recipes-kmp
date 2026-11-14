package com.example.restapi.domain.recipes

import com.example.restapi.domain.common.AppResult

internal interface RecipeRepository {
    suspend fun getRecipes(): AppResult<List<Recipe>>
    suspend fun getRecipe(id: Int): AppResult<Recipe>
}

internal class GetRecipesUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(): AppResult<List<Recipe>> = repository.getRecipes()
}

internal class GetRecipeUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(id: Int): AppResult<Recipe> = repository.getRecipe(id)
}
