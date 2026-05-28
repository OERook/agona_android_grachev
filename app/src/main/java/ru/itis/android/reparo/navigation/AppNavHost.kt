package ru.itis.android.reparo.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import ru.itis.android.chat.presentation.ChatListViewModel
import ru.itis.android.chat.presentation.ChatRoomViewModel
import ru.itis.android.chat.presentation.ui.ChatListScreen
import ru.itis.android.chat.presentation.ui.ChatRoomScreen
import ru.itis.android.main.presentation.MainViewModel
import ru.itis.android.main.presentation.ui.HomeScreen
import ru.itis.android.main.presentation.ui.ServiceDetailScreen
import ru.itis.android.model.UserRole
import ru.itis.android.navigation.AppRoute
import ru.itis.android.order_creation.presentation.OrderCreationViewModel
import ru.itis.android.order_creation.presentation.ui.OrderCreationScreen
import ru.itis.android.order_creation.presentation.ui.OrderDetailScreen
import ru.itis.android.orders.presentation.OrdersViewModel
import ru.itis.android.orders.presentation.WriteReviewViewModel
import ru.itis.android.orders.presentation.ui.WriteReviewScreen
import ru.itis.android.orders.presentation.activeClientOrders
import ru.itis.android.orders.presentation.activeMasterOrders
import ru.itis.android.orders.presentation.completedClientOrders
import ru.itis.android.orders.presentation.completedMasterOrders
import ru.itis.android.orders.presentation.ui.ClientOrderDetailScreen
import ru.itis.android.orders.presentation.ui.ClientOrdersScreen
import ru.itis.android.orders.presentation.ui.MasterOrderDetailScreen
import ru.itis.android.orders.presentation.ui.MasterOrdersScreen
import ru.itis.android.profile.presentation.ProfileViewModel
import ru.itis.android.profile.presentation.ui.CreateServiceScreen
import ru.itis.android.profile.presentation.ui.MasterProfileScreen
import ru.itis.android.profile.presentation.ui.ProfileScreen
import ru.itis.android.search.presentation.ui.SearchScreen

