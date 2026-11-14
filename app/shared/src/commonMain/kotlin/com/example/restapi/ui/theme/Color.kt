package com.example.restapi.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Palette "Warm Culinary Modern" from the Stitch design (docs/design/DESIGN.md):
// terracotta (primary), olive herb (secondary), saffron (tertiary) on a warm linen background.

val LightColors = lightColorScheme(
    primary = Color(0xFFB5451B),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDBCF),
    onPrimaryContainer = Color(0xFF3A0B00),
    inversePrimary = Color(0xFFFFB59D),
    secondary = Color(0xFF4F6B2E),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD2ECAE),
    onSecondaryContainer = Color(0xFF4F6B2E),
    tertiary = Color(0xFF8B6D00),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE17D),
    onTertiaryContainer = Color(0xFF5A4500),
    background = Color(0xFFFFF8F5),
    onBackground = Color(0xFF231916),
    surface = Color(0xFFFFF8F5),
    onSurface = Color(0xFF231916),
    surfaceVariant = Color(0xFFF2DED9),
    onSurfaceVariant = Color(0xFF53433E),
    surfaceTint = Color(0xFFB5451B),
    inverseSurface = Color(0xFF392E2A),
    inverseOnSurface = Color(0xFFFFEDE8),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF93000A),
    outline = Color(0xFF85736E),
    outlineVariant = Color(0xFFDFC0B6),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFFF1ED),
    surfaceContainer = Color(0xFFF5ECE7),
    surfaceContainerHigh = Color(0xFFF8E4DF),
    surfaceContainerHighest = Color(0xFFF2DED9),
)

// The design defines only the light theme. The dark scheme is a calm warm charcoal: low-saturation accents
// and soft (not pure white) text, so that nothing glares at night; cards are a step lighter than the background.
val DarkColors = darkColorScheme(
    primary = Color(0xFFD9917B),
    onPrimary = Color(0xFF3A1409),
    primaryContainer = Color(0xFF5E3225),
    onPrimaryContainer = Color(0xFFF1D6CC),
    inversePrimary = Color(0xFFB5451B),
    secondary = Color(0xFFA9BC8C),
    onSecondary = Color(0xFF1F2B10),
    secondaryContainer = Color(0xFF3A452E),
    onSecondaryContainer = Color(0xFFD3E0BD),
    tertiary = Color(0xFFD8BA70),
    onTertiary = Color(0xFF3A2E08),
    tertiaryContainer = Color(0xFF4D4220),
    onTertiaryContainer = Color(0xFFF0DEAA),
    background = Color(0xFF1C1B1A),
    onBackground = Color(0xFFE4DFDC),
    surface = Color(0xFF1C1B1A),
    onSurface = Color(0xFFE4DFDC),
    surfaceVariant = Color(0xFF47433F),
    onSurfaceVariant = Color(0xFFBDB5B0),
    error = Color(0xFFF0A9A0),
    onError = Color(0xFF560C08),
    errorContainer = Color(0xFF7A2A22),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF8E8782),
    outlineVariant = Color(0xFF47433F),
    surfaceContainerLowest = Color(0xFF262523),
    surfaceContainerLow = Color(0xFF211F1E),
    surfaceContainer = Color(0xFF2B2927),
    surfaceContainerHigh = Color(0xFF353230),
    surfaceContainerHighest = Color(0xFF3F3C3A),
)
