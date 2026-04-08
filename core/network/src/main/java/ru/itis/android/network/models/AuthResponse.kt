package ru.itis.android.network.models

data class AuthResponse(
    val accessToken: String,
    val user: NetworkUser
)

data class NetworkUser(
    val id: String,
    val phone: String,
    val email: String,
    val fullName: String,
    val role: String,
    val avatarUrl: String? = null,
    val about: String? = null,
    val experienceYears: Int? = null
)