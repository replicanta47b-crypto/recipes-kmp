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

// The design defines only the light theme; the dark scheme is derived from the same hues.
val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB59D),
    onPrimary = Color(0xFF601500),
    primaryContainer = Color(0xFF852400),
    onPrimaryContainer = Color(0xFFFFDBCF),
    inversePrimary = Color(0xFFB5451B),
    secondary = Color(0xFFB6CE8E),
    onSecondary = Color(0xFF223600),
    secondaryContainer = Color(0xFF384D19),
    onSecondaryContainer = Color(0xFFD2ECAE),
    tertiary = Color(0xFFEBC248),
    onTertiary = Color(0xFF3F2E00),
    tertiaryContainer = Color(0xFF5B4300),
    onTertiaryContainer = Color(0xFFFFE17D),
    background = Color(0xFF1A110F),
    onBackground = Color(0xFFF1DFDA),
    surface = Color(0xFF1A110F),
    onSurface = Color(0xFFF1DFDA),
    surfaceVariant = Color(0xFF53433E),
    onSurfaceVariant = Color(0xFFD8C2BA),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFFA08D85),
    outlineVariant = Color(0xFF53433E),
    surfaceContainerLowest = Color(0xFF140C0A),
    surfaceContainerLow = Color(0xFF231917),
    surfaceContainer = Color(0xFF271D1B),
    surfaceContainerHigh = Color(0xFF322825),
    surfaceContainerHighest = Color(0xFF3D3230),
)
