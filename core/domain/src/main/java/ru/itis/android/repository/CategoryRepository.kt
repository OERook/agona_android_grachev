package ru.itis.android.repository

import ru.itis.android.model.Category

interface CategoryRepository {
    suspend fun fetchCategories(forceRefresh: Boolean = false): Result<List<Category>>
}
