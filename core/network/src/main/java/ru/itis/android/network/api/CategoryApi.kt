package ru.itis.android.network.api

import retrofit2.http.GET
import ru.itis.android.network.models.NetworkCategory

interface CategoryApi {
    @GET("/api/categories")
    suspend fun getCategories(): List<NetworkCategory>
}