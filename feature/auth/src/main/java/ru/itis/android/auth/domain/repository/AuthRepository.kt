package ru.itis.android.auth.domain.repository

import ru.itis.android.auth.presentation.RegistrationState
import ru.itis.android.data.model.UserInfo
import ru.itis.android.network.models.LoginRequest
import ru.itis.android.network.models.RegisterRequest

interface AuthRepository {
    suspend fun register(request: RegisterRequest) : Result<UserInfo.User>

    suspend fun login(request: LoginRequest): Result<UserInfo.User>
}