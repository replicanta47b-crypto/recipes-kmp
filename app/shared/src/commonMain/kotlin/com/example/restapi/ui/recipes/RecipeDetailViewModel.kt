package com.example.restapi.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restapi.domain.common.AppError
import com.example.restapi.domain.common.AppResult
import com.example.restapi.domain.recipes.GetRecipeUseCase
import com.example.restapi.domain.recipes.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal data class RecipeDetailState(
    val recipe: Recipe? = null,
    val isLoading: Boolean = true,
    val error: AppError? = null,
)

internal class RecipeDetailViewModel(
    private val getRecipe: GetRecipeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeDetailState())
    val state: StateFlow<RecipeDetailState> = _state.asStateFlow()

    private var loadedId: Int? = null

    /** Called by the screen with the id from the navigation key; a second call with the same id does nothing. */
    fun load(id: Int, force: Boolean = false) {
        if (!force && loadedId == id) return
        loadedId = id
        viewModelScope.launch {
            _state.update { RecipeDetailState(isLoading = true) }
            when (val result = getRecipe(id)) {
                is AppResult.Success -> _state.update { RecipeDetailState(recipe = result.data, isLoading = false) }
                is AppResult.Failure -> _state.update { RecipeDetailState(isLoading = false, error = result.error) }
            }
        }
    }
}
