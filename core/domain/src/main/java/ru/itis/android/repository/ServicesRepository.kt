package ru.itis.android.repository

import ru.itis.android.model.Service

interface ServicesRepository {
    suspend fun getAllServices(): Result<List<Service>>
    suspend fun getMyServices(): Result<List<Service>>
    suspend fun createService(
        title: String,
        description: String,
        price: Int,
        categoryId: Long
    ): Result<Service>

    suspend fun updateService(
        id: Long,
        title: String,
        description: String,
        price: Int,
        categoryId: Long
    ): Result<Service>

    suspend fun deleteService(id: Long): Result<Unit>
}
