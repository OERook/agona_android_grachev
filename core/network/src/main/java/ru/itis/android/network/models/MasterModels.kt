package ru.itis.android.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class ServiceCategoryDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("icon_url")
    val iconUrl: String? = null
)

@Serializable
data class MasterServiceDto(
    @SerialName("id")
    val id: String,
    @SerialName("category_id")
    val categoryId: String,
    @SerialName("title")
    val title: String,
    @SerialName("price_from")
    val priceFrom: Double,
    @SerialName("price_to")
    val priceTo: Double? = null,
    @SerialName("description")
    val description: String? = null
)

@Serializable
data class MasterProfileDto(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("about")
    val about: String,
    @SerialName("experience_years")
    val experienceYears: Int,
    @SerialName("rating")
    val rating: Double,
    @SerialName("reviews_count")
    val reviewsCount: Int,
    @SerialName("completed_jobs")
    val completedJobs: Int,
    @SerialName("is_verified")
    val isVerified: Boolean,
    @SerialName("services")
    val services: List<MasterServiceDto>
)