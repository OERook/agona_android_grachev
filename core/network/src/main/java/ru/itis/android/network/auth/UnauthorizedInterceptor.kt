package ru.itis.android.network.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

interface SessionInvalidator {
    suspend fun invalidate()
}


class UnauthorizedInterceptor @Inject constructor(
    private val invalidator: SessionInvalidator
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 401 && !request.url.encodedPath.startsWith("/auth")) {
            runBlocking { invalidator.invalidate() }
        }
        return response
    }
}
