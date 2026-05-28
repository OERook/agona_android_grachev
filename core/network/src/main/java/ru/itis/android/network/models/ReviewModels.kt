package ru.itis.android.network.models

import com.google.gson.annotations.SerializedName

data class NetworkReviewAttachment(
    @SerializedName("id") val id: Long,
    @SerializedName("url") val url: String,
    @SerializedName("original_filename") val originalFilename: String? = null,
    @SerializedName("mime_type") val mimeType: String? = null,
    @SerializedName("size_bytes") val sizeBytes: Long? = null
)

data class NetworkReview(
    @SerializedName("id") val id: Long,
    @SerializedName("order_id") val orderId: Long,
    @SerializedName("client_id") val clientId: String,
    @SerializedName("client_name") val clientName: String,
    @SerializedName("master_id") val masterId: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String? = null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("attachments") val attachments: List<NetworkReviewAttachment>? = null
)

data class ExistsResponse(
    @SerializedName("exists") val exists: Boolean
)
