package com.example.restapi.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.glass.GlassStyle
import dev.chrisbanes.haze.glass.hazeGlass

/**
 * Soft colour spots on the background. Glass needs something colourful behind it: the blur of the spots
 * (and of the photos that scroll under the panels) is what makes the surface look like liquid glass.
 */
@Composable
internal fun Modifier.glassBackdrop(): Modifier {
    val colors = MaterialTheme.colorScheme
    val strong = if (isDark(colors.background)) 0.30f else 0.38f
    return this
        .background(colors.background)
        .drawBehind {
            val radius = size.maxDimension * 0.55f
            fun spot(color: Color, x: Float, y: Float) {
                val center = Offset(size.width * x, size.height * y)
                drawCircle(
                    brush = Brush.radialGradient(listOf(color.copy(alpha = strong), Color.Transparent), center = center, radius = radius),
                    radius = radius,
                    center = center,
                )
            }
            spot(colors.primary, 0.05f, 0.05f)
            spot(colors.secondary, 0.95f, 0.40f)
            spot(colors.tertiary, 0.10f, 0.95f)
        }
}

private fun isDark(background: Color): Boolean = background.red + background.green + background.blue < 1.5f

/**
 * A panel of frosted glass: blurs and refracts what lies under it ([hazeState] is the source marked with
 * `hazeSource`), tinted with the surface colour so the text stays readable also where the platform
 * cannot blur (then only the tint and the shape remain). A thin light edge gives the "lit glass" look.
 */
@Composable
internal fun GlassSurface(
    hazeState: HazeState,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    tintAlpha: Float = 0.45f,
    content: @Composable () -> Unit,
) {
    val tintColor = MaterialTheme.colorScheme.surface.copy(alpha = tintAlpha)
    val glassShape = shape
    val style = remember(tintColor, glassShape) { GlassStyle.regular.then { tint(tintColor); shape(glassShape) } }
    val edge = remember {
        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.55f), Color.White.copy(alpha = 0.06f), Color.White.copy(alpha = 0.30f)))
    }
    Box(
        modifier = modifier
            .hazeGlass(input = HazeInput.Sources(hazeState), style = style)
            .border(1.dp, edge, shape),
    ) {
        content()
    }
}
