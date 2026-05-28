package ru.itis.android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_rooms")
data class ChatRoomEntity(
    @PrimaryKey val id: String,
    val orderId: Long? = null,
    val peerId: String,
    val peerName: String,
    val lastMessagePreview: String? = null,
    val lastMessageAt: String? = null
)
