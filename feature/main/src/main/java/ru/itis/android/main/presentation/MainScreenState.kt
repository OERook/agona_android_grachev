package ru.itis.android.main.presentation

import ru.itis.android.model.Category
import ru.itis.android.model.Service
import ru.itis.android.model.UserRole

data class MainScreenState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val services: List<Service> = emptyList(),
    val role: UserRole = UserRole.CLIENT,
    val errorMessage: String? = null,
    val selectedCategoryId: Long? = null,
    val selectedService: Service? = null
)
