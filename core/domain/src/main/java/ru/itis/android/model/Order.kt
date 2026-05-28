package ru.itis.android.model

enum class OrderStatus {
    PENDING,
    ACCEPTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    DISPUTED
}

data class Order(
    val id: Long,
    val clientId: String,
    val masterId: String,
    val serviceId: Long? = null,
    val serviceTitle: String? = null,
    val serviceCategoryId: String,
    val status: OrderStatus,
    val address: String,
    val scheduledDate: String,
    val scheduledTime: String,
    val description: String,
    val estimatedPrice: Double,
    val finalPrice: Double? = null,
    val createdAt: String,
    val acceptedAt: String? = null,
    val startedAt: String? = null,
    val completedAt: String? = null,
    val cancelledAt: String? = null,

    val availableActions: List<OrderStatus> = emptyList()
)
