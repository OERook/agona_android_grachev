package ru.itis.android.reparo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .authDeps((application as ReparoApp).appComponent)
            .build()

        val viewModelFactory = authComponent.viewModelFactory()
        val authViewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]

        setContent {
            ReparoTheme {
                val isAuthenticated by authViewModel.isAuthorised.collectAsState()

                when (isAuthenticated) {
                    null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF4A90E2))
                        }
                    }
                    true -> {
                        MainScreen()
                    }
                    false -> {
                        AuthNavigation(
                            viewModel = authViewModel
                        )
                    }
                }
            }
        }
    }
}