package ru.itis.android.model

enum class MessageStatus { PENDING, SENT }

data class ChatMessage(
    val clientMessageId: String,
    val serverId: Long?,
    val roomId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val createdAt: String,
    val status: MessageStatus
)

data class ChatRoom(
    val id: String,
    val orderId: Long?,
    val peerId: String,
    val peerName: String,
    val lastMessagePreview: String?,
    val lastMessageAt: String?
)
