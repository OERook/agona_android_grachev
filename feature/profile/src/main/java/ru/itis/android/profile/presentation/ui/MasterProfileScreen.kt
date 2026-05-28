package ru.itis.android.profile.presentation.ui

import ru.itis.android.presentation.components.ErrorBanner

import ru.itis.android.presentation.theme.AppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ru.itis.android.model.MasterProfile
import ru.itis.android.model.MasterService
import ru.itis.android.model.Review

@Composable
fun MasterProfileScreen(
    profile: MasterProfile?,
    isLoading: Boolean,
    onBack: () -> Unit,
    errorMessage: String? = null,
    onRetry: () -> Unit = {},
    reviews: List<Review> = emptyList(),
    reviewsLoading: Boolean = false,
    onWriteClick: ((peerUserId: String) -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Назад", tint = AppColors.TextSecondary)
            }
            Text(
                text = "Профиль мастера",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColors.PrimaryBlue)
                }
            }
            profile == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ErrorBanner(
                        message = errorMessage ?: "Не удалось загрузить профиль",
                        onRetry = onRetry
                    )
                }
            }
            else -> {
                MasterProfileContent(
                    profile = profile,
                    reviews = reviews,
                    reviewsLoading = reviewsLoading,
                    onWriteClick = onWriteClick?.let { cb -> { cb(profile.userId) } }
                )
            }
        }
    }
}

@Composable
private fun MasterProfileContent(
    profile: MasterProfile,
    reviews: List<Review>,
    reviewsLoading: Boolean,
    onWriteClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AppColors.GreenBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Build,
                    contentDescription = null,
                    tint = AppColors.GreenAccent,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.fullName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AppColors.GreenBg
                ) {
                    Text(
                        text = "Мастер",
                        color = AppColors.GreenAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            if (profile.isVerified) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Верифицирован",
                    tint = AppColors.GreenInfo,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        if (onWriteClick != null) {
            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.Button(
                onClick = onWriteClick,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = AppColors.PrimaryBlue
                )
            ) {
                Text(
                    "Написать мастеру",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = AppColors.LightBlueBg
        ) {
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(value = String.format("%.1f", profile.rating), label = "Рейтинг", icon = "⭐")
                StatDivider()
                StatItem(value = "${profile.reviewsCount}", label = "Отзывов", icon = null)
                StatDivider()
                StatItem(value = "${profile.completedJobs}", label = "Выполнено", icon = null)
                StatDivider()
                StatItem(
                    value = experienceShort(profile.experienceYears),
                    label = "Опыт",
                    icon = null
                )
            }
        }

        if (profile.about.isNotBlank()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "О мастере",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = AppColors.SurfaceMuted
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Work,
                            contentDescription = null,
                            tint = AppColors.TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = experienceText(profile.experienceYears),
                            color = AppColors.TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = profile.about,
                        fontSize = 14.sp,
                        color = AppColors.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = AppColors.BorderLight)
        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Услуги мастера",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Text(
                "${profile.services.size}",
                fontSize = 14.sp,
                color = AppColors.TextGray
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (profile.services.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = AppColors.SurfaceMuted
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Услуги не добавлены",
                        color = AppColors.TextGray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                profile.services.forEach { service ->
                    MasterServiceCard(service = service)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = AppColors.BorderLight)
        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Отзывы",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Text(
                "${reviews.size}",
                fontSize = 14.sp,
                color = AppColors.TextGray
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        when {
            reviewsLoading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.PrimaryBlue)
                }
            }
            reviews.isEmpty() -> {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = AppColors.SurfaceMuted
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Отзывов пока нет", color = AppColors.TextGray, fontSize = 14.sp)
                    }
                }
            }
            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    reviews.forEach { ReviewCard(review = it) }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.BorderLight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = review.clientName.ifBlank { "Клиент" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                ReviewStars(rating = review.rating)
            }
            if (!review.comment.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = review.comment!!,
                    fontSize = 13.sp,
                    color = AppColors.TextSecondary,
                    lineHeight = 18.sp
                )
            }
            if (review.attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(review.attachments) { att ->
                        val isImage = (att.mimeType ?: "").startsWith("image/")
                        if (isImage) {
                            AsyncImage(
                                model = att.url,
                                contentDescription = att.originalFilename,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AppColors.LightBlueBg)
                            )
                        } else {
                            Surface(
                                modifier = Modifier.size(80.dp),
                                shape = RoundedCornerShape(10.dp),
                                color = AppColors.LightBlueBg
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = att.originalFilename ?: "файл",
                                        fontSize = 10.sp,
                                        color = AppColors.PrimaryBlueDark,
                                        maxLines = 3,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewStars(rating: Int) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = null,
                tint = if (i <= rating) Color(0xFFFFB300) else AppColors.TextGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun MasterServiceCard(service: MasterService) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.BorderLight)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.TextPrimary
                )
                val desc = service.description
                if (!desc.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = desc,
                        fontSize = 13.sp,
                        color = AppColors.TextGray,
                        lineHeight = 18.sp,
                        maxLines = 2
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End) {
                val priceText = if (service.priceTo != null) {
                    "от ${service.priceFrom.toInt()} ₽"
                } else {
                    "${service.priceFrom.toInt()} ₽"
                }
                Text(
                    text = priceText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.PrimaryBlueDark
                )
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String, icon: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (icon != null) "$icon $value" else value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 11.sp, color = AppColors.TextGray)
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .height(36.dp)
            .width(1.dp)
            .background(AppColors.BorderLight)
    )
}

private fun experienceShort(years: Int): String {
    if (years <= 0) return "—"
    return "${years}л"
}

private fun experienceText(years: Int): String {
    if (years <= 0) return "Опыт не указан"
    val mod10 = years % 10
    val mod100 = years % 100
    val suffix = when {
        mod100 in 11..14 -> "лет"
        mod10 == 1 -> "год"
        mod10 in 2..4 -> "года"
        else -> "лет"
    }
    return "Опыт работы: $years $suffix"
}
