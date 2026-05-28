package ru.itis.android.main.presentation.ui

import ru.itis.android.presentation.theme.AppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.model.Service

@Composable
fun ServiceDetailScreen(
    service: Service,
    isMaster: Boolean = false,
    onBack: () -> Unit,
    onLeave: () -> Unit = {},
    onCreateOrder: () -> Unit = {},
    onMasterClick: () -> Unit = {}
) {
    DisposableEffect(Unit) {
        onDispose { onLeave() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            DetailTopBar(onBack = onBack)

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(AppColors.LightBlueBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Build,
                            contentDescription = null,
                            tint = AppColors.PrimaryBlueDark,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    val categoryName = service.categoryName
                    if (!categoryName.isNullOrBlank()) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = AppColors.LightBlueBg
                        ) {
                            Text(
                                text = categoryName,
                                fontSize = 13.sp,
                                color = AppColors.PrimaryBlueDark,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = service.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    lineHeight = 30.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AppColors.SoftBlueBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Стоимость",
                            fontSize = 14.sp,
                            color = AppColors.TextGray,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${service.price} ₽",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.PrimaryBlueDark
                        )
                    }
                }

                val description = service.description
                if (!description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionLabel("Описание")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        fontSize = 15.sp,
                        color = AppColors.TextSecondary,
                        lineHeight = 22.sp
                    )
                }

                val masterName = service.masterName
                if (!masterName.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = AppColors.BorderLight)
                    Spacer(modifier = Modifier.height(20.dp))
                    SectionLabel("Мастер")
                    Spacer(modifier = Modifier.height(10.dp))
                    MasterRow(
                        masterName = masterName,
                        onClick = if (service.masterId != null) onMasterClick else null
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (!isMaster) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                    Button(
                        onClick = onCreateOrder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
                    ) {
                        Text(
                            "Создать заказ",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailTopBar(onBack: () -> Unit) {
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
            Icon(
                Icons.Default.ChevronLeft,
                contentDescription = "Назад",
                tint = AppColors.TextSecondary
            )
        }
        Text(
            text = "Детали услуги",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = AppColors.TextGray,
        letterSpacing = 0.5.sp
    )
}

@Composable
private fun MasterRow(masterName: String, onClick: (() -> Unit)? = null) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = AppColors.LightBlueBg
    ) {
        Row(
            modifier = Modifier
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AppColors.SoftBlueBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = AppColors.PrimaryBlueDark,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = masterName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.TextPrimary
                )
                Text(
                    text = if (onClick != null) "Нажмите, чтобы открыть профиль" else "Мастер",
                    fontSize = 13.sp,
                    color = AppColors.TextGray
                )
            }
            if (onClick != null) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = AppColors.TextGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
