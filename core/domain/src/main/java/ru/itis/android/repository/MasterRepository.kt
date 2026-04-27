package ru.itis.android.repository

import ru.itis.android.model.MasterProfile
import ru.itis.android.model.ServiceCategory

interface MasterRepository {
    suspend fun getCategories(): Result<List<ServiceCategory>>

    suspend fun getMasters(categoryId: String? = null): Result<List<MasterProfile>>

    suspend fun getMasterById(masterId: String): Result<MasterProfile>
}