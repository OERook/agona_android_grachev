package ru.itis.android.auth.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.itis.android.auth.domain.repository.AuthRepository
import ru.itis.android.data.datastore.SessionManager
import ru.itis.android.data.error.NetworkErrorMapper
import ru.itis.android.data.model.UserInfo
import ru.itis.android.database.dao.UserDao
import ru.itis.android.model.UserRole
import ru.itis.android.database.entity.UserEntity
import ru.itis.android.network.api.AuthApi
import ru.itis.android.network.models.AuthResponse
import ru.itis.android.network.models.LoginRequest
import ru.itis.android.network.models.NetworkUser
import ru.itis.android.network.models.RegisterRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun register(request: RegisterRequest): Result<UserInfo.User> =
        withContext(Dispatchers.IO) {
            try {
                val response = authApi.register(request)
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception(NetworkErrorMapper.fromResponse(response)))
                }
                val body = response.body()
                    ?: return@withContext Result.failure(Exception("Сервер не вернул данные"))
                Result.success(persistAndMap(body))
            } catch (t: Throwable) {
                Result.failure(Exception(NetworkErrorMapper.fromThrowable(t)))
            }
        }

    override suspend fun login(request: LoginRequest): Result<UserInfo.User> =
        withContext(Dispatchers.IO) {
            try {
                val response = authApi.login(request)
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception(NetworkErrorMapper.fromResponse(response)))
                }
                val body = response.body()
                    ?: return@withContext Result.failure(Exception("Сервер не вернул данные"))
                Result.success(persistAndMap(body))
            } catch (t: Throwable) {
                Result.failure(Exception(NetworkErrorMapper.fromThrowable(t)))
            }
        }

    override fun checkAuthStatus(): Flow<Boolean> {
        return sessionManager.tokenFlow.map { token -> !token.isNullOrBlank() }
    }

    private suspend fun persistAndMap(body: AuthResponse): UserInfo.User {
        sessionManager.saveToken(body.accessToken)
        val networkUser: NetworkUser = body.user
        sessionManager.saveRole(networkUser.role)

        userDao.saveUser(
            UserEntity(
                id = networkUser.id,
                phone = networkUser.phone,
                email = networkUser.email,
                fullName = networkUser.fullName,
                role = networkUser.role,
                about = networkUser.about,
                experienceYears = networkUser.experienceYears
            )
        )

        val domainRole = UserRole.fromRaw(networkUser.role)

        return UserInfo.User(
            id = networkUser.id,
            phone = networkUser.phone,
            email = networkUser.email,
            fullName = networkUser.fullName,
            role = domainRole,
            avatarUrl = networkUser.avatarUrl,
            isActive = true,
            masterInfo = if (domainRole == UserRole.MASTER) {
                UserInfo.MasterInfo(
                    about = networkUser.about ?: "",
                    experienceYears = networkUser.experienceYears ?: 0,
                    rating = 0.0,
                    isVerified = false,
                    categories = emptyList()
                )
            } else null
        )
    }
}
