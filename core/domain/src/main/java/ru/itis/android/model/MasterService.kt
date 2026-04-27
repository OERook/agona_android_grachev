package ru.itis.android.model

data class MasterService(
    val id: String,
    val categoryId: String,
    val title: String,
    val priceFrom: Double,
    val priceTo: Double?,
    val description: String?
)
