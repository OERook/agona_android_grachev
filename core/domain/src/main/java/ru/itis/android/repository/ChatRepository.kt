package ru.itis.android.repository

import kotlinx.coroutines.flow.Flow
import ru.itis.android.model.ChatMessage
import ru.itis.android.model.ChatRoom


interface ChatRepository {

    fun observeRooms(): Flow<List<ChatRoom>>

    fun observeMessages(roomId: String): Flow<List<ChatMessage>>

    suspend fun openRoomForOrder(orderId: Long): Result<ChatRoom>

    suspend fun refreshRooms(): Result<Unit>

    suspend fun refreshMessages(roomId: String): Result<Unit>


    suspend fun sendMessage(roomId: String, text: String): Result<Unit>


    suspend fun connect()

    suspend fun disconnect()

    suspend fun subscribeRoom(roomId: String)
}
