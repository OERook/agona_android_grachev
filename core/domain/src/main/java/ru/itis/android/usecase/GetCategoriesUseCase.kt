package ru.itis.android.usecase

import ru.itis.android.model.Category
import ru.itis.android.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): Result<List<Category>> {
        return repository.fetchCategories(forceRefresh)
    }
}
