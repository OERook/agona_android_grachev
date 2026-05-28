package ru.itis.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.itis.android.database.dao.ChatMessageDao
import ru.itis.android.database.dao.ChatRoomDao
import ru.itis.android.database.dao.UserDao
import ru.itis.android.database.entity.ChatMessageEntity
import ru.itis.android.database.entity.ChatRoomEntity
import ru.itis.android.database.entity.UserEntity

@Database(
    entities = [UserEntity::class, ChatRoomEntity::class, ChatMessageEntity::class],
    version = 3,
    exportSchema = false
)
abstract class ReparoDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatRoomDao(): ChatRoomDao
    abstract fun chatMessageDao(): ChatMessageDao
}
