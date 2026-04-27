package ru.itis.android.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.android.usecase.GetCategoriesUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.value = MainScreenState.Loading

            val result = getCategoriesUseCase()

            result.fold(
                onSuccess = { categories ->
                    _state.value = MainScreenState.Success(categories)
                },
                onFailure = { error ->
                    _state.value = MainScreenState.Error(error.message ?: "Произошла ошибка при загрузке")
                }
            )
        }
    }
}