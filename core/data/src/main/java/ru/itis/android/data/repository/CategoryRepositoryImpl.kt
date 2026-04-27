package ru.itis.android.data.repository


import ru.itis.android.model.Category
import ru.itis.android.network.api.CategoryApi
import ru.itis.android.repository.CategoryRepository
import javax.inject.Inject


class CategoryRepositoryImpl @Inject constructor(
    private val categoryApi: CategoryApi
) : CategoryRepository {

    override suspend fun fetchCategories(): Result<List<Category>> {
        return try {
            val networkCategories = categoryApi.getCategories()

            val domainCategories = networkCategories.map { networkModel ->
                Category(
                    id = networkModel.id,
                    name = networkModel.name
                )
            }

            Result.success(domainCategories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}