package ru.itis.android.network.models

import com.google.gson.annotations.SerializedName

data class CreateServiceRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Int,
    @SerializedName("category_id") val categoryId: Long
)

data class NetworkService(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val price: Int,
    @SerializedName("category_id") val categoryId: Long?,
    @SerializedName("category_name") val categoryName: String?,
    @SerializedName("master_id") val masterId: String?,
    @SerializedName("master_name") val masterName: String?
)
