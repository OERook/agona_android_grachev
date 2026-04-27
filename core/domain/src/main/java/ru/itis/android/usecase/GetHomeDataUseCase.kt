package ru.itis.android.domain.usecase


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
                val categoriesDeferred = async { repository.getCategories().getOrThrow() }
                val mastersDeferred = async { repository.getMasters().getOrThrow() }

                val categories = categoriesDeferred.await()
                val masters = mastersDeferred.await()

                Result.success(HomeData(categories, masters))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}