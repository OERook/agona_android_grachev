package ru.itis.android.auth.domain.repository

import ru.itis.android.auth.presentation.RegistrationState
import ru.itis.android.data.model.UserInfo

interface AuthRepository {
    suspend fun register(data: RegistrationState) : Result<UserInfo.User>
}