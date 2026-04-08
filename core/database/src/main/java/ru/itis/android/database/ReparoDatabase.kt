package ru.itis.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.itis.android.database.dao.UserDao
import ru.itis.android.database.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class ReparoDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}