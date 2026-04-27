package ru.itis.android.data.repository

import ru.itis.android.data.mapper.toDomain
import ru.itis.android.model.MasterProfile
import ru.itis.android.model.ServiceCategory

import ru.itis.android.network.api.MastersApi
import ru.itis.android.repository.MasterRepository
import javax.inject.Inject

class MastersRepositoryImpl @Inject constructor(
    private val api: MastersApi
) : MasterRepository {

    override suspend fun getCategories(): Result<List<ServiceCategory>> {
        return runCatching {
            val dtoList = api.getCategories()
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun getMasters(categoryId: String?): Result<List<MasterProfile>> {
        return runCatching {
            val dtoList = api.getMasters(categoryId)
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun getMasterById(masterId: String): Result<MasterProfile> {
        return runCatching {
            val dto = api.getMasterById(masterId)
            dto.toDomain()
        }
    }
}