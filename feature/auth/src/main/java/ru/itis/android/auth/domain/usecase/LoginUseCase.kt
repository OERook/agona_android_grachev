package ru.itis.android.auth.domain.usecase

import ru.itis.android.auth.domain.repository.AuthRepository
import ru.itis.android.data.model.UserInfo
import ru.itis.android.network.models.LoginRequest
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String, password: String): Result<UserInfo.User> {
        if (phone.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Пожалуйста, заполните телефон и пароль"))
        }

        val request = LoginRequest(phone = phone, password = password)
        return repository.login(request)
    }
}