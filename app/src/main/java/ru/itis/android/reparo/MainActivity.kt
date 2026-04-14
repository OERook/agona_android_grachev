package ru.itis.android.reparo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import ru.itis.android.auth.di.DaggerAuthComponent
import ru.itis.android.auth.presentation.AuthNavigation
import ru.itis.android.auth.presentation.AuthViewModel
import ru.itis.android.auth.presentation.screens.MainScreen // Импортируем наш экран-заглушку
import ru.itis.android.reparo.ui.theme.ReparoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authComponent = DaggerAuthComponent.builder()
            .appComponent((application as ReparoApp).appComponent)
            .build()

        val viewModelFactory = authComponent.viewModelFactory()
        val authViewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]

        setContent {
            ReparoTheme {
                var isAuthenticated by remember { mutableStateOf(false) }

                if (isAuthenticated) {
                    MainScreen()
                } else {
                    AuthNavigation(
                        viewModel = authViewModel,
                        onAuthSuccess = {
                            isAuthenticated = true
                        }
                    )
                }
            }
        }
    }
}