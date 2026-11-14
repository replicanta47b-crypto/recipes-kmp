package com.example.restapi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.restapi.domain.auth.GetCurrentUserUseCase
import com.example.restapi.domain.auth.LogoutUseCase
import com.example.restapi.domain.common.AppError
import com.example.restapi.domain.common.AppResult
import com.example.restapi.domain.onboarding.ResetOnboardingUseCase
import com.example.restapi.domain.recipes.GetRecipesUseCase
import com.example.restapi.domain.recipes.Recipe
import com.example.restapi.domain.recipes.RecipeFilter
import com.example.restapi.domain.recipes.RecipeSort
import com.example.restapi.domain.recipes.applyFilter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal data class HomeState(
    /** Name of the signed-in user; null until the profile is loaded. */
    val userName: String? = null,
    val recipes: List<Recipe> = emptyList(),
    val filter: RecipeFilter = RecipeFilter(),
    val isLoading: Boolean = true,
    val error: AppError? = null,
) {
    /** Options of the drop-downs: whatever exists in the loaded recipes. */
    val mealTypes: List<String>
        get() {
            val order = listOf("Breakfast", "Lunch", "Dinner", "Snack", "Appetizer", "Side Dish", "Dessert", "Beverage")
            val found = recipes.flatMap { it.mealTypes }.distinct()
            return found.sortedBy { order.indexOf(it).let { index -> if (index < 0) order.size else index } }
        }

    val cuisines: List<String> get() = recipes.map { it.cuisine }.distinct().sorted()

    val difficulties: List<String>
        get() {
            val order = listOf("Easy", "Medium", "Hard")
            return recipes.map { it.difficulty }.distinct().sortedBy { order.indexOf(it).let { index -> if (index < 0) order.size else index } }
        }

    val visibleRecipes: List<Recipe> get() = recipes.applyFilter(filter)
}

internal sealed interface HomeAction {
    data class QueryChanged(val value: String) : HomeAction
    data class ToggleMealType(val value: String) : HomeAction
    data class ToggleCuisine(val value: String) : HomeAction
    data class ToggleDifficulty(val value: String) : HomeAction
    data class SortChanged(val value: RecipeSort) : HomeAction
    data object ClearFilters : HomeAction
    data object Retry : HomeAction
    data object ShowIntroAgain : HomeAction
    data object SignOut : HomeAction
}

internal sealed interface HomeEvent {
    data object NavigateToOnboarding : HomeEvent
    data object NavigateToAuth : HomeEvent
}

internal class HomeViewModel(
    private val resetOnboarding: ResetOnboardingUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getRecipes: GetRecipesUseCase,
    private val logout: LogoutUseCase,
) : ViewModel() {

    private val log = Logger.withTag("HomeViewModel")

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events: Flow<HomeEvent> = _events.receiveAsFlow()

    init {
        loadUser()
        loadRecipes()
    }

    /**
     * An authorized request: the HTTP client adds the access token and, if the server says it has expired,
     * refreshes it by itself. Only when even the refresh fails the user has to sign in again.
     */
    private fun loadUser() {
        viewModelScope.launch {
            when (val result = getCurrentUser()) {
                is AppResult.Success -> _state.update { it.copy(userName = result.data.displayName) }
                is AppResult.Failure -> {
                    log.w { "Profile was not loaded: ${result.error}" }
                    if (result.error == AppError.Unauthorized) signOut()
                }
            }
        }
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = getRecipes()) {
                is AppResult.Success -> _state.update { it.copy(recipes = result.data, isLoading = false) }
                is AppResult.Failure -> {
                    log.w { "Recipes were not loaded: ${result.error}" }
                    _state.update { it.copy(isLoading = false, error = result.error) }
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.QueryChanged -> updateFilter { it.copy(query = action.value) }
            is HomeAction.ToggleMealType -> updateFilter { it.copy(mealTypes = it.mealTypes.toggled(action.value)) }
            is HomeAction.ToggleCuisine -> updateFilter { it.copy(cuisines = it.cuisines.toggled(action.value)) }
            is HomeAction.ToggleDifficulty -> updateFilter { it.copy(difficulties = it.difficulties.toggled(action.value)) }
            is HomeAction.SortChanged -> updateFilter { it.copy(sort = action.value) }
            HomeAction.ClearFilters -> updateFilter { it.copy(query = "", mealTypes = emptySet(), cuisines = emptySet(), difficulties = emptySet()) }
            HomeAction.Retry -> loadRecipes()
            HomeAction.ShowIntroAgain -> {
                log.i { "Intro requested again" }
                resetOnboarding()
                _events.trySend(HomeEvent.NavigateToOnboarding)
            }
            HomeAction.SignOut -> viewModelScope.launch { signOut() }
        }
    }

    private fun updateFilter(transform: (RecipeFilter) -> RecipeFilter) =
        _state.update { it.copy(filter = transform(it.filter)) }

    private fun Set<String>.toggled(value: String): Set<String> = if (value in this) this - value else this + value

    private suspend fun signOut() {
        logout()
        _events.send(HomeEvent.NavigateToAuth)
    }
}
