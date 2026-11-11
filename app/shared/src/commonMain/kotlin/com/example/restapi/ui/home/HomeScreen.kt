package com.example.restapi.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.restapi.ui.common.ObserveEvents
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import restapi.app.shared.generated.resources.Res
import restapi.app.shared.generated.resources.home_greeting
import restapi.app.shared.generated.resources.home_placeholder
import restapi.app.shared.generated.resources.home_show_intro
import restapi.app.shared.generated.resources.home_sign_out
import restapi.app.shared.generated.resources.home_title

@Composable
internal fun HomeRoute(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToAuth: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            HomeEvent.NavigateToOnboarding -> onNavigateToOnboarding()
            HomeEvent.NavigateToAuth -> onNavigateToAuth()
        }
    }

    HomeScreen(
        state = state,
        onShowIntroAgain = { viewModel.onAction(HomeAction.ShowIntroAgain) },
        onSignOut = { viewModel.onAction(HomeAction.SignOut) },
    )
}

/** Placeholder: the list of recipes from dummyjson will be built here in a later lab. */
@Composable
internal fun HomeScreen(
    state: HomeState,
    onShowIntroAgain: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        state.userName?.let { name ->
            Text(
                text = stringResource(Res.string.home_greeting, name),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = stringResource(Res.string.home_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(Res.string.home_placeholder),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        OutlinedButton(onClick = onShowIntroAgain) {
            Text(stringResource(Res.string.home_show_intro))
        }
        TextButton(onClick = onSignOut) {
            Text(stringResource(Res.string.home_sign_out))
        }
    }
}
