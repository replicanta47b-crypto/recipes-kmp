package com.example.restapi.ui.main

import androidx.compose.runtime.Composable
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
import org.koin.compose.viewmodel.koinViewModel

/** Root of the app: owns the Navigation 3 back stack and maps every [Route] to its screen. */
@Composable
internal fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val backStack = rememberNavBackStack(navSavedStateConfiguration, viewModel.startDestination)

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
                OnboardingRoute(onNavigateToAuth = { replaceWith(Route.Auth) })
            }
            entry<Route.Auth> {
                AuthRoute(onSignedIn = { replaceWith(Route.Home) })
            }
            entry<Route.Home> {
                HomeRoute(
                    onNavigateToOnboarding = { replaceWith(Route.Onboarding) },
                    onSignOut = { replaceWith(Route.Auth) },
                )
            }
        },
    )
}
