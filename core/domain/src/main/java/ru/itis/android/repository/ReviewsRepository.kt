package ru.itis.android.repository

import ru.itis.android.model.Review


data class ReviewFileInput(
    val bytes: ByteArray,
    val fileName: String,
    val mimeType: String
)

interface ReviewsRepository {

    suspend fun createReview(
        orderId: Long,
        rating: Int,
        comment: String?,
        files: List<ReviewFileInput>
    ): Result<Review>

    suspend fun getReviewsForMaster(masterId: String): Result<List<Review>>

    suspend fun existsForOrder(orderId: Long): Result<Boolean>
}
