package ru.itis.android.network.models

data class AuthResponse(
    val accessToken: String,
    val userId: String,
    val role: String
)
