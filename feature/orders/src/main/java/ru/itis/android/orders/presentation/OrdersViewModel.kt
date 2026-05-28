package ru.itis.android.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.android.data.session.CurrentUserHolder
import ru.itis.android.model.Order
import ru.itis.android.model.OrderStatus
import ru.itis.android.model.UserRole
import ru.itis.android.repository.OrdersRepository
import ru.itis.android.repository.ReviewsRepository
import javax.inject.Inject

class OrdersViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val reviewsRepository: ReviewsRepository,
    private val currentUserHolder: CurrentUserHolder
) : ViewModel() {

    private val _state = MutableStateFlow(OrdersScreenState())
    val state: StateFlow<OrdersScreenState> = _state.asStateFlow()

    init {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            currentUserHolder.user.collect { user ->
                if (user == null) {
                    _state.value = OrdersScreenState()
                    return@collect
                }
                val role = UserRole.fromRaw(user.role)
                _state.update { it.copy(role = role) }
                loadOrders(role)
            }
        }
    }

    private fun loadOrders(role: UserRole) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            ordersRepository.getMyOrders().onSuccess { orders ->
                _state.update {
                    if (role == UserRole.MASTER) it.copy(masterOrders = orders, isLoading = false)
                    else it.copy(clientOrders = orders, isLoading = false)
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Не удалось загрузить заказы"
                    )
                }
            }
        }
    }

    fun refresh() {
        val user = currentUserHolder.user.value ?: return
        loadOrders(UserRole.fromRaw(user.role))
    }

    fun selectMasterOrderById(orderId: Long) {
        val order = _state.value.masterOrders.firstOrNull { it.id == orderId }
        if (order != null) selectMasterOrder(order)
    }

    fun selectClientOrderById(orderId: Long) {
        val order = _state.value.clientOrders.firstOrNull { it.id == orderId }
        if (order != null) selectClientOrder(order)
    }

    fun selectMasterOrder(order: Order?) {
        _state.update { it.copy(selectedMasterOrder = order) }
    }

    fun updateOrderStatus(orderId: Long, status: OrderStatus) {
        _state.update { it.copy(orderStatusUpdating = true, statusUpdateError = null) }
        viewModelScope.launch {
            ordersRepository.updateOrderStatus(orderId, status)
                .onSuccess { updated ->
                    _state.update { s ->
                        s.copy(
                            orderStatusUpdating = false,
                            selectedMasterOrder = updated,
                            masterOrders = s.masterOrders.map { if (it.id == updated.id) updated else it }
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            orderStatusUpdating = false,
                            statusUpdateError = error.message ?: "Не удалось изменить статус"
                        )
                    }
                }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun clearStatusError() {
        _state.update { it.copy(statusUpdateError = null) }
    }

    fun refreshMasterOrders() {
        viewModelScope.launch {
            ordersRepository.getMyOrders().onSuccess { orders ->
                _state.update { it.copy(masterOrders = orders) }
            }
        }
    }

    fun selectClientOrder(order: Order?) {
        _state.update { it.copy(selectedClientOrder = order, selectedOrderHasReview = false) }
        if (order != null && order.status == OrderStatus.COMPLETED) {
            viewModelScope.launch {
                reviewsRepository.existsForOrder(order.id).onSuccess { exists ->
                    _state.update { s ->
                        if (s.selectedClientOrder?.id != order.id) s
                        else s.copy(selectedOrderHasReview = exists)
                    }
                }
            }
        }
    }

    fun markSelectedReviewed() {
        _state.update { it.copy(selectedOrderHasReview = true) }
    }

    fun refreshClientOrders() {
        viewModelScope.launch {
            ordersRepository.getMyOrders().onSuccess { orders ->
                _state.update { s ->
                    val refreshed = s.selectedClientOrder?.let { sel ->
                        orders.firstOrNull { it.id == sel.id } ?: sel
                    }
                    s.copy(clientOrders = orders, selectedClientOrder = refreshed)
                }
            }
        }
    }

    fun refreshSelectedClientOrder() {
        val selectedId = _state.value.selectedClientOrder?.id ?: return
        viewModelScope.launch {
            ordersRepository.getOrderById(selectedId).onSuccess { updated ->
                _state.update { s ->
                    s.copy(
                        selectedClientOrder = updated,
                        clientOrders = s.clientOrders.map {
                            if (it.id == updated.id) updated else it
                        }
                    )
                }
            }
        }
    }

    fun refreshSelectedMasterOrder() {
        val selectedId = _state.value.selectedMasterOrder?.id ?: return
        viewModelScope.launch {
            ordersRepository.getOrderById(selectedId).onSuccess { updated ->
                _state.update { s ->
                    s.copy(
                        selectedMasterOrder = updated,
                        masterOrders = s.masterOrders.map {
                            if (it.id == updated.id) updated else it
                        }
                    )
                }
            }
        }
    }
}
