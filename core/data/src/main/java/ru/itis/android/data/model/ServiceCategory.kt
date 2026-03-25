package ru.itis.android.data.model

data class ServiceCategory(
    val id: String,
    val name: String,
    val description: String?,
    val orderIndex: Int,
    val isActive: Boolean
)
