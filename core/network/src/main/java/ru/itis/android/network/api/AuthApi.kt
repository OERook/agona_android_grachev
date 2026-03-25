package ru.itis.android.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.itis.android.network.models.RegisterRequest
import ru.itis.android.network.models.AuthResponse
import ru.itis.android.network.models.LoginRequest

interface AuthApi {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>
}
