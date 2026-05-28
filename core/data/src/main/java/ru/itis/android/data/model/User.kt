package ru.itis.android.data.model

import ru.itis.android.model.ServiceCategory
import ru.itis.android.model.UserRole

sealed interface UserInfo {

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
    ) : UserInfo

    data class MasterInfo(
        val about: String,
        val experienceYears: Int,
        val rating: Double,
        val isVerified: Boolean,
        val categories: List<ServiceCategory>,
    ) : UserInfo

    data class ClientInfo(
        val totalOrders: Int,
        val completedOrders: Int
    ) : UserInfo
}
