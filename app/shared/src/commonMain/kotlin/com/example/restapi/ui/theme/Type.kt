package com.example.restapi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun appTypography(): Typography {
    val family = appFontFamily()
    return remember(family) {
        val base = Typography()
        Typography(
            displayLarge = base.displayLarge.copy(fontFamily = family),
            displayMedium = base.displayMedium.copy(fontFamily = family),
            displaySmall = base.displaySmall.copy(fontFamily = family),
            headlineLarge = base.headlineLarge.copy(fontFamily = family, fontWeight = FontWeight.Bold),
            headlineMedium = base.headlineMedium.copy(fontFamily = family, fontWeight = FontWeight.Bold),
            headlineSmall = base.headlineSmall.copy(fontFamily = family, fontWeight = FontWeight.SemiBold),
            titleLarge = base.titleLarge.copy(fontFamily = family, fontWeight = FontWeight.SemiBold),
            titleMedium = base.titleMedium.copy(fontFamily = family, fontWeight = FontWeight.Medium),
            titleSmall = base.titleSmall.copy(fontFamily = family, fontWeight = FontWeight.Medium),
            bodyLarge = base.bodyLarge.copy(fontFamily = family),
            bodyMedium = base.bodyMedium.copy(fontFamily = family),
            bodySmall = base.bodySmall.copy(fontFamily = family),
            labelLarge = base.labelLarge.copy(fontFamily = family, fontWeight = FontWeight.SemiBold),
            labelMedium = base.labelMedium.copy(fontFamily = family, fontWeight = FontWeight.Medium),
            labelSmall = base.labelSmall.copy(fontFamily = family, fontWeight = FontWeight.Medium),
        )
    }
}
