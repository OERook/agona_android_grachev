package ru.itis.android.network.models

import com.google.gson.annotations.SerializedName

data class NetworkCreateOrderRequest(
    @SerializedName("service_id") val serviceId: Long,
    @SerializedName("address") val address: String,
    @SerializedName("scheduled_date") val scheduledDate: String,
    @SerializedName("scheduled_time") val scheduledTime: String,
    @SerializedName("description") val description: String,
    @SerializedName("estimated_price") val estimatedPrice: Double
)

data class NetworkOrder(
    @SerializedName("id") val id: Long,
    @SerializedName("client_id") val clientId: String,
    @SerializedName("master_id") val masterId: String,
    @SerializedName("service_id") val serviceId: Long? = null,
    @SerializedName("service_title") val serviceTitle: String? = null,
    @SerializedName("service_category_id") val serviceCategoryId: String? = null,
    @SerializedName("status") val status: String,
    @SerializedName("address") val address: String,
    @SerializedName("scheduled_date") val scheduledDate: String,
    @SerializedName("scheduled_time") val scheduledTime: String,
    @SerializedName("description") val description: String?,
    @SerializedName("estimated_price") val estimatedPrice: Double,
    @SerializedName("final_price") val finalPrice: Double? = null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("accepted_at") val acceptedAt: String? = null,
    @SerializedName("started_at") val startedAt: String? = null,
    @SerializedName("completed_at") val completedAt: String? = null,
    @SerializedName("cancelled_at") val cancelledAt: String? = null,
    @SerializedName("available_actions") val availableActions: List<String>? = null
)
