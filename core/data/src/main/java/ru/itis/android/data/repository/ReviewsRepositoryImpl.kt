package ru.itis.android.data.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import ru.itis.android.data.error.NetworkErrorMapper
import ru.itis.android.model.Review
import ru.itis.android.model.ReviewAttachment
import ru.itis.android.network.api.ReviewsApi
import ru.itis.android.network.models.NetworkReview
import ru.itis.android.network.models.NetworkReviewAttachment
import ru.itis.android.repository.ReviewFileInput
import ru.itis.android.repository.ReviewsRepository
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor(
    private val reviewsApi: ReviewsApi,
    private val retrofit: Retrofit
) : ReviewsRepository {

    private val baseHost: String by lazy {
        retrofit.baseUrl().toString().trimEnd('/')
    }

    override suspend fun createReview(
        orderId: Long,
        rating: Int,
        comment: String?,
        files: List<ReviewFileInput>
    ): Result<Review> = safeCall {
        val parts = files.map { input ->
            val media = (input.mimeType.takeIf { it.isNotBlank() } ?: "application/octet-stream")
                .toMediaTypeOrNull()
            val body = input.bytes.toRequestBody(media)
            MultipartBody.Part.createFormData("files", input.fileName, body)
        }

        val response = reviewsApi.createReview(
            orderId = orderId.toString().toRequestBody(TEXT),
            rating = rating.toString().toRequestBody(TEXT),
            comment = comment?.toRequestBody(TEXT),
            files = parts
        )
        if (!response.isSuccessful) throw Exception(NetworkErrorMapper.fromResponse(response))
        response.body()?.toDomain() ?: throw Exception("Сервер не вернул данные")
    }

    override suspend fun getReviewsForMaster(masterId: String): Result<List<Review>> = safeCall {
        reviewsApi.getReviewsForMaster(masterId).map { it.toDomain() }
    }

    override suspend fun existsForOrder(orderId: Long): Result<Boolean> = safeCall {
        reviewsApi.existsForOrder(orderId).exists
    }

    private inline fun <T> safeCall(block: () -> T): Result<T> = try {
        Result.success(block())
    } catch (t: Throwable) {
        Result.failure(Exception(NetworkErrorMapper.fromThrowable(t)))
    }

    private fun NetworkReview.toDomain(): Review = Review(
        id = id,
        orderId = orderId,
        clientId = clientId,
        clientName = clientName,
        masterId = masterId,
        rating = rating,
        comment = comment,
        createdAt = createdAt,
        attachments = attachments.orEmpty().map { it.toDomain() }
    )

    private fun NetworkReviewAttachment.toDomain(): ReviewAttachment = ReviewAttachment(
        id = id,
        url = if (url.startsWith("http")) url else baseHost + url,
        originalFilename = originalFilename,
        mimeType = mimeType,
        sizeBytes = sizeBytes
    )

    companion object {
        private val TEXT = "text/plain".toMediaTypeOrNull()
    }
}
