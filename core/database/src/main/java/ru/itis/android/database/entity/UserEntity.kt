package ru.itis.android.database.entity

import android.provider.ContactsContract
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val phone: String,
    val email: String,
    val fullName: String,
    val role: String,
    val about: String? = null,
    val experienceYears: Int? = null
)
