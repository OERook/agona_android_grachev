package ru.itis.android.order_creation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.android.model.CreateOrderRequest
import ru.itis.android.model.Service
import ru.itis.android.repository.OrdersRepository
import javax.inject.Inject

class OrderCreationViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrderFormState())
    val state: StateFlow<OrderFormState> = _state.asStateFlow()

    fun initWithService(service: Service) {
        _state.update { it.copy(service = service, createdOrder = null, errorMessage = null) }
    }

    fun updateDate(value: String) = _state.update { it.copy(date = value, errorMessage = null) }
    fun updateTime(value: String) = _state.update { it.copy(time = value, errorMessage = null) }
    fun updateAddress(value: String) = _state.update { it.copy(address = value, errorMessage = null) }
    fun updateDescription(value: String) = _state.update { it.copy(description = value, errorMessage = null) }

    fun submit() {
        val s = _state.value
        val service = s.service ?: return

        when {
            s.date.isBlank() -> { _state.update { it.copy(errorMessage = "Укажите дату") }; return }
            s.time.isBlank() -> { _state.update { it.copy(errorMessage = "Укажите время") }; return }
            s.address.isBlank() -> { _state.update { it.copy(errorMessage = "Укажите адрес") }; return }
            s.description.trim().length < 20 -> {
                _state.update { it.copy(errorMessage = "Описание должно содержать не менее 20 символов") }
                return
            }
        }

        _state.update { it.copy(isSubmitting = true, errorMessage = null) }

        viewModelScope.launch {
            ordersRepository.createOrder(
                CreateOrderRequest(
                    serviceId = service.id,
                    address = s.address.trim(),
                    scheduledDate = s.date.trim(),
                    scheduledTime = s.time.trim(),
                    description = s.description.trim(),
                    estimatedPrice = service.price.toDouble()
                )
            ).onSuccess { order ->
                _state.update { it.copy(isSubmitting = false, createdOrder = order) }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = error.message ?: "Не удалось создать заказ"
                    )
                }
            }
        }
    }

    fun refreshOrder() {
        val orderId = _state.value.createdOrder?.id ?: return
        viewModelScope.launch {
            ordersRepository.getOrderById(orderId).onSuccess { updated ->
                _state.update { it.copy(createdOrder = updated) }
            }
        }
    }

    fun reset() {
        _state.value = OrderFormState()
    }
}
