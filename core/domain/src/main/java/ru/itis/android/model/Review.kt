package ru.itis.android.model

data class ReviewAttachment(
    val id: Long,
    val url: String,
    val originalFilename: String?,
    val mimeType: String?,
    val sizeBytes: Long?
)

data class Review(
    val id: Long,
    val orderId: Long,
    val clientId: String,
    val clientName: String,
    val masterId: String,
    val rating: Int,
    val comment: String?,
    val createdAt: String,
    val attachments: List<ReviewAttachment>
)
