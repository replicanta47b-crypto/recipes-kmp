package com.example.restapi.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.restapi.domain.recipes.RecipeSort
import com.example.restapi.ui.common.GlassSurface
import com.example.restapi.ui.common.ObserveEvents
import com.example.restapi.ui.common.glassBackdrop
import com.example.restapi.ui.common.message
import com.example.restapi.ui.recipes.RecipeCard
import com.example.restapi.ui.recipes.difficultyLabel
import com.example.restapi.ui.recipes.mealTypeLabel
import com.example.restapi.ui.recipes.sortLabel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import restapi.app.shared.generated.resources.Res
import restapi.app.shared.generated.resources.filter_cuisine
import restapi.app.shared.generated.resources.filter_difficulty
import restapi.app.shared.generated.resources.filter_meal
import restapi.app.shared.generated.resources.home_clear_filters
import restapi.app.shared.generated.resources.home_empty
import restapi.app.shared.generated.resources.home_greeting
import restapi.app.shared.generated.resources.home_heading
import restapi.app.shared.generated.resources.home_results
import restapi.app.shared.generated.resources.home_retry
import restapi.app.shared.generated.resources.home_search_hint
import restapi.app.shared.generated.resources.home_show_intro
import restapi.app.shared.generated.resources.home_sign_out
import restapi.app.shared.generated.resources.sort_label

@Composable
internal fun HomeRoute(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onOpenRecipe: (Int) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            HomeEvent.NavigateToOnboarding -> onNavigateToOnboarding()
            HomeEvent.NavigateToAuth -> onNavigateToAuth()
        }
    }

    HomeScreen(state = state, onAction = viewModel::onAction, onOpenRecipe = onOpenRecipe)
}

/**
 * Recipes under a floating glass header. The grid scrolls below the header, so the photos blur through it.
 * The grid adapts: one column on a phone, more on a wide window.
 */
@Composable
internal fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onOpenRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hazeState = rememberHazeState()
    val density = LocalDensity.current
    var headerHeight by remember { mutableStateOf(0.dp) }
    val recipes = state.visibleRecipes

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding(),
    ) {
        // Everything inside is the "source" that the glass header blurs.
        Box(Modifier.fillMaxSize().hazeSource(hazeState).glassBackdrop()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 320.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = headerHeight + 12.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                when {
                    state.isLoading -> item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(Modifier.fillMaxWidth().padding(vertical = 48.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    state.error != null -> item(span = { GridItemSpan(maxLineSpan) }) {
                        Column(
                            Modifier.fillMaxWidth().padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            Text(
                                text = stringResource(state.error.message()),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                            )
                            Button(onClick = { onAction(HomeAction.Retry) }) {
                                Text(stringResource(Res.string.home_retry))
                            }
                        }
                    }
                    recipes.isEmpty() -> item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = stringResource(Res.string.home_empty),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                    else -> items(recipes, key = { it.id }) { recipe ->
                        RecipeCard(recipe = recipe, onClick = { onOpenRecipe(recipe.id) })
                    }
                }
            }
        }

        HomeHeader(
            state = state,
            shownCount = recipes.size,
            hazeState = hazeState,
            onAction = onAction,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .onSizeChanged { size -> headerHeight = with(density) { size.height.toDp() } + 8.dp },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeHeader(
    state: HomeState,
    shownCount: Int,
    hazeState: HazeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val filter = state.filter

    GlassSurface(
        hazeState = hazeState,
        shape = RoundedCornerShape(28.dp),
        modifier = modifier.fillMaxWidth(),
        tintAlpha = 0.50f,
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.home_heading),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    state.userName?.let { name ->
                        Text(
                            text = stringResource(Res.string.home_greeting, name),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                AccountMenu(onShowIntro = { onAction(HomeAction.ShowIntroAgain) }, onSignOut = { onAction(HomeAction.SignOut) })
            }

            OutlinedTextField(
                value = filter.query,
                onValueChange = { onAction(HomeAction.QueryChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(Res.string.home_search_hint)) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                ),
            )

            // Wraps to the next line on a narrow window, so every control is always reachable.
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MultiSelectDropdown(
                    title = stringResource(Res.string.filter_meal),
                    options = state.mealTypes,
                    selected = filter.mealTypes,
                    label = { mealTypeLabel(it) },
                    onToggle = { onAction(HomeAction.ToggleMealType(it)) },
                )
                MultiSelectDropdown(
                    title = stringResource(Res.string.filter_cuisine),
                    options = state.cuisines,
                    selected = filter.cuisines,
                    label = { it },
                    onToggle = { onAction(HomeAction.ToggleCuisine(it)) },
                )
                MultiSelectDropdown(
                    title = stringResource(Res.string.filter_difficulty),
                    options = state.difficulties,
                    selected = filter.difficulties,
                    label = { difficultyLabel(it) },
                    onToggle = { onAction(HomeAction.ToggleDifficulty(it)) },
                )
                SortDropdown(selected = filter.sort, onSelect = { onAction(HomeAction.SortChanged(it)) })
            }

            if (!state.isLoading && state.error == null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.home_results, shownCount, state.recipes.size),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (filter.isActive) {
                        TextButton(onClick = { onAction(HomeAction.ClearFilters) }) {
                            Text(stringResource(Res.string.home_clear_filters))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountMenu(onShowIntro: () -> Unit, onSignOut: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }) { Text("⋮", style = MaterialTheme.typography.titleLarge) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.home_show_intro)) },
                onClick = { expanded = false; onShowIntro() },
            )
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.home_sign_out)) },
                onClick = { expanded = false; onSignOut() },
            )
        }
    }
}

/** A drop-down where several options can be ticked; the menu stays open until the user taps outside. */
@Composable
private fun MultiSelectDropdown(
    title: String,
    options: List<String>,
    selected: Set<String>,
    label: @Composable (String) -> String,
    onToggle: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        DropdownTrigger(
            text = if (selected.isEmpty()) title else "$title · ${selected.size}",
            highlighted = selected.isNotEmpty(),
            onClick = { expanded = true },
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = option in selected, onCheckedChange = null)
                            Spacer(Modifier.width(12.dp))
                            Text(label(option))
                        }
                    },
                    onClick = { onToggle(option) },
                )
            }
        }
    }
}

@Composable
private fun SortDropdown(selected: RecipeSort, onSelect: (RecipeSort) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        DropdownTrigger(
            text = stringResource(Res.string.sort_label) + ": " + sortLabel(selected),
            highlighted = selected != RecipeSort.Default,
            onClick = { expanded = true },
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            RecipeSort.entries.forEach { sort ->
                DropdownMenuItem(
                    text = { Text((if (sort == selected) "✓  " else "     ") + sortLabel(sort)) },
                    onClick = { expanded = false; onSelect(sort) },
                )
            }
        }
    }
}

@Composable
private fun DropdownTrigger(text: String, highlighted: Boolean, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (highlighted) colors.primaryContainer.copy(alpha = 0.75f) else colors.surface.copy(alpha = 0.35f),
        border = BorderStroke(1.dp, if (highlighted) colors.primary.copy(alpha = 0.6f) else colors.outline.copy(alpha = 0.35f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text, style = MaterialTheme.typography.labelLarge, color = colors.onSurface)
            Text("  ▾", style = MaterialTheme.typography.labelLarge, color = colors.onSurfaceVariant)
        }
    }
}
