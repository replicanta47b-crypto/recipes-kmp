package com.example.restapi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import restapi.app.shared.generated.resources.Res
import restapi.app.shared.generated.resources.inter_bold
import restapi.app.shared.generated.resources.inter_medium
import restapi.app.shared.generated.resources.inter_regular
import restapi.app.shared.generated.resources.inter_semibold

/** Inter (OFL-1.1) supports both Latin and Cyrillic, so Ukrainian text uses the same font. */
@Composable
fun appFontFamily(): FontFamily {
    val regular = Font(Res.font.inter_regular, FontWeight.Normal)
    val medium = Font(Res.font.inter_medium, FontWeight.Medium)
    val semiBold = Font(Res.font.inter_semibold, FontWeight.SemiBold)
    val bold = Font(Res.font.inter_bold, FontWeight.Bold)
    return remember(regular, medium, semiBold, bold) {
        FontFamily(regular, medium, semiBold, bold)
    }
}

/** Type scale from docs/design/DESIGN.md (display-lg, headline-*, title-md, body-*, label-*). */
@Composable
fun appTypography(): Typography {
    val family = appFontFamily()
    return remember(family) {
        fun style(size: Int, line: Int, weight: FontWeight, spacing: Float = 0f) = TextStyle(
            fontFamily = family,
            fontSize = size.sp,
            lineHeight = line.sp,
            fontWeight = weight,
            letterSpacing = spacing.em,
        )
        Typography(
            displayLarge = style(40, 48, FontWeight.Bold, -0.02f),
            displayMedium = style(34, 42, FontWeight.Bold, -0.02f),
            displaySmall = style(28, 36, FontWeight.Bold, -0.015f),
            headlineLarge = style(32, 40, FontWeight.Bold, -0.015f),
            headlineMedium = style(26, 34, FontWeight.Bold, -0.01f),
            headlineSmall = style(20, 28, FontWeight.SemiBold),
            titleLarge = style(24, 32, FontWeight.SemiBold, -0.01f),
            titleMedium = style(16, 24, FontWeight.SemiBold, 0.005f),
            titleSmall = style(14, 20, FontWeight.SemiBold, 0.005f),
            bodyLarge = style(17, 26, FontWeight.Normal),
            bodyMedium = style(15, 22, FontWeight.Normal),
            bodySmall = style(13, 18, FontWeight.Normal),
            labelLarge = style(14, 20, FontWeight.SemiBold, 0.01f),
            labelMedium = style(12, 16, FontWeight.Medium, 0.02f),
            labelSmall = style(11, 14, FontWeight.Medium, 0.03f),
        )
    }
}
