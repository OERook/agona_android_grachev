package ru.itis.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.itis.android.database.entity.ChatRoomEntity

@Dao
interface ChatRoomDao {

    @Query("select * from chat_rooms order by lastMessageAt desc")
    fun observeAll(): Flow<List<ChatRoomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(rooms: List<ChatRoomEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(room: ChatRoomEntity)

    @Query("delete from chat_rooms")
    suspend fun clear()
}
