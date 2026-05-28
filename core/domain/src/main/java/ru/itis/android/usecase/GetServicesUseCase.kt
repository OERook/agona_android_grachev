package ru.itis.android.usecase

import ru.itis.android.model.Service
import ru.itis.android.repository.ServicesRepository
import javax.inject.Inject

class GetServicesUseCase @Inject constructor(
    private val repository: ServicesRepository
) {
    suspend operator fun invoke(): Result<List<Service>> = repository.getAllServices()
}

class GetMyServicesUseCase @Inject constructor(
    private val repository: ServicesRepository
) {
    suspend operator fun invoke(): Result<List<Service>> = repository.getMyServices()
}

class CreateServiceUseCase @Inject constructor(
    private val repository: ServicesRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        price: Int,
        categoryId: Long
    ): Result<Service> = repository.createService(title, description, price, categoryId)
}
