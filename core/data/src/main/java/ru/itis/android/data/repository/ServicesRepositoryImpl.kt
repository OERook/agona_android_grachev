package ru.itis.android.data.repository

import ru.itis.android.data.error.NetworkErrorMapper
import ru.itis.android.model.Service
import ru.itis.android.network.api.ServicesApi
import ru.itis.android.network.models.CreateServiceRequest
import ru.itis.android.network.models.NetworkService
import ru.itis.android.repository.ServicesRepository
import javax.inject.Inject

class ServicesRepositoryImpl @Inject constructor(
    private val servicesApi: ServicesApi
) : ServicesRepository {

    override suspend fun getAllServices(): Result<List<Service>> = safeCall {
        servicesApi.getAllServices().map { it.toDomain() }
    }

    override suspend fun getMyServices(): Result<List<Service>> = safeCall {
        servicesApi.getMyServices().map { it.toDomain() }
    }

    override suspend fun createService(
        title: String,
        description: String,
        price: Int,
        categoryId: Long
    ): Result<Service> = safeCall {
        val response = servicesApi.createService(
            CreateServiceRequest(
                title = title,
                description = description,
                price = price,
                categoryId = categoryId
            )
        )
        if (!response.isSuccessful) throw Exception(NetworkErrorMapper.fromResponse(response))
        response.body()?.toDomain() ?: throw Exception("Сервер не вернул данные")
    }

    override suspend fun updateService(
        id: Long,
        title: String,
        description: String,
        price: Int,
        categoryId: Long
    ): Result<Service> = safeCall {
        val response = servicesApi.updateService(
            id,
            CreateServiceRequest(
                title = title,
                description = description,
                price = price,
                categoryId = categoryId
            )
        )
        if (!response.isSuccessful) throw Exception(NetworkErrorMapper.fromResponse(response))
        response.body()?.toDomain() ?: throw Exception("Сервер не вернул данные")
    }

    override suspend fun deleteService(id: Long): Result<Unit> = safeCall {
        val response = servicesApi.deleteService(id)
        if (!response.isSuccessful) throw Exception(NetworkErrorMapper.fromResponse(response))
    }

    private inline fun <T> safeCall(block: () -> T): Result<T> = try {
        Result.success(block())
    } catch (t: Throwable) {
        Result.failure(Exception(NetworkErrorMapper.fromThrowable(t)))
    }

    private fun NetworkService.toDomain(): Service = Service(
        id = id,
        title = title,
        description = description,
        price = price,
        categoryId = categoryId,
        categoryName = categoryName,
        masterId = masterId,
        masterName = masterName
    )
}
