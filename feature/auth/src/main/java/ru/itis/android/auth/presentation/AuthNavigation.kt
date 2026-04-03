package ru.itis.android.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import ru.itis.android.auth.presentation.screens.PhoneScreen
import ru.itis.android.auth.presentation.screens.MasterProfileScreen
import ru.itis.android.auth.presentation.screens.ClientProfileScreen
import ru.itis.android.auth.presentation.screens.RoleScreen
import ru.itis.android.auth.presentation.screens.StartScreen

@Composable
fun AuthNavigation(viewModel: AuthViewModel) {
    val backStack = rememberNavBackStack(AuthScreen.Start)
    val state by viewModel.state.collectAsState()

    NavDisplay(
        backStack = backStack
    ) { screen ->
        NavEntry(screen) {
            when (screen) {
                AuthScreen.Start -> {
                    StartScreen(
                        onStartClick = { backStack.add(AuthScreen.Phone) }
                    )
                }
                AuthScreen.Phone -> {
                    PhoneScreen(
                        viewModel = viewModel,
                        onNext = { backStack.add(AuthScreen.Role) },
                        onBack = { backStack.removeAt(backStack.lastIndex) }
                    )
                }
                AuthScreen.Role -> {
                    RoleScreen(
                        viewModel = viewModel,
                        onContinueClick = {
                            if (state.role == "client") {
                                backStack.add(AuthScreen.ClientProfile)
                            } else {
                                backStack.add(AuthScreen.MasterProfile)
                            }
                        },
                        onBackClick = { backStack.removeAt(backStack.lastIndex) }
                    )
                }
                AuthScreen.MasterProfile -> {
                    MasterProfileScreen(
                        viewModel = viewModel,
                        onBackClick = { backStack.removeAt(backStack.lastIndex) }
                    )
                }
                AuthScreen.ClientProfile -> {
                    ClientProfileScreen(
                        viewModel = viewModel,
                        onBackClick = { backStack.removeAt(backStack.lastIndex) }
                    )
                }
            }
        }
    }
}