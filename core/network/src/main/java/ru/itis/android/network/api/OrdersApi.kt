package ru.itis.android.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import ru.itis.android.network.models.NetworkCreateOrderRequest
import ru.itis.android.network.models.NetworkOrder

interface OrdersApi {
    @POST("api/orders")
    suspend fun createOrder(@Body request: NetworkCreateOrderRequest): Response<NetworkOrder>

    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") id: Long): NetworkOrder

    @GET("api/orders/my")
    suspend fun getMyOrders(): List<NetworkOrder>

    @PATCH("api/orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") id: Long,
        @Body body: Map<String, String>
    ): Response<NetworkOrder>
}