@Composable
fun AppNavHost(
    backStack: NavBackStack<NavKey>,
    viewModel: MainViewModel,
    orderCreationViewModel: OrderCreationViewModel,
    profileViewModel: ProfileViewModel,
    ordersViewModel: OrdersViewModel,
    writeReviewViewModel: WriteReviewViewModel,
    chatListViewModel: ChatListViewModel,
    chatRoomViewModel: ChatRoomViewModel,
    tabModifier: Modifier,
    overlayModifier: Modifier,
    onBack: () -> Unit,
    onNavigateToServiceDetail: () -> Unit,
    onNavigateToCreateOrder: () -> Unit,
    onNavigateToOrderDetail: () -> Unit,
    onNavigateToMasterOrders: () -> Unit,
    onNavigateToMasterOrderDetail: () -> Unit,
    onNavigateToClientOrders: () -> Unit,
    onNavigateToClientOrderDetail: () -> Unit,
    onNavigateToMasterProfile: () -> Unit,
    onNavigateToMasterCompletedOrders: () -> Unit,
    onNavigateToClientCompletedOrders: () -> Unit,
    onNavigateToWriteReview: () -> Unit,
    onNavigateToChatRoom: (roomId: String, peerName: String, orderId: Long) -> Unit,
    onNavigateToCreateService: () -> Unit,
) {
    NavDisplay(backStack = backStack, onBack = onBack) { key ->
        NavEntry(key) {

            val state by viewModel.state.collectAsState()
            val orderFormState by orderCreationViewModel.state.collectAsState()
            val profileState by profileViewModel.state.collectAsState()
            val createServiceState by profileViewModel.createServiceState.collectAsState()
            val ordersState by ordersViewModel.state.collectAsState()

            when (key) {
                AppRoute.Home -> Box(tabModifier) {
                    HomeScreen(
                        state = state,
                        onRefresh = viewModel::refresh,
                        onCategorySelected = viewModel::selectCategory,
                        onServiceClick = { service ->
                            viewModel.selectService(service)
                            onNavigateToServiceDetail()
                        }
                    )
                }

                AppRoute.Search -> Box(tabModifier) {
                    SearchScreen(
                        services = state.services,
                        categories = state.categories,
                        isLoading = state.isLoading,
                        onServiceClick = { service ->
                            viewModel.selectService(service)
                            onNavigateToServiceDetail()
                        }
                    )
                }

                AppRoute.Chat -> Box(tabModifier) {
                    ChatListScreen(
                        viewModel = chatListViewModel,
                        onRoomClick = { room ->
                            onNavigateToChatRoom(room.id, room.peerName, room.orderId ?: 0L)
                        }
                    )
                }

                is AppRoute.ChatRoom -> Box(overlayModifier) {
                    ChatRoomScreen(
                        viewModel = chatRoomViewModel,
                        roomId = key.roomId,
                        peerName = key.peerName,
                        orderId = key.orderId,
                        onBack = onBack,
                        onOrderClick = { orderId ->
                            if (state.role == UserRole.MASTER) {
                                ordersViewModel.selectMasterOrderById(orderId)
                                onNavigateToMasterOrderDetail()
                            } else {
                                ordersViewModel.selectClientOrderById(orderId)
                                onNavigateToClientOrderDetail()
                            }
                        }
                    )
                }

                AppRoute.Profile -> Box(tabModifier) {
                    LaunchedEffect(Unit) { profileViewModel.refreshOwnData() }
                    ProfileScreen(
                        state = profileState,
                        activeMasterOrdersCount = ordersState.activeMasterOrders.size,
                        completedMasterOrdersCount = ordersState.completedMasterOrders.size,
                        activeClientOrdersCount = ordersState.activeClientOrders.size,
                        completedClientOrdersCount = ordersState.completedClientOrders.size,
                        onLogout = profileViewModel::logout,
                        onServiceClick = { service ->
                            viewModel.selectService(service)
                            onNavigateToServiceDetail()
                        },
                        onActiveOrdersClick = {
                            ordersViewModel.refreshMasterOrders()
                            onNavigateToMasterOrders()
                        },
                        onMyOrdersClick = {
                            ordersViewModel.refreshClientOrders()
                            onNavigateToClientOrders()
                        },
                        onCompletedMasterOrdersClick = {
                            ordersViewModel.refreshMasterOrders()
                            onNavigateToMasterCompletedOrders()
                        },
                        onCompletedClientOrdersClick = {
                            ordersViewModel.refreshClientOrders()
                            onNavigateToClientCompletedOrders()
                        },
                        onRetryLoadProfile = profileViewModel::refresh,
                        onEditService = { service ->
                            profileViewModel.startEditService(service)
                            onNavigateToCreateService()
                        },
                        onDeleteService = { service ->
                            profileViewModel.deleteService(service.id)
                        }
                    )
                }

                AppRoute.MasterOrders -> Box(overlayModifier) {
                    MasterOrdersScreen(
                        orders = ordersState.activeMasterOrders,
                        onOrderClick = { order ->
                            ordersViewModel.selectMasterOrder(order)
                            onNavigateToMasterOrderDetail()
                        },
                        onBack = onBack,
                        isLoading = ordersState.isLoading,
                        errorMessage = ordersState.errorMessage,
                        onRetry = ordersViewModel::refresh
                    )
                }

                AppRoute.MasterOrderDetail -> {
                    val order = ordersState.selectedMasterOrder
                    if (order != null) {
                        Box(overlayModifier) {
                            MasterOrderDetailScreen(
                                order = order,
                                isUpdating = ordersState.orderStatusUpdating,
                                onUpdateStatus = { status ->
                                    ordersViewModel.updateOrderStatus(order.id, status)
                                },
                                onBack = {
                                    ordersViewModel.selectMasterOrder(null)
                                    onBack()
                                },
                                statusUpdateError = ordersState.statusUpdateError,
                                onDismissStatusError = ordersViewModel::clearStatusError,
                                onOpenChat = {
                                    chatListViewModel.openRoomForOrder(order.id) { roomId, peerName, oid ->
                                        onNavigateToChatRoom(roomId, peerName, oid)
                                    }
                                }
                            )
                        }
                    } else {
                        LaunchedEffect(Unit) { onBack() }
                    }
                }

                AppRoute.ServiceDetail -> {
                    val service = state.selectedService
                    if (service != null) {
                        Box(overlayModifier) {
                            ServiceDetailScreen(
                                service = service,
                                isMaster = state.role == UserRole.MASTER,
                                onBack = onBack,
                                onLeave = { viewModel.selectService(null) },
                                onCreateOrder = {
                                    orderCreationViewModel.initWithService(service)
                                    onNavigateToCreateOrder()
                                },
                                onMasterClick = {
                                    val masterId = service.masterId
                                    if (masterId != null) {
                                        profileViewModel.loadMasterProfile(masterId)
                                        onNavigateToMasterProfile()
                                    }
                                }
                            )
                        }
                    } else {
                        LaunchedEffect(Unit) { onBack() }
                    }
                }

                AppRoute.CreateOrder -> {
                    Box(overlayModifier) {
                        OrderCreationScreen(
                            state = orderFormState,
                            onDateChange = orderCreationViewModel::updateDate,
                            onTimeChange = orderCreationViewModel::updateTime,
                            onAddressChange = orderCreationViewModel::updateAddress,
                            onDescriptionChange = orderCreationViewModel::updateDescription,
                            onSubmit = orderCreationViewModel::submit,
                            onBack = onBack,
                            onSuccess = onNavigateToOrderDetail
                        )
                    }
                }

                AppRoute.OrderDetail -> {
                    val order = orderFormState.createdOrder
                    val service = orderFormState.service
                    if (order != null) {
                        Box(overlayModifier) {
                            OrderDetailScreen(
                                order = order,
                                service = service,
                                onRefresh = orderCreationViewModel::refreshOrder,
                                onBack = {
                                    orderCreationViewModel.reset()
                                    while (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
                                }
                            )
                        }
                    } else {
                        LaunchedEffect(Unit) { onBack() }
                    }
                }

                AppRoute.ClientOrders -> Box(overlayModifier) {
                    ClientOrdersScreen(
                        orders = ordersState.activeClientOrders,
                        onOrderClick = { order ->
                            ordersViewModel.selectClientOrder(order)
                            onNavigateToClientOrderDetail()
                        },
                        onBack = onBack,
                        title = "Активные заказы",
                        emptyTitle = "Нет активных заказов",
                        emptySubtitle = "Создайте заказ, выбрав услугу",
                        isLoading = ordersState.isLoading,
                        errorMessage = ordersState.errorMessage,
                        onRetry = ordersViewModel::refresh
                    )
                }

                AppRoute.ClientCompletedOrders -> Box(overlayModifier) {
                    ClientOrdersScreen(
                        orders = ordersState.completedClientOrders,
                        onOrderClick = { order ->
                            ordersViewModel.selectClientOrder(order)
                            onNavigateToClientOrderDetail()
                        },
                        onBack = onBack,
                        title = "Завершённые заказы",
                        emptyTitle = "История пуста",
                        emptySubtitle = "Здесь будут отображаться завершённые заказы",
                        isLoading = ordersState.isLoading,
                        errorMessage = ordersState.errorMessage,
                        onRetry = ordersViewModel::refresh
                    )
                }

                AppRoute.MasterCompletedOrders -> Box(overlayModifier) {
                    MasterOrdersScreen(
                        orders = ordersState.completedMasterOrders,
                        onOrderClick = { order ->
                            ordersViewModel.selectMasterOrder(order)
                            onNavigateToMasterOrderDetail()
                        },
                        onBack = onBack,
                        title = "Завершённые заказы",
                        emptyTitle = "История пуста",
                        emptySubtitle = "Здесь будут отображаться завершённые заказы",
                        isLoading = ordersState.isLoading,
                        errorMessage = ordersState.errorMessage,
                        onRetry = ordersViewModel::refresh
                    )
                }

                AppRoute.ClientOrderDetail -> {
                    val order = ordersState.selectedClientOrder
                    if (order != null) {
                        Box(overlayModifier) {
                            ClientOrderDetailScreen(
                                order = order,
                                onRefresh = ordersViewModel::refreshSelectedClientOrder,
                                onBack = {
                                    ordersViewModel.selectClientOrder(null)
                                    onBack()
                                },
                                canLeaveReview = order.status == ru.itis.android.model.OrderStatus.COMPLETED
                                        && !ordersState.selectedOrderHasReview,
                                onLeaveReview = { onNavigateToWriteReview() },
                                onOpenChat = {
                                    chatListViewModel.openRoomForOrder(order.id) { roomId, peerName, oid ->
                                        onNavigateToChatRoom(roomId, peerName, oid)
                                    }
                                }
                            )
                        }
                    } else {
                        LaunchedEffect(Unit) { onBack() }
                    }
                }

                AppRoute.WriteReview -> {
                    val order = ordersState.selectedClientOrder
                    if (order != null) {
                        Box(overlayModifier) {
                            WriteReviewScreen(
                                viewModel = writeReviewViewModel,
                                orderId = order.id,
                                onBack = onBack,
                                onSubmitted = {
                                    ordersViewModel.markSelectedReviewed()
                                    onBack()
                                }
                            )
                        }
                    } else {
                        LaunchedEffect(Unit) { onBack() }
                    }
                }

                AppRoute.MasterProfile -> Box(overlayModifier) {
                    MasterProfileScreen(
                        profile = profileState.selectedMasterProfile,
                        isLoading = profileState.masterProfileLoading,
                        onBack = {
                            profileViewModel.clearMasterProfile()
                            onBack()
                        },
                        errorMessage = profileState.masterProfileError,
                        onRetry = profileViewModel::retryLoadMasterProfile,
                        reviews = profileState.masterReviews,
                        reviewsLoading = profileState.masterReviewsLoading
                    )
                }

                AppRoute.CreateService -> {
                    Box(overlayModifier) {
                        CreateServiceScreen(
                            categories = profileState.categories,
                            createState = createServiceState,
                            onTitleChange = profileViewModel::updateCreateTitle,
                            onDescriptionChange = profileViewModel::updateCreateDescription,
                            onPriceChange = profileViewModel::updateCreatePrice,
                            onCategorySelected = profileViewModel::updateCreateCategory,
                            onSubmit = profileViewModel::submitCreateService,
                            onDismiss = {
                                profileViewModel.resetCreateService()
                                onBack()
                            },
                            onSuccess = {
                                profileViewModel.resetCreateService()
                                viewModel.refreshCatalog()
                                onBack()
                            }
                        )
                    }
                }

                else -> {}
            }
        }
    }
}
