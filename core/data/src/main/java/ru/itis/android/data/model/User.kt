package ru.itis.android.data.model

enum class UserRole(val value: String) {
    USER("client"),
    MASTER("master"),
    ADMIN("admin")
}


data class User(
    val id: String,
    val phone: String,
    val email: String,
    val fullName: String,
    val role: UserRole,
    val avatarUrl: String?,
    val isActive: Boolean,
    val masterInfo: MasterInfo? = null,
    val clientInfo: ClientInfo? = null
)

data class MasterInfo(
    val about: String,
    val experienceYears: Int,
    val rating: Double,
    val isVerified: Boolean,
    val categories: List<ServiceCategory>,
)

data class ClientInfo(
    val totalOrders: Int,
    val completedOrders: Int
)