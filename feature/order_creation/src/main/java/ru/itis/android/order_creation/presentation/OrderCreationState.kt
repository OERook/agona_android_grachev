package ru.itis.android.order_creation.presentation

import ru.itis.android.model.Order
import ru.itis.android.model.Service

data class OrderFormState(
    val service: Service? = null,
    val date: String = "",
    val time: String = "",
    val address: String = "",
    val description: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val createdOrder: Order? = null
)
