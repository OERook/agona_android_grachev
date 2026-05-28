package ru.itis.android.profile.presentation.ui

import ru.itis.android.presentation.components.ErrorBanner

import ru.itis.android.presentation.theme.AppColors

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.profile.R
import ru.itis.android.model.Review
import ru.itis.android.model.Service
import ru.itis.android.model.UserRole
import ru.itis.android.profile.presentation.ProfileScreenState

@Composable
fun ProfileScreen(
    state: ProfileScreenState,
    activeMasterOrdersCount: Int,
    completedMasterOrdersCount: Int,
    activeClientOrdersCount: Int,
    completedClientOrdersCount: Int,
    onLogout: () -> Unit,
    onServiceClick: (Service) -> Unit = {},
    onActiveOrdersClick: () -> Unit = {},
    onMyOrdersClick: () -> Unit = {},
    onCompletedMasterOrdersClick: () -> Unit = {},
    onCompletedClientOrdersClick: () -> Unit = {},
    onRetryLoadProfile: () -> Unit = {},
    onEditService: (Service) -> Unit = {},
    onDeleteService: (Service) -> Unit = {}
) {
    val scroll = rememberScrollState()
    val isMaster = state.role == UserRole.MASTER

    if (state.isLoading && state.fullName.isBlank()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .windowInsetsPadding(WindowInsets.statusBars),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.CircularProgressIndicator(color = AppColors.PrimaryBlue)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scroll)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        ProfileHeader(fullName = state.fullName, isMaster = isMaster)

        if (state.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            ErrorBanner(message = state.errorMessage, onRetry = onRetryLoadProfile)
        }

        Spacer(modifier = Modifier.height(20.dp))

        ContactCard(phone = state.phone, email = state.email)

        if (!isMaster) {
            Spacer(modifier = Modifier.height(16.dp))
            OrdersBanner(
                title = "Активные заказы",
                emptyText = "Нет активных заказов",
                count = activeClientOrdersCount,
                onClick = onMyOrdersClick,
                accent = BannerAccent.Blue
            )
            Spacer(modifier = Modifier.height(12.dp))
            OrdersBanner(
                title = "Завершённые заказы",
                emptyText = "История пуста",
                count = completedClientOrdersCount,
                onClick = onCompletedClientOrdersClick,
                accent = BannerAccent.Green
            )
        }

        if (isMaster) {
            Spacer(modifier = Modifier.height(16.dp))
            MasterAboutCard(about = state.about, experienceYears = state.experienceYears)

            Spacer(modifier = Modifier.height(16.dp))
            OrdersBanner(
                title = "Активные заказы",
                emptyText = "Нет активных заказов",
                count = activeMasterOrdersCount,
                onClick = onActiveOrdersClick,
                accent = BannerAccent.Blue
            )
            Spacer(modifier = Modifier.height(12.dp))
            OrdersBanner(
                title = "Завершённые заказы",
                emptyText = "История пуста",
                count = completedMasterOrdersCount,
                onClick = onCompletedMasterOrdersClick,
                accent = BannerAccent.Green
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Мои услуги",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            MyServicesList(
                services = state.myServices,
                onServiceClick = onServiceClick,
                onEditService = onEditService,
                onDeleteService = onDeleteService
            )

            Spacer(modifier = Modifier.height(24.dp))
            ReviewsSection(
                reviews = state.masterReviews,
                isLoading = state.masterReviewsLoading
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, AppColors.Border)
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = AppColors.TextSecondary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Выйти", color = AppColors.TextSecondary, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun ProfileHeader(fullName: String, isMaster: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(if (isMaster) AppColors.GreenBg else AppColors.LightBlueBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (isMaster) Icons.Default.Build else Icons.Default.Person,
                contentDescription = null,
                tint = if (isMaster) AppColors.GreenAccent else AppColors.PrimaryBlueDark,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = fullName.ifBlank { "Без имени" },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isMaster) AppColors.GreenBg else AppColors.LightBlueBg
            ) {
                Text(
                    text = if (isMaster) "Мастер" else "Клиент",
                    color = if (isMaster) AppColors.GreenAccent else AppColors.PrimaryBlueDark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
        if (isMaster) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = AppColors.GreenInfo,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun ContactCard(phone: String, email: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, AppColors.BorderLight)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Контактная информация",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            ContactRow(Icons.Default.Phone, phone.ifBlank { "—" })
            Spacer(modifier = Modifier.height(8.dp))
            ContactRow(Icons.Default.Email, email.ifBlank { "—" })
        }
    }
}

@Composable
private fun ContactRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = AppColors.TextGray, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = AppColors.TextPrimary, fontSize = 14.sp)
    }
}

