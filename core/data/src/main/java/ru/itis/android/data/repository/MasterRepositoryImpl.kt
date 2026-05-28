package ru.itis.android.data.repository

import ru.itis.android.data.error.NetworkErrorMapper
import ru.itis.android.data.mapper.toDomain
import ru.itis.android.model.MasterProfile
import ru.itis.android.model.ServiceCategory
import ru.itis.android.network.api.MastersApi
import ru.itis.android.repository.MasterRepository
import javax.inject.Inject

class MasterRepositoryImpl @Inject constructor(
    private val api: MastersApi
) : MasterRepository {

    override suspend fun getCategories(): Result<List<ServiceCategory>> = safeCall {
        val response = api.getCategories()
        if (!response.isSuccessful) throw Exception(NetworkErrorMapper.fromResponse(response))
        (response.body() ?: emptyList()).map { it.toDomain() }
    }

    override suspend fun getMasters(categoryId: String?): Result<List<MasterProfile>> = safeCall {
        val response = api.getMasters(categoryId)
        if (!response.isSuccessful) throw Exception(NetworkErrorMapper.fromResponse(response))
        (response.body() ?: emptyList()).map { it.toDomain() }
    }

    override suspend fun getMasterById(masterId: String): Result<MasterProfile> = safeCall {
        val response = api.getMasterById(masterId)
        if (!response.isSuccessful) throw Exception(NetworkErrorMapper.fromResponse(response))
        response.body()?.toDomain() ?: throw Exception("Мастер не найден")
    }

    private inline fun <T> safeCall(block: () -> T): Result<T> = try {
        Result.success(block())
    } catch (t: Throwable) {
        Result.failure(Exception(NetworkErrorMapper.fromThrowable(t)))
    }
}
