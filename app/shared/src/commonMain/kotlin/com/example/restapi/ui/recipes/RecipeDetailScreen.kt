package com.example.restapi.ui.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.restapi.domain.recipes.Recipe
import com.example.restapi.ui.common.GlassSurface
import com.example.restapi.ui.common.glassBackdrop
import com.example.restapi.ui.common.message
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import restapi.app.shared.generated.resources.Res
import restapi.app.shared.generated.resources.home_retry
import restapi.app.shared.generated.resources.recipe_back
import restapi.app.shared.generated.resources.recipe_ingredients
import restapi.app.shared.generated.resources.recipe_instructions
import restapi.app.shared.generated.resources.recipe_kcal
import restapi.app.shared.generated.resources.recipe_minutes
import restapi.app.shared.generated.resources.recipe_servings

@Composable
internal fun RecipeDetailRoute(
    id: Int,
    onBack: () -> Unit,
    viewModel: RecipeDetailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(id) { viewModel.load(id) }

    // The colourful background is the "source" under the glass panels of the page.
    val pageState = rememberHazeState()
    val recipe = state.recipe

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding(),
    ) {
        Box(Modifier.fillMaxSize().hazeSource(pageState).glassBackdrop())

        when {
            recipe != null -> RecipeDetailContent(recipe, pageState, onBack)
            state.isLoading -> {
                BackButton(onBack, Modifier.align(Alignment.TopStart).padding(12.dp))
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            }
            else -> {
                BackButton(onBack, Modifier.align(Alignment.TopStart).padding(12.dp))
                Column(
                    Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                ) {
                    state.error?.let {
                        Text(
                            text = stringResource(it.message()),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Button(onClick = { viewModel.load(id, force = true) }) {
                        Text(stringResource(Res.string.home_retry))
                    }
                }
            }
        }
    }
}

@Composable
private fun BackButton(onBack: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(onClick = onBack, modifier = modifier) {
        Text("← " + stringResource(Res.string.recipe_back))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RecipeDetailContent(recipe: Recipe, pageState: HazeState, onBack: () -> Unit) {
    val heroState = rememberHazeState()

    // On a wide window the content keeps a readable width and is centered.
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .widthIn(max = 720.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(Modifier.fillMaxWidth().height(280.dp).clip(RoundedCornerShape(28.dp))) {
                AsyncImage(
                    model = recipe.image,
                    contentDescription = recipe.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .hazeSource(heroState),
                    contentScale = ContentScale.Crop,
                )
                GlassSurface(
                    hazeState = heroState,
                    shape = CircleShape,
                    modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
                    tintAlpha = 0.40f,
                ) {
                    BackButton(onBack)
                }
            }

            Text(
                text = recipe.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            GlassSurface(pageState, RoundedCornerShape(24.dp), Modifier.fillMaxWidth(), tintAlpha = 0.45f) {
                FlowRow(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Pill(recipe.cuisine)
                    Pill(difficultyLabel(recipe.difficulty))
                    Pill("★ ${formatRating(recipe.rating)} (${recipe.reviewCount})")
                    Pill(stringResource(Res.string.recipe_minutes, recipe.totalMinutes))
                    Pill(stringResource(Res.string.recipe_servings, recipe.servings))
                    Pill(stringResource(Res.string.recipe_kcal, recipe.caloriesPerServing))
                }
            }

            GlassSurface(pageState, RoundedCornerShape(24.dp), Modifier.fillMaxWidth(), tintAlpha = 0.45f) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(Res.string.recipe_ingredients),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    recipe.ingredients.forEach { ingredient ->
                        Text(
                            text = "•  $ingredient",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }

            GlassSurface(pageState, RoundedCornerShape(24.dp), Modifier.fillMaxWidth(), tintAlpha = 0.45f) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(Res.string.recipe_instructions),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    recipe.instructions.forEachIndexed { index, step ->
                        Text(
                            text = "${index + 1}.  $step",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
            Box(Modifier.height(24.dp))
        }
    }
}
