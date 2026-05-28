package ru.itis.android.network.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

interface AuthTokenProvider {
    suspend fun token(): String?
}

class AuthInterceptor @Inject constructor(
    private val tokenProvider: AuthTokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        if (original.header("Authorization") != null) {
            return chain.proceed(original)
        }

        val token = runBlocking { tokenProvider.token() }
        if (token.isNullOrBlank()) {
            return chain.proceed(original)
        }

        val authorized = original.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(authorized)
    }
}
