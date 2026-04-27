package ru.itis.android.model

data class MasterProfile(
    val id: String,
    val userId: String,
    val fullName: String,
    val avatarUrl: String?,
    val about: String,
    val experienceYears: Int,
    val rating: Double,
    val reviewsCount: Int,
    val completedJobs: Int,
    val isVerified: Boolean,
    val services: List<MasterService>
)
