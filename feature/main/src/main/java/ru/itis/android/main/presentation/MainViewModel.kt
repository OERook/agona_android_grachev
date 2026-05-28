package ru.itis.android.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.android.data.session.CurrentUserHolder
import ru.itis.android.model.UserRole
import ru.itis.android.usecase.GetCategoriesUseCase
import ru.itis.android.usecase.GetServicesUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getServicesUseCase: GetServicesUseCase,
    private val currentUserHolder: CurrentUserHolder
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        observeUser()
        loadCatalog()
    }

    fun refresh() {
        loadCatalog(forceRefresh = true)
    }

    private fun observeUser() {
        viewModelScope.launch {
            currentUserHolder.user.collect { user ->
                _state.update { it.copy(role = UserRole.fromRaw(user?.role)) }
            }
        }
    }

    private fun loadCatalog(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            getCategoriesUseCase(forceRefresh).onSuccess { categories ->
                _state.update { it.copy(categories = categories) }
            }.onFailure { error ->
                _state.update { it.copy(errorMessage = error.message) }
            }

            getServicesUseCase().onSuccess { services ->
                _state.update { it.copy(services = services) }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    fun selectCategory(categoryId: Long?) {
        _state.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun selectService(service: ru.itis.android.model.Service?) {
        _state.update { it.copy(selectedService = service) }
    }

    fun refreshCatalog() {
        viewModelScope.launch {
            getServicesUseCase().onSuccess { services ->
                _state.update { it.copy(services = services) }
            }
        }
    }
}
