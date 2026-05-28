package ru.itis.android.network.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("role") val role: String,
    @SerializedName("city") val city: String,
    @SerializedName("about") val about: String? = null,
    @SerializedName("experience_years") val experienceYears: Int? = null,
    @SerializedName("categories") val categories: List<String>? = null
)
