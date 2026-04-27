package ru.itis.android.main.presentation

import ru.itis.android.model.Category


sealed class MainScreenState {
    object Loading : MainScreenState()
    data class Success(val categories: List<Category>) : MainScreenState()
    data class Error(val message: String): MainScreenState()
}