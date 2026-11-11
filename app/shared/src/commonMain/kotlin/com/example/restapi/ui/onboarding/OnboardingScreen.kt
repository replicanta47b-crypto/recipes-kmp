package com.example.restapi.ui.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.restapi.ui.common.AdaptiveLayout
import com.example.restapi.ui.common.ObserveEvents
import com.example.restapi.ui.common.rememberAdaptiveLayout
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import restapi.app.shared.generated.resources.Res
import restapi.app.shared.generated.resources.ic_arrow_forward
import restapi.app.shared.generated.resources.onboarding_next
import restapi.app.shared.generated.resources.onboarding_page_indicator
import restapi.app.shared.generated.resources.onboarding_skip
import restapi.app.shared.generated.resources.onboarding_start

@Composable
internal fun OnboardingRoute(
    onNavigateToAuth: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            OnboardingEvent.NavigateToAuth -> onNavigateToAuth()
        }
    }

    OnboardingScreen(state = state, onAction = viewModel::onAction, layout = rememberAdaptiveLayout())
}

@Composable
internal fun OnboardingScreen(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
    layout: AdaptiveLayout = AdaptiveLayout(),
    slides: List<OnboardingSlide> = onboardingSlides,
) {
    val pagerState = rememberPagerState { slides.size }
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == slides.lastIndex

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onAction(OnboardingAction.PageChanged(page))
        }
    }

    // On a wide window the content keeps a readable width and is centered instead of being stretched.
    val contentMaxWidth = if (layout.isSplit) 960.dp else 600.dp
    val controlsMaxWidth = 400.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = contentMaxWidth)
                .padding(horizontal = 24.dp, vertical = if (layout.isCompactHeight) 8.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(if (layout.isCompactHeight) 40.dp else 48.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!isLastPage) {
                    TextButton(
                        enabled = !state.isCompleting,
                        onClick = { onAction(OnboardingAction.Skip) },
                    ) {
                        Text(
                            stringResource(Res.string.onboarding_skip),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
            ) { page ->
                OnboardingSlideContent(slides[page], layout)
            }

            PagerIndicator(pagerState = pagerState, pageCount = slides.size)

            Spacer(Modifier.height(if (layout.isCompactHeight) 12.dp else 24.dp))

            Button(
                modifier = Modifier.fillMaxWidth().widthIn(max = controlsMaxWidth).height(56.dp),
                enabled = !state.isCompleting,
                onClick = {
                    if (isLastPage) {
                        onAction(OnboardingAction.Finish)
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                },
            ) {
                Text(
                    stringResource(if (isLastPage) Res.string.onboarding_start else Res.string.onboarding_next),
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_forward),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@Composable
private fun OnboardingSlideContent(slide: OnboardingSlide, layout: AdaptiveLayout) {
    if (layout.isSplit) {
        // Wide window or phone in landscape: the picture and the text stand side by side.
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SlideIllustration(slide, size = if (layout.isCompactHeight) 160.dp else 240.dp)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                SlideTexts(slide, textAlign = TextAlign.Start)
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            SlideIllustration(slide, size = if (layout.isCompactHeight) 120.dp else 208.dp)
            Spacer(Modifier.height(if (layout.isCompactHeight) 16.dp else 48.dp))
            SlideTexts(slide, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun SlideIllustration(slide: OnboardingSlide, size: Dp) {
    val inner = size * (184f / 208f)
    val icon = size * (88f / 208f)
    Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
        // Outer soft ring + inner circle, as in the Stitch design.
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)),
        )
        Box(
            modifier = Modifier
                .size(inner)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(slide.icon),
                contentDescription = null,
                modifier = Modifier.size(icon),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        slide.badge?.let { badge ->
            Surface(
                modifier = Modifier.align(Alignment.BottomEnd).offset(x = 20.dp, y = 6.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 3.dp,
            ) {
                Text(
                    text = stringResource(badge),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Composable
private fun SlideTexts(slide: OnboardingSlide, textAlign: TextAlign) {
    Text(
        text = stringResource(slide.title),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = textAlign,
    )
    Spacer(Modifier.height(16.dp))
    Text(
        text = stringResource(slide.description),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = textAlign,
    )
}

@Composable
private fun PagerIndicator(pagerState: PagerState, pageCount: Int) {
    val description = stringResource(
        Res.string.onboarding_page_indicator,
        pagerState.currentPage + 1,
        pageCount,
    )
    Row(
        modifier = Modifier.semantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val selected = pagerState.currentPage == index
            val width = animateDpAsState(if (selected) 28.dp else 8.dp).value
            val color = animateColorAsState(
                if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            ).value
            Box(
                modifier = Modifier
                    .size(width = width, height = 8.dp)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}
