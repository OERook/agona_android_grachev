package ru.itis.android.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.itis.android.network.models.CreateServiceRequest
import ru.itis.android.network.models.NetworkService

interface ServicesApi {

    @GET("api/services")
    suspend fun getAllServices(): List<NetworkService>

    @GET("api/services/my")
    suspend fun getMyServices(): List<NetworkService>

    @POST("api/services")
    suspend fun createService(@Body request: CreateServiceRequest): Response<NetworkService>

    @PUT("api/services/{id}")
    suspend fun updateService(
        @Path("id") id: Long,
        @Body request: CreateServiceRequest
    ): Response<NetworkService>

    @DELETE("api/services/{id}")
    suspend fun deleteService(@Path("id") id: Long): Response<Unit>
}
