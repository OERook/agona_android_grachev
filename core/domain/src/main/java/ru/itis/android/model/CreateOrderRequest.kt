package ru.itis.android.model

data class CreateOrderRequest(
    val serviceId: Long,
    val address: String,
    val scheduledDate: String,
    val scheduledTime: String,
    val description: String,
    val estimatedPrice: Double
)
