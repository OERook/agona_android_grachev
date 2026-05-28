package ru.itis.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.itis.android.database.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)

    @Query("select * from users limit 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("select * from users limit 1")
    fun observeCurrentUser(): Flow<UserEntity?>

    @Query("delete from users")
    suspend fun clear()
}