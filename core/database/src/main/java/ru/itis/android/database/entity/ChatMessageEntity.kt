package ru.itis.android.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "chat_messages",
    indices = [Index(value = ["roomId"]), Index(value = ["status"])]
)
data class ChatMessageEntity(
    @PrimaryKey val clientMessageId: String,
    val serverId: Long? = null,
    val roomId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val createdAt: String,
    val status: String
)
