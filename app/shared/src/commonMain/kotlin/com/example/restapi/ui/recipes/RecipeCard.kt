package com.example.restapi.ui.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.restapi.domain.recipes.Recipe
import com.example.restapi.ui.common.GlassSurface
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.jetbrains.compose.resources.stringResource
import restapi.app.shared.generated.resources.Res
import restapi.app.shared.generated.resources.recipe_kcal
import restapi.app.shared.generated.resources.recipe_minutes
import restapi.app.shared.generated.resources.recipe_servings

/** A photo over the whole card and a panel of glass at the bottom: the photo shines through the panel. */
@Composable
internal fun RecipeCard(recipe: Recipe, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val photoState = rememberHazeState()
    val shape = RoundedCornerShape(28.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .clip(shape)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = recipe.image,
            contentDescription = recipe.name,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .hazeSource(photoState),
            contentScale = ContentScale.Crop,
        )
        Row(
            modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Pill(recipe.cuisine)
            Pill(difficultyLabel(recipe.difficulty))
        }
        Pill(
            text = "★ ${formatRating(recipe.rating)}",
            modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
        )
        GlassSurface(
            hazeState = photoState,
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(10.dp),
            tintAlpha = 0.50f,
        ) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = recipe.ingredients.take(4).joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(Res.string.recipe_minutes, recipe.totalMinutes) + "  ·  " +
                            stringResource(Res.string.recipe_servings, recipe.servings),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = stringResource(Res.string.recipe_kcal, recipe.caloriesPerServing),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}

/** Small rounded label over a photo. */
@Composable
internal fun Pill(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
