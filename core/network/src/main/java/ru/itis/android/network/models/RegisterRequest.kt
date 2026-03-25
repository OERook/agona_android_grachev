package ru.itis.android.network.models

data class RegisterRequest(
    val phone: String,
    val password: String,
    val email: String,
    val fullName: String,
    val role: String,
    val city: String,
    val about: String? = null,
    val experienceYears: Int? = null,
    val categories: List<String>? = null
)
