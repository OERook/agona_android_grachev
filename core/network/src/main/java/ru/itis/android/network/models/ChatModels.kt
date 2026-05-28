package ru.itis.android.network.models

import com.google.gson.annotations.SerializedName

data class NetworkChatRoom(
    @SerializedName("id") val id: String,
    @SerializedName("order_id") val orderId: Long? = null,
    @SerializedName("client_id") val clientId: String,
    @SerializedName("client_name") val clientName: String,
    @SerializedName("master_id") val masterId: String,
    @SerializedName("master_name") val masterName: String,
    @SerializedName("peer_id") val peerId: String,
    @SerializedName("peer_name") val peerName: String,
    @SerializedName("last_message_preview") val lastMessagePreview: String? = null,
    @SerializedName("last_message_at") val lastMessageAt: String? = null
)

data class NetworkChatMessage(
    @SerializedName("id") val id: Long,
    @SerializedName("room_id") val roomId: String,
    @SerializedName("sender_id") val senderId: String,
    @SerializedName("sender_name") val senderName: String,
    @SerializedName("client_message_id") val clientMessageId: String,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt: String
)

data class NetworkSendMessageRequest(
    @SerializedName("room_id") val roomId: String,
    @SerializedName("client_message_id") val clientMessageId: String,
    @SerializedName("content") val content: String
)

data class NetworkOpenRoomRequest(
    @SerializedName("order_id") val orderId: Long
)
