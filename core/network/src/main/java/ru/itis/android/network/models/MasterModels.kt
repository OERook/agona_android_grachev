package ru.itis.android.network.models

import com.google.gson.annotations.SerializedName

data class ServiceCategoryDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("icon_url")
    val iconUrl: String? = null
)

data class MasterServiceDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("price_from")
    val priceFrom: Double,
    @SerializedName("price_to")
    val priceTo: Double? = null,
    @SerializedName("description")
    val description: String? = null
)

data class MasterProfileDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    @SerializedName("about")
    val about: String,
    @SerializedName("experience_years")
    val experienceYears: Int,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("reviews_count")
    val reviewsCount: Int,
    @SerializedName("completed_jobs")
    val completedJobs: Int,
    @SerializedName("is_verified")
    val isVerified: Boolean,
    @SerializedName("services")
    val services: List<MasterServiceDto>
)