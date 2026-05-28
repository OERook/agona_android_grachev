package ru.itis.android.profile.presentation

import ru.itis.android.model.Category
import ru.itis.android.model.MasterProfile
import ru.itis.android.model.Review
import ru.itis.android.model.Service
import ru.itis.android.model.UserRole

data class ProfileScreenState(
    val isLoading: Boolean = true,
    val role: UserRole = UserRole.CLIENT,
    val fullName: String = "",
    val phone: String = "",
    val email: String = "",
    val about: String? = null,
    val experienceYears: Int? = null,
    val myServices: List<Service> = emptyList(),
    val selectedMasterProfile: MasterProfile? = null,
    val masterProfileLoading: Boolean = false,
    val masterProfileError: String? = null,
    val masterReviews: List<Review> = emptyList(),
    val masterReviewsLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val errorMessage: String? = null
)

sealed interface CreateServiceState {
    data class Idle(
        val editingServiceId: Long? = null,
        val title: String = "",
        val description: String = "",
        val price: String = "",
        val categoryId: Long? = null,
        val isSubmitting: Boolean = false,
        val errorMessage: String? = null
    ) : CreateServiceState {
        val isEditing: Boolean get() = editingServiceId != null
    }

    data object Success : CreateServiceState
}
