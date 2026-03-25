package ru.itis.android.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.android.network.api.AuthApi
import ru.itis.android.network.models.RegisterRequest
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authApi: AuthApi
) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    fun updatePhone(phone: String) {
        _state.value = _state.value.copy(phone = phone)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun setRole(role: String) {
        _state.update { it.copy(role = role) }
    }

    fun nextStep() {
        _state.update { it.copy(currentStep = it.currentStep + 1) }
    }

    fun previousStep() {
        if (_state.value.currentStep > 1){
            _state.update { it.copy(currentStep = it.currentStep - 1) }
        }
    }

    fun register() {
        val currentState = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isloading = true) }
            try {
                val request = RegisterRequest(
                    phone = currentState.phone,
                    password = currentState.password,
                    email = currentState.email,
                    fullName = currentState.fullName,
                    role = currentState.role,
                    city = currentState.city,
                    about = currentState.about,
                    experienceYears = currentState.experienceYears,
                    categories = currentState.selectedCategories
                )

                val response = authApi.register(request)

                if (response.isSuccessful){
                    _state.update {it.copy(isloading = false)}
                } else {
                    _state.update {it.copy(isloading = false, errorMessage = "Ошибка регистрации")}
                }
            }catch (e : Exception){
                _state.update {it.copy(isloading = false, errorMessage = e.message)}
            }
        }
    }
}