package ru.itis.android.orders.presentation.ui

import ru.itis.android.presentation.theme.AppColors

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.model.Order
import ru.itis.android.model.OrderStatus
import ru.itis.android.presentation.components.PollEffect

private val TERMINAL_STATUSES = setOf(
    OrderStatus.COMPLETED,
    OrderStatus.CANCELLED,
    OrderStatus.DISPUTED
)

@Composable
fun ClientOrderDetailScreen(
    order: Order,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    canLeaveReview: Boolean = false,
    onLeaveReview: () -> Unit = {},
    onOpenChat: () -> Unit = {}
) {
    PollEffect(
        key = order.id,
        enabled = order.status !in TERMINAL_STATUSES,
        action = { onRefresh() }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.statusBars)
            .verticalScroll(rememberScrollState())
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
                Icon(Icons.Default.ChevronLeft, contentDescription = "Назад", tint = AppColors.TextGray)
            }
            Text(
                text = "Заказ #${order.id}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text("Статус заказа", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            ClientOrderStatusStepper(status = order.status)
            Spacer(modifier = Modifier.height(24.dp))
        }

        HorizontalDivider(color = AppColors.BorderLight, thickness = 6.dp)
        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text("Детали заказа", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))

            if (!order.serviceTitle.isNullOrBlank()) {
                ClientDetailRow("Услуга", order.serviceTitle!!)
                Spacer(modifier = Modifier.height(12.dp))
            }

            val dateTime = buildString {
                if (order.scheduledDate.isNotBlank()) append(order.scheduledDate)
                if (order.scheduledTime.isNotBlank()) append(", ${order.scheduledTime}")
            }.ifBlank { "—" }

            ClientDetailRow("Дата и время", dateTime)
            Spacer(modifier = Modifier.height(12.dp))
            ClientDetailRow("Адрес", order.address.ifBlank { "—" })
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Стоимость", fontSize = 15.sp, color = AppColors.TextGray, modifier = Modifier.weight(1f))
                Text(
                    text = "${(order.finalPrice ?: order.estimatedPrice).toInt()} ₽",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.PrimaryBlueDark
                )
            }

            if (order.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = AppColors.BorderLight)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Описание",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    order.description,
                    fontSize = 15.sp,
                    color = AppColors.TextPrimary,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        HorizontalDivider(color = AppColors.BorderLight, thickness = 6.dp)
        Spacer(modifier = Modifier.height(20.dp))

        // Master info placeholder
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text("Мастер", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(AppColors.LightBlueBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = AppColors.PrimaryBlueDark,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text("Мастер назначен", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
                    Text("Свяжется с вами для уточнения деталей", fontSize = 13.sp, color = AppColors.TextGray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.OutlinedButton(
                onClick = onOpenChat,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.PrimaryBlue)
            ) {
                Text(
                    "Открыть чат",
                    color = AppColors.PrimaryBlueDark,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }

            if (canLeaveReview) {
                Spacer(modifier = Modifier.height(24.dp))
                androidx.compose.material3.Button(
                    onClick = onLeaveReview,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = AppColors.PrimaryBlue
                    )
                ) {
                    Text(
                        "Оставить отзыв",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ClientDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Text(label, fontSize = 15.sp, color = AppColors.TextGray, modifier = Modifier.weight(1f))
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
    }
}

@Composable
private fun ClientOrderStatusStepper(status: OrderStatus) {
    val steps = listOf(
        OrderStatus.PENDING to "Новый",
        OrderStatus.ACCEPTED to "Принят",
        OrderStatus.IN_PROGRESS to "В работе",
        OrderStatus.COMPLETED to "Завершён"
    )
    val currentIndex = when (status) {
        OrderStatus.CANCELLED, OrderStatus.DISPUTED -> -1
        else -> steps.indexOfFirst { it.first == status }.coerceAtLeast(0)
    }

    if (status == OrderStatus.CANCELLED || status == OrderStatus.DISPUTED) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFFF1F1)
        ) {
            Text(
                text = if (status == OrderStatus.CANCELLED) "Заказ отменён" else "Спорная ситуация",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFDC3545),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
            )
        }
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, (_, label) ->
            val isDone = index < currentIndex
            val isActive = index == currentIndex

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isDone || isActive -> AppColors.PrimaryBlue
                                else -> Color(0xFFE5E7EB)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    } else if (isActive) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = if (isDone || isActive) AppColors.PrimaryBlueDark else AppColors.TextGray,
                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(2.dp)
                        .padding(bottom = 20.dp)
                        .background(if (isDone) AppColors.PrimaryBlue else Color(0xFFE5E7EB))
                )
            }
        }
    }
}
