package ru.itis.android.network.models

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("user") val user: NetworkUser
)

data class NetworkUser(
    @SerializedName("id") val id: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("role") val role: String,
    @SerializedName("city") val city: String? = null,
    @SerializedName("avatar_url") val avatarUrl: String? = null,
    @SerializedName("about") val about: String? = null,
    @SerializedName("experience_years") val experienceYears: Int? = null,
    @SerializedName("categories") val categories: List<String>? = null
)
