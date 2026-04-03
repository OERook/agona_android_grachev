package ru.itis.android.reparo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import ru.itis.android.auth.di.DaggerAuthComponent
import ru.itis.android.auth.presentation.AuthNavigation
import ru.itis.android.auth.presentation.AuthViewModel
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
            AuthNavigation(viewModel = authViewModel)
        }
    }
}
