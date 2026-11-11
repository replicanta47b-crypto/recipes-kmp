package com.example.restapi.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.restapi.navigation.Route
import com.example.restapi.navigation.navSavedStateConfiguration
import com.example.restapi.ui.auth.AuthRoute
import com.example.restapi.ui.home.HomeRoute
import com.example.restapi.ui.onboarding.OnboardingRoute
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/** Root of the app: waits for the start destination, then shows the navigation. */
@Composable
internal fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
    val start = startDestination
    if (start == null) {
        // Blank frame while the stored tokens are being read.
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
    } else {
        AppNavigation(startDestination = start, viewModel = viewModel)
    }
}

/** Owns the Navigation 3 back stack and maps every [Route] to its screen. */
@Composable
private fun AppNavigation(startDestination: Route, viewModel: MainViewModel) {
    val backStack = rememberNavBackStack(navSavedStateConfiguration, startDestination)
    val scope = rememberCoroutineScope()

    // The user must not return to the previous screen of the flow, so the whole stack is replaced.
    fun replaceWith(route: Route) {
        backStack.clear()
        backStack.add(route)
    }

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.Onboarding> {
                OnboardingRoute(
                    onNavigateToAuth = { scope.launch { replaceWith(viewModel.routeAfterOnboarding()) } },
                )
            }
            entry<Route.Auth> {
                AuthRoute(onSignedIn = { replaceWith(Route.Home) })
            }
            entry<Route.Home> {
                HomeRoute(
                    onNavigateToOnboarding = { replaceWith(Route.Onboarding) },
                    onNavigateToAuth = { replaceWith(Route.Auth) },
                )
            }
        },
    )
}
