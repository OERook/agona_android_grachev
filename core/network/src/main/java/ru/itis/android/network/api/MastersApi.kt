package ru.itis.android.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.itis.android.network.models.MasterProfileDto
import ru.itis.android.network.models.ServiceCategoryDto

interface MastersApi {

    @GET("/categories")
    suspend fun getCategories(): Response<List<ServiceCategoryDto>>

    @GET("/masters")
    suspend fun getMasters(@Query("categoryId") categoryId: String? = null): Response<List<MasterProfileDto>>

    @GET("/masters/{id}")
    suspend fun getMasterById(@Path("id") id: String): Response<MasterProfileDto>
}