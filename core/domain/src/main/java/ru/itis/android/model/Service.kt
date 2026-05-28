package ru.itis.android.model

data class Service(
    val id: Long,
    val title: String,
    val description: String?,
    val price: Int,
    val categoryId: Long?,
    val categoryName: String?,
    val masterId: String?,
    val masterName: String?
)
