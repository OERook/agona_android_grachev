package ru.itis.android.orders.presentation

data class PendingAttachment(
    val uri: String,
    val mimeType: String,
    val displayName: String
)

data class WriteReviewState(
    val orderId: Long = 0L,
    val rating: Int = 0,
    val comment: String = "",
    val attachments: List<PendingAttachment> = emptyList(),
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val submitted: Boolean = false
)
