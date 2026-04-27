package ru.itis.android.data.mapper


import ru.itis.android.model.MasterProfile
import ru.itis.android.model.MasterService
import ru.itis.android.model.ServiceCategory
import ru.itis.android.network.models.MasterProfileDto
import ru.itis.android.network.models.MasterServiceDto
import ru.itis.android.network.models.ServiceCategoryDto

fun ServiceCategoryDto.toDomain(): ServiceCategory {
    return ServiceCategory(
        id = this.id,
        name = this.name,
        description = this.description,
        iconUrl = this.iconUrl
    )
}

fun MasterServiceDto.toDomain(): MasterService {
    return MasterService(
        id = this.id,
        categoryId = this.categoryId,
        title = this.title,
        priceFrom = this.priceFrom,
        priceTo = this.priceTo,
        description = this.description
    )
}

fun MasterProfileDto.toDomain(): MasterProfile {
    return MasterProfile(
        id = this.id,
        userId = this.userId,
        fullName = this.fullName,
        avatarUrl = this.avatarUrl,
        about = this.about,
        experienceYears = this.experienceYears,
        rating = this.rating,
        reviewsCount = this.reviewsCount,
        completedJobs = this.completedJobs,
        isVerified = this.isVerified,
        services = this.services.map { it.toDomain() } // Мапим вложенный список!
    )
}