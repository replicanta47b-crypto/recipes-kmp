package com.example.restapi.data.recipes

import com.example.restapi.data.network.ApiConfig
import com.example.restapi.data.network.safeCall
import com.example.restapi.domain.common.AppError
import com.example.restapi.domain.common.AppResult
import com.example.restapi.domain.common.map
import com.example.restapi.domain.recipes.Recipe
import com.example.restapi.domain.recipes.RecipeRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class RecipeRepositoryImpl(private val client: HttpClient) : RecipeRepository {

    override suspend fun getRecipes(): AppResult<List<Recipe>> =
        client.safeCall<RecipesResponseDto>(httpError = { AppError.Server(it) }) {
            // limit=0 asks dummyjson for all the recipes (there are only 50 of them).
            get(ApiConfig.RECIPES_PATH) { parameter("limit", 0) }
        }.map { response -> response.recipes.map { it.toDomain() } }

    override suspend fun getRecipe(id: Int): AppResult<Recipe> =
        client.safeCall<RecipeDto>(httpError = { AppError.Server(it) }) {
            get("${ApiConfig.RECIPES_PATH}/$id")
        }.map { it.toDomain() }
}
