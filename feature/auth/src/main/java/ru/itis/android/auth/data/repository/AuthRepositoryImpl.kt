package ru.itis.android.auth.data.repository

import ru.itis.android.auth.domain.repository.AuthRepository
import ru.itis.android.auth.presentation.RegistrationState
import ru.itis.android.data.model.UserInfo
import ru.itis.android.data.model.UserRole
import ru.itis.android.data.model.ServiceCategory // Добавь этот импорт
import ru.itis.android.network.api.AuthApi
import ru.itis.android.network.models.RegisterRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun register(data: RegistrationState): Result<UserInfo.User> {
        return try {

            val request = RegisterRequest(
                phone = data.phone,
                password = data.password,
                email = data.email,
                fullName = data.fullName,
                role = data.role,
                city = data.city,
                about = data.about,
                experienceYears = data.experienceYears,
                categories = data.selectedCategories
            )

            val response = authApi.register(request)

            if (response.isSuccessful) {
                val authResponse = response.body()
                    ?: return Result.failure(Exception("Сервер вернул пустой ответ"))

                val user = UserInfo.User(
                    id = authResponse.userId,
                    phone = data.phone,
                    email = data.email,
                    fullName = data.fullName,
                    role = try {
                        UserRole.valueOf(data.role.uppercase())
                    } catch (e: Exception) {
                        UserRole.USER
                    },
                    avatarUrl = null,
                    isActive = true,
                    masterInfo = if (data.role == "master") {
                        UserInfo.MasterInfo(
                            about = data.about ?: "",
                            experienceYears = data.experienceYears ?: 0,
                            rating = 0.0,
                            isVerified = false,
                            categories = emptyList<ServiceCategory>()
                        )
                    } else null
                )
                Result.success(user)
            } else {
                Result.failure(Exception("Ошибка регистрации: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}