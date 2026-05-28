package ru.itis.android.model

data class ServiceCategory(
    val id: String,
    val name: String,
    val description: String?,
    val iconUrl: String? = null
)
