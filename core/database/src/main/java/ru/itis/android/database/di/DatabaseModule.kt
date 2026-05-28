package ru.itis.android.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.itis.android.database.ReparoDatabase
import ru.itis.android.database.dao.ChatMessageDao
import ru.itis.android.database.dao.ChatRoomDao
import ru.itis.android.database.dao.UserDao
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): ReparoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ReparoDatabase::class.java,
            "reparo_database"
        )
            .fallbackToDestructiveMigrationFrom(1, 2)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: ReparoDatabase): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideChatRoomDao(database: ReparoDatabase): ChatRoomDao = database.chatRoomDao()

    @Provides
    @Singleton
    fun provideChatMessageDao(database: ReparoDatabase): ChatMessageDao = database.chatMessageDao()
}