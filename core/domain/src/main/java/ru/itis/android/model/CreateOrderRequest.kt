package ru.itis.android.model

data class CreateOrderRequest(
    val masterId: String,
    val serviceCategoryId: String,
    val address: String,
    val scheduledDate: String,
    val scheduledTime: String,
    val description: String,
    val estimatedPrice: Double
)
