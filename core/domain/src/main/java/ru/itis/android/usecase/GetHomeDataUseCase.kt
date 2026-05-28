package ru.itis.android.usecase

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.itis.android.model.MasterProfile
import ru.itis.android.model.ServiceCategory
import ru.itis.android.repository.MasterRepository
import javax.inject.Inject

data class HomeData(
    val categories: List<ServiceCategory>,
    val popularMasters: List<MasterProfile>
)

class GetHomeDataUseCase @Inject constructor(
    private val repository: MasterRepository
) {
    suspend operator fun invoke(): Result<HomeData> {
        return try {
            coroutineScope {
                val categoriesDeferred = async { repository.getCategories() }
                val mastersDeferred = async { repository.getMasters() }

                val categoriesResult = categoriesDeferred.await()
                val mastersResult = mastersDeferred.await()

                if (categoriesResult.isFailure) {
                    return@coroutineScope Result.failure(categoriesResult.exceptionOrNull()!!)
                }
                if (mastersResult.isFailure) {
                    return@coroutineScope Result.failure(mastersResult.exceptionOrNull()!!)
                }

                Result.success(HomeData(
                    categories = categoriesResult.getOrDefault(emptyList()),
                    popularMasters = mastersResult.getOrDefault(emptyList())
                ))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}