package ru.itis.android.auth.presentation

data class RegistrationState(
    val phone: String = "",
    val password: String = "",
    val email: String = "",
    val fullName: String = "",
    val city: String = "",
    val role: String = "client",
    val about: String? = null,
    val experienceYears: Int? = null,
    val selectedCategories: List<String>? = emptyList(),

    val isloading: Boolean = false,
    val errorMessage: String? = null,
    val currentStep: Int = 1
)
