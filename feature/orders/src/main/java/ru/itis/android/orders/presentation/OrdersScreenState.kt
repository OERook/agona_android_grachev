package ru.itis.android.orders.presentation

import ru.itis.android.model.Order
import ru.itis.android.model.OrderStatus
import ru.itis.android.model.UserRole

data class OrdersScreenState(
    val isLoading: Boolean = false,
    val role: UserRole = UserRole.CLIENT,
    val errorMessage: String? = null,
    val masterOrders: List<Order> = emptyList(),
    val selectedMasterOrder: Order? = null,
    val orderStatusUpdating: Boolean = false,
    val statusUpdateError: String? = null,
    val clientOrders: List<Order> = emptyList(),
    val selectedClientOrder: Order? = null,
    val selectedOrderHasReview: Boolean = false
)

private val ACTIVE_STATUSES =
    setOf(OrderStatus.PENDING, OrderStatus.ACCEPTED, OrderStatus.IN_PROGRESS)
private val COMPLETED_STATUSES =
    setOf(OrderStatus.COMPLETED, OrderStatus.CANCELLED, OrderStatus.DISPUTED)

val OrdersScreenState.activeMasterOrders: List<Order>
    get() = masterOrders.filter { it.status in ACTIVE_STATUSES }

val OrdersScreenState.completedMasterOrders: List<Order>
    get() = masterOrders.filter { it.status in COMPLETED_STATUSES }

val OrdersScreenState.activeClientOrders: List<Order>
    get() = clientOrders.filter { it.status in ACTIVE_STATUSES }

val OrdersScreenState.completedClientOrders: List<Order>
    get() = clientOrders.filter { it.status in COMPLETED_STATUSES }
