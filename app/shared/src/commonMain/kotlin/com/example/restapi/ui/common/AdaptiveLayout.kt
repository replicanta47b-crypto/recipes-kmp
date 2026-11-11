package com.example.restapi.ui.common

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass

internal enum class WidthClass { Compact, Medium, Expanded }

/**
 * What the screens need to know about the window, derived from the Material [WindowSizeClass]
 * (width: compact < 600dp <= medium < 840dp <= expanded; height: compact < 480dp).
 * Screens receive it as a parameter, so they stay stateless and easy to preview with any size.
 */
internal data class AdaptiveLayout(
    val widthClass: WidthClass = WidthClass.Compact,
    val isCompactHeight: Boolean = false,
) {
    /**
     * Two panes side by side: on wide windows (tablet, desktop, browser) and on a phone in landscape,
     * where a single column would leave no room for the content.
     */
    val isSplit: Boolean
        get() = widthClass == WidthClass.Expanded || (widthClass == WidthClass.Medium && isCompactHeight)
}

@Composable
internal fun rememberAdaptiveLayout(): AdaptiveLayout {
    val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val width = when {
        sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> WidthClass.Expanded
        sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> WidthClass.Medium
        else -> WidthClass.Compact
    }
    val compactHeight = !sizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
    return AdaptiveLayout(width, compactHeight)
}
