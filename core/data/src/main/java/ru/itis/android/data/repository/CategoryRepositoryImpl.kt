package ru.itis.android.data.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.itis.android.data.error.NetworkErrorMapper
import ru.itis.android.model.Category
import ru.itis.android.network.api.CategoryApi
import ru.itis.android.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryApi: CategoryApi
) : CategoryRepository {

    private val mutex = Mutex()
    @Volatile
    private var cached: List<Category>? = null

    override suspend fun fetchCategories(forceRefresh: Boolean): Result<List<Category>> {
        if (!forceRefresh) {
            cached?.let { return Result.success(it) }
        }

        return mutex.withLock {

            if (!forceRefresh) {
                cached?.let { return@withLock Result.success(it) }
            }
            val result = fetchFromNetwork()
            result.onSuccess { cached = it }
            result
        }
    }

    private suspend fun fetchFromNetwork(): Result<List<Category>> = try {
        val response = categoryApi.getCategories()
        if (response.isSuccessful) {
            val networkCategories = response.body() ?: emptyList()
            Result.success(networkCategories.map { Category(id = it.id, name = it.name) })
        } else {
            Result.failure(Exception(NetworkErrorMapper.fromResponse(response)))
        }
    } catch (t: Throwable) {
        Result.failure(Exception(NetworkErrorMapper.fromThrowable(t)))
    }
}
