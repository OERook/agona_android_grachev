package ru.itis.android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val phone: String,
    val email: String,
    val fullName: String,
    val role: String,
    val avatarUrl: String? = null,
    val about: String? = null,
    val experienceYears: Int? = null
)