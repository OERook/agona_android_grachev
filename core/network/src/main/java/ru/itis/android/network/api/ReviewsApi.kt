package ru.itis.android.network.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import ru.itis.android.network.models.ExistsResponse
import ru.itis.android.network.models.NetworkReview

interface ReviewsApi {

    @Multipart
    @POST("api/reviews")
    suspend fun createReview(
        @Part("order_id") orderId: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("comment") comment: RequestBody?,
        @Part files: List<MultipartBody.Part>
    ): Response<NetworkReview>

    @GET("masters/{id}/reviews")
    suspend fun getReviewsForMaster(@Path("id") masterId: String): List<NetworkReview>

    @GET("api/reviews/by-order/{orderId}")
    suspend fun existsForOrder(@Path("orderId") orderId: Long): ExistsResponse
}
