package ru.itis.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.itis.android.database.entity.ChatMessageEntity

@Dao
interface ChatMessageDao {

    @Query("select * from chat_messages where roomId = :roomId order by createdAt asc")
    fun observeForRoom(roomId: String): Flow<List<ChatMessageEntity>>

    @Query("select coalesce(max(serverId), 0) from chat_messages where roomId = :roomId")
    suspend fun maxServerId(roomId: String): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(messages: List<ChatMessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(message: ChatMessageEntity)

    @Query("select * from chat_messages where status = 'PENDING' order by createdAt asc")
    suspend fun pendingOutbox(): List<ChatMessageEntity>

    @Query("select * from chat_messages where clientMessageId = :cmid limit 1")
    suspend fun findByClientMessageId(cmid: String): ChatMessageEntity?

    @Query("delete from chat_messages")
    suspend fun clear()
}
