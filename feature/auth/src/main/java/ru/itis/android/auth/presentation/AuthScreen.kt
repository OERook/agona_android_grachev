package ru.itis.android.auth.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthScreen : NavKey{
    @Serializable data object Start : AuthScreen
    @Serializable data object Phone : AuthScreen
    @Serializable data object Role : AuthScreen

    @Serializable data object MasterProfile : AuthScreen

    @Serializable data object ClientProfile : AuthScreen
}