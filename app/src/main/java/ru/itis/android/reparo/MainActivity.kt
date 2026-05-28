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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import ru.itis.android.auth.di.DaggerAuthComponent
import ru.itis.android.auth.presentation.AuthNavigation
import ru.itis.android.auth.presentation.AuthViewModel
import ru.itis.android.main.presentation.MainViewModel
import ru.itis.android.order_creation.presentation.OrderCreationViewModel
import ru.itis.android.chat.presentation.ChatListViewModel
import ru.itis.android.chat.presentation.ChatRoomViewModel
import ru.itis.android.orders.presentation.OrdersViewModel
import ru.itis.android.orders.presentation.WriteReviewViewModel
import ru.itis.android.profile.presentation.ProfileViewModel
import ru.itis.android.reparo.navigation.AppShell
import ru.itis.android.reparo.ui.theme.ReparoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appComponent = (application as ReparoApp).appComponent

        val authComponent = DaggerAuthComponent.builder()
            .authDeps(appComponent)
            .build()

        val authViewModel = ViewModelProvider(this, authComponent.viewModelFactory())[AuthViewModel::class.java]

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
                        val factory = appComponent.viewModelFactory()
                        val mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
                        val orderCreationViewModel = ViewModelProvider(this, factory)[OrderCreationViewModel::class.java]
                        val profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
                        val ordersViewModel = ViewModelProvider(this, factory)[OrdersViewModel::class.java]
                        val writeReviewViewModel = ViewModelProvider(this, factory)[WriteReviewViewModel::class.java]
                        val chatListViewModel = ViewModelProvider(this, factory)[ChatListViewModel::class.java]
                        val chatRoomViewModel = ViewModelProvider(this, factory)[ChatRoomViewModel::class.java]
                        AppShell(
                            viewModel = mainViewModel,
                            orderCreationViewModel = orderCreationViewModel,
                            profileViewModel = profileViewModel,
                            ordersViewModel = ordersViewModel,
                            writeReviewViewModel = writeReviewViewModel,
                            chatListViewModel = chatListViewModel,
                            chatRoomViewModel = chatRoomViewModel
                        )
                    }
                    false -> {
                        AuthNavigation(viewModel = authViewModel)
                    }
                }
            }
        }
    }
}
