package ru.itis.android.auth.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itis.android.auth.domain.repository.AuthRepository
import ru.itis.android.database.dao.UserDao
import ru.itis.android.database.entity.UserEntity
import ru.itis.android.network.api.AuthApi
import ru.itis.android.network.models.LoginRequest
import ru.itis.android.network.models.RegisterRequest
import ru.itis.android.data.model.UserInfo
import ru.itis.android.data.model.UserRole
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun register(request: RegisterRequest): Result<UserInfo.User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApi.register(request)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val networkUser = body.user

                        val userEntity = UserEntity(
                            id = networkUser.id,
                            phone = networkUser.phone,
                            email = networkUser.email,
                            fullName = networkUser.fullName,
                            role = networkUser.role,
                            about = networkUser.about,
                            experienceYears = networkUser.experienceYears
                        )
                        userDao.saveUser(userEntity)

                        val domainRole = when(networkUser.role) {
                            "master" -> UserRole.MASTER
                            "admin" -> UserRole.ADMIN
                            else -> UserRole.USER
                        }

                        val domainUser = UserInfo.User(
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

                        Result.success(domainUser)
                    } else {
                        Result.failure(Exception("Пустой ответ от сервера"))
                    }
                } else {
                    Result.failure(Exception("Ошибка сервера: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun login(request: LoginRequest): Result<UserInfo.User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApi.login(request)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val networkUser = body.user

                        val userEntity = UserEntity(
                            id = networkUser.id,
                            phone = networkUser.phone,
                            email = networkUser.email,
                            fullName = networkUser.fullName,
                            role = networkUser.role,
                            about = networkUser.about,
                            experienceYears = networkUser.experienceYears
                        )
                        userDao.saveUser(userEntity)

                        val domainRole = when(networkUser.role) {
                            "master" -> UserRole.MASTER
                            "admin" -> UserRole.ADMIN
                            else -> UserRole.USER
                        }

                        val domainUser = UserInfo.User(
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

                        Result.success(domainUser)
                    } else {
                        Result.failure(Exception("Пустой ответ от сервера"))
                    }
                } else {
                    Result.failure(Exception("Ошибка сервера: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}