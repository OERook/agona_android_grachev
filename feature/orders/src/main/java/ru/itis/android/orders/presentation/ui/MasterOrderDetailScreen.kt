package ru.itis.android.orders.presentation.ui

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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.model.Order
import ru.itis.android.model.OrderStatus

private val DangerRed = Color(0xFFDC3545)
private val DangerRedLight = Color(0xFFFFF1F1)
private val SuccessGreen = Color(0xFF28A745)
private val SuccessGreenLight = Color(0xFFF0FFF4)

@Composable
fun MasterOrderDetailScreen(
    order: Order,
    isUpdating: Boolean,
    onUpdateStatus: (OrderStatus) -> Unit,
    onBack: () -> Unit,
    statusUpdateError: String? = null,
    onDismissStatusError: () -> Unit = {},
    onOpenChat: () -> Unit = {}
) {
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
                    text = "Заказ #${order.id}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Статус:", fontSize = 15.sp, color = AppColors.TextGray)
                    Spacer(modifier = Modifier.size(10.dp))
                    StatusChip(order.status)
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = AppColors.BorderLight)
                Spacer(modifier = Modifier.height(20.dp))

                Text("Детали заказа", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
                Spacer(modifier = Modifier.height(14.dp))

                if (!order.serviceTitle.isNullOrBlank()) {
                    DetailRow("Услуга", order.serviceTitle!!)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                DetailRow("Адрес", order.address.ifBlank { "—" })
                Spacer(modifier = Modifier.height(10.dp))

                val dateTime = buildString {
                    if (order.scheduledDate.isNotBlank()) append(order.scheduledDate)
                    if (order.scheduledTime.isNotBlank()) append(", ${order.scheduledTime}")
                }.ifBlank { "—" }
                DetailRow("Дата и время", dateTime)
                Spacer(modifier = Modifier.height(10.dp))

                DetailRow("Стоимость", "${order.estimatedPrice.toInt()} ₽")

                if (order.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = AppColors.BorderLight)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Описание проблемы", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        order.description,
                        fontSize = 15.sp,
                        color = AppColors.TextPrimary,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    onClick = onOpenChat,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.PrimaryBlue)
                ) {
                    Text(
                        "Открыть чат с клиентом",
                        color = AppColors.PrimaryBlueDark,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (order.availableActions.isNotEmpty() || statusUpdateError != null) {
            HorizontalDivider(color = AppColors.BorderLight)
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                if (statusUpdateError != null) {
                    ErrorBanner(message = statusUpdateError, onRetry = onDismissStatusError)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (order.availableActions.isNotEmpty()) {
                    Text(
                        "Изменить статус",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (isUpdating) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color = AppColors.PrimaryBlue,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    } else {
                        ActionButtons(
                            actions = order.availableActions,
                            onClick = onUpdateStatus
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Text(label, fontSize = 14.sp, color = AppColors.TextGray, modifier = Modifier.weight(1f))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
    }
}

private data class ActionMeta(
    val label: String,
    val isDestructive: Boolean,
    val isPrimary: Boolean
)

private fun OrderStatus.actionMeta(): ActionMeta = when (this) {
    OrderStatus.ACCEPTED    -> ActionMeta("Принять",        isDestructive = false, isPrimary = true)
    OrderStatus.IN_PROGRESS -> ActionMeta("Начать работу",  isDestructive = false, isPrimary = true)
    OrderStatus.COMPLETED   -> ActionMeta("Завершить заказ",isDestructive = false, isPrimary = true)
    OrderStatus.CANCELLED   -> ActionMeta("Отменить",       isDestructive = true,  isPrimary = false)
    else                    -> ActionMeta(name,             isDestructive = false, isPrimary = false)
}


@Composable
private fun ActionButtons(actions: List<OrderStatus>, onClick: (OrderStatus) -> Unit) {
    if (actions.size == 1 && actions.first() == OrderStatus.COMPLETED) {
        Button(
            onClick = { onClick(OrderStatus.COMPLETED) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
        ) {
            Text("Завершить заказ", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        }
        return
    }

    val sorted = actions.sortedBy { if (it.actionMeta().isDestructive) 0 else 1 }
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        sorted.forEach { action ->
            val meta = action.actionMeta()
            if (meta.isDestructive) {
                OutlinedButton(
                    onClick = { onClick(action) },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DangerRed),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed)
                ) {
                    Text(meta.label, fontWeight = FontWeight.SemiBold)
                }
            } else {
                Button(
                    onClick = { onClick(action) },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
                ) {
                    Text(meta.label, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
