package ru.itis.android.reparo.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import ru.itis.android.chat.presentation.ChatListViewModel
import ru.itis.android.chat.presentation.ChatRoomViewModel
import ru.itis.android.main.presentation.MainViewModel
import ru.itis.android.model.UserRole
import ru.itis.android.navigation.AppRoute
import ru.itis.android.order_creation.presentation.OrderCreationViewModel
import ru.itis.android.orders.presentation.OrdersViewModel
import ru.itis.android.orders.presentation.WriteReviewViewModel
import ru.itis.android.profile.presentation.ProfileViewModel

private val PrimaryBlue = Color(0xFF4A90E2)
private val PrimaryBlueDark = Color(0xFF0056D2)
private val LightBlueBg = Color(0xFFE3EFFF)

private val TAB_ROUTES = setOf(
    AppRoute.Home,
    AppRoute.Search,
    AppRoute.Chat,
    AppRoute.Profile
)

@Composable
fun AppShell(
    viewModel: MainViewModel,
    orderCreationViewModel: OrderCreationViewModel,
    profileViewModel: ProfileViewModel,
    ordersViewModel: OrdersViewModel,
    writeReviewViewModel: WriteReviewViewModel,
    chatListViewModel: ChatListViewModel,
    chatRoomViewModel: ChatRoomViewModel
) {
    val state by viewModel.state.collectAsState()
    val isMaster = state.role == UserRole.MASTER

    val backStack = rememberNavBackStack(AppRoute.Home as NavKey)
    val currentKey = backStack.lastOrNull()
    val isTabRoute = currentKey in TAB_ROUTES
    val currentTab = backStack.lastOrNull { it in TAB_ROUTES } as? AppRoute ?: AppRoute.Home

    val safeBack: () -> Unit = {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        containerColor = Color.White,
        bottomBar = {
            if (isTabRoute) {
                AppBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { newTab ->
                        backStack.clear()
                        backStack.add(newTab)
                    }
                )
            }
        },
        floatingActionButton = {
            if (isMaster && currentKey == AppRoute.Home) {
                FloatingActionButton(
                    onClick = {
                        profileViewModel.startCreateService()
                        backStack.add(AppRoute.CreateService)
                    },
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Создать услугу")
                }
            }
        }
    ) { paddingValues ->
        val tabModifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)

        val overlayModifier = Modifier
            .fillMaxSize()
            .background(Color.White)

        AppNavHost(
            backStack = backStack,
            viewModel = viewModel,
            orderCreationViewModel = orderCreationViewModel,
            profileViewModel = profileViewModel,
            ordersViewModel = ordersViewModel,
            writeReviewViewModel = writeReviewViewModel,
            chatListViewModel = chatListViewModel,
            chatRoomViewModel = chatRoomViewModel,
            tabModifier = tabModifier,
            overlayModifier = overlayModifier,
            onBack = safeBack,
            onNavigateToServiceDetail = { backStack.add(AppRoute.ServiceDetail) },
            onNavigateToCreateOrder = { backStack.add(AppRoute.CreateOrder) },
            onNavigateToOrderDetail = {
                backStack.removeAt(backStack.lastIndex)
                backStack.add(AppRoute.OrderDetail)
            },
            onNavigateToMasterOrders = { backStack.add(AppRoute.MasterOrders) },
            onNavigateToMasterOrderDetail = { backStack.add(AppRoute.MasterOrderDetail) },
            onNavigateToClientOrders = { backStack.add(AppRoute.ClientOrders) },
            onNavigateToClientOrderDetail = { backStack.add(AppRoute.ClientOrderDetail) },
            onNavigateToMasterProfile = { backStack.add(AppRoute.MasterProfile) },
            onNavigateToMasterCompletedOrders = { backStack.add(AppRoute.MasterCompletedOrders) },
            onNavigateToClientCompletedOrders = { backStack.add(AppRoute.ClientCompletedOrders) },
            onNavigateToWriteReview = { backStack.add(AppRoute.WriteReview) },
            onNavigateToChatRoom = { roomId, peerName, orderId ->
                backStack.add(AppRoute.ChatRoom(roomId = roomId, peerName = peerName, orderId = orderId))
            },
            onNavigateToCreateService = { backStack.add(AppRoute.CreateService) }
        )
    }
}

@Composable
private fun AppBottomBar(
    currentTab: AppRoute,
    onTabSelected: (AppRoute) -> Unit
) {
    val navColors = NavigationBarItemDefaults.colors(
        selectedIconColor = PrimaryBlueDark,
        selectedTextColor = PrimaryBlueDark,
        indicatorColor = LightBlueBg
    )
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            selected = currentTab == AppRoute.Home,
            onClick = { onTabSelected(AppRoute.Home) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Главная") },
            label = { Text("Главная") },
            colors = navColors
        )
        NavigationBarItem(
            selected = currentTab == AppRoute.Search,
            onClick = { onTabSelected(AppRoute.Search) },
            icon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
            label = { Text("Поиск") },
            colors = navColors
        )
        NavigationBarItem(
            selected = currentTab == AppRoute.Chat,
            onClick = { onTabSelected(AppRoute.Chat) },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Чат") },
            label = { Text("Чат") },
            colors = navColors
        )
        NavigationBarItem(
            selected = currentTab == AppRoute.Profile,
            onClick = { onTabSelected(AppRoute.Profile) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Профиль") },
            label = { Text("Профиль") },
            colors = navColors
        )
    }
}