@Composable
private fun MasterAboutCard(about: String?, experienceYears: Int?) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppColors.GreenBg,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, AppColors.GreenInfo.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = AppColors.GreenAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Профиль мастера",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Work, contentDescription = null, tint = AppColors.TextSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = experienceText(experienceYears),
                    color = AppColors.TextSecondary,
                    fontSize = 14.sp
                )
            }

            if (!about.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = about,
                    color = AppColors.TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

private fun experienceText(years: Int?): String {
    if (years == null || years <= 0) return "Опыт работы не указан"
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

private enum class BannerAccent { Blue, Green }

@Composable
private fun OrdersBanner(
    title: String,
    emptyText: String,
    count: Int,
    onClick: () -> Unit,
    accent: BannerAccent
) {
    val bg = if (accent == BannerAccent.Blue) AppColors.LightBlueBg else AppColors.GreenBg
    val accentColor = if (accent == BannerAccent.Blue) AppColors.PrimaryBlue else AppColors.GreenAccent
    val accentDark = if (accent == BannerAccent.Blue) AppColors.PrimaryBlueDark else AppColors.GreenAccent
    val borderColor = if (accent == BannerAccent.Blue)
        AppColors.PrimaryBlue.copy(alpha = 0.3f) else AppColors.GreenInfo.copy(alpha = 0.4f)
    val icon = if (accent == BannerAccent.Blue) Icons.Default.Assignment else Icons.Default.CheckCircle

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = bg,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = accentDark,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.TextPrimary
                )
                Text(
                    if (count == 0) emptyText
                    else "$count ${orderWord(count)}",
                    fontSize = 13.sp,
                    color = AppColors.TextGray
                )
            }
            if (count > 0) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = accentColor
                ) {
                    Text(
                        "$count",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = AppColors.TextGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private fun orderWord(count: Int): String {
    val mod10 = count % 10
    val mod100 = count % 100
    return when {
        mod100 in 11..14 -> "заказов"
        mod10 == 1 -> "заказ"
        mod10 in 2..4 -> "заказа"
        else -> "заказов"
    }
}

@Composable
private fun MyServicesList(
    services: List<Service>,
    onServiceClick: (Service) -> Unit,
    onEditService: (Service) -> Unit,
    onDeleteService: (Service) -> Unit
) {
    if (services.isEmpty()) {
        EmptyServicesPlaceholder()
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        services.forEach { svc ->
            ServiceCard(
                service = svc,
                onClick = { onServiceClick(svc) },
                onEdit = { onEditService(svc) },
                onDelete = { onDeleteService(svc) }
            )
        }
    }
}

@Composable
private fun EmptyServicesPlaceholder() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = AppColors.SurfaceMuted
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.profile_services_empty),
                color = AppColors.TextGray,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun ServiceCard(
    service: Service,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, AppColors.BorderLight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        service.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.TextPrimary
                    )
                    val categoryName = service.categoryName
                    if (!categoryName.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(categoryName, fontSize = 12.sp, color = AppColors.TextGray)
                    }
                }
                Text(
                    text = "${service.price} ₽",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.PrimaryBlueDark
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ServiceActionButton(
                    icon = Icons.Default.Edit,
                    label = stringResource(R.string.service_action_edit),
                    tint = AppColors.PrimaryBlueDark,
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                )
                ServiceActionButton(
                    icon = Icons.Default.Delete,
                    label = stringResource(R.string.service_action_delete),
                    tint = AppColors.Error,
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    if (showDeleteDialog) {
        DeleteServiceDialog(
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
private fun ServiceActionButton(
    icon: ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, AppColors.BorderLight)
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, color = tint, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun DeleteServiceDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.service_delete_dialog_title)) },
        text = { Text(stringResource(R.string.service_delete_dialog_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.action_confirm), color = AppColors.Error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}

@Composable
private fun ReviewsSection(reviews: List<Review>, isLoading: Boolean) {
    Text(
        text = "Отзывы",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = AppColors.TextPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(color = AppColors.PrimaryBlue)
            }
        }
        reviews.isEmpty() -> {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = AppColors.SurfaceMuted
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Отзывов пока нет", color = AppColors.TextGray, fontSize = 13.sp)
                }
            }
        }
        else -> {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                reviews.forEach { ProfileReviewCard(it) }
            }
        }
    }
}

@Composable
private fun ProfileReviewCard(review: Review) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(1.dp, AppColors.BorderLight)
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
                Row {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= review.rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null,
                            tint = if (i <= review.rating) Color(0xFFFFB300) else AppColors.TextGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            val comment = review.comment
            if (!comment.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = comment,
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
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AppColors.LightBlueBg)
                            )
                        } else {
                            Surface(
                                modifier = Modifier.size(72.dp),
                                shape = RoundedCornerShape(10.dp),
                                color = AppColors.LightBlueBg
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(6.dp),
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
