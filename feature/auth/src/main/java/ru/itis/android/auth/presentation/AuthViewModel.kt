package ru.itis.android.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.android.auth.domain.usecase.LoginUseCase
import ru.itis.android.auth.domain.usecase.RegisterUseCase
import javax.inject.Inject

sealed interface AuthEffect {
    data object NavigateToMain : AuthEffect
}

class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    private val _effect = Channel<AuthEffect>()
    val effect = _effect.receiveAsFlow()

    fun toggleLoginMode() {
        _state.update { it.copy(isLoginMode = !it.isLoginMode) }
    }

    fun updatePhone(phone: String) { _state.update { it.copy(phone = phone) } }
    fun updatePassword(password: String) { _state.update { it.copy(password = password) } }
    fun setRole(role: String) { _state.update { it.copy(role = role) } }
    fun updateFullName(fullName: String) { _state.update { it.copy(fullName = fullName) } }
    fun updateEmail(email: String) { _state.update { it.copy(email = email) } }
    fun updateCity(city: String) { _state.update { it.copy(city = city) } }
    fun updateAbout(about: String) { _state.update { it.copy(about = about) } }
    fun updateExperience(years: Int) { _state.update { it.copy(experienceYears = years) } }

    fun toggleCategory(categoryTitle: String) {
        val currentCategories = _state.value.selectedCategories?.toMutableList() ?: mutableListOf()
        if (currentCategories.contains(categoryTitle)) {
            currentCategories.remove(categoryTitle)
        } else {
            currentCategories.add(categoryTitle)
        }
        _state.update { it.copy(selectedCategories = currentCategories) }
    }

    fun nextStep() {
        _state.update { it.copy(currentStep = it.currentStep + 1) }
    }

    fun previousStep() {
        if (_state.value.currentStep > 1) {
            _state.update { it.copy(currentStep = it.currentStep - 1) }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun register() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            registerUseCase(state.value)
                .onSuccess {
                   _state.update { it.copy(isLoading = false, isAuthSuccessful = true) }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message
                        )
                    }
                }
        }
    }

    fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            loginUseCase(
                phone = state.value.phone,
                password = state.value.password
            )
                .onSuccess {
                   _state.update { it.copy(isLoading = false, isAuthSuccessful = true) }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message
                        )
                    }
                }
        }
    }
}
