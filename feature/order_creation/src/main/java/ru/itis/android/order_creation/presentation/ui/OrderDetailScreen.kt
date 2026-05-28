package ru.itis.android.order_creation.presentation.ui

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import ru.itis.android.model.Service
import ru.itis.android.presentation.components.PollEffect

private val PrimaryBlue = Color(0xFF4A90E2)
private val PrimaryBlueDark = Color(0xFF1E5FA6)
private val LightBlueBg = Color(0xFFEFF6FF)
private val SoftBlueBg = Color(0xFFDBEAFE)
private val TextPrimary = Color(0xFF101828)
private val TextGray = Color(0xFF6A7282)
private val BorderLight = Color(0xFFF3F4F6)
private val StepActive = Color(0xFF4A90E2)
private val StepDone = Color(0xFF4A90E2)
private val StepInactive = Color(0xFFE5E7EB)

private val TERMINAL_STATUSES = setOf(
    OrderStatus.COMPLETED,
    OrderStatus.CANCELLED,
    OrderStatus.DISPUTED
)

@Composable
fun OrderDetailScreen(
    order: Order,
    service: Service?,
    onRefresh: () -> Unit = {},
    onBack: () -> Unit
) {
    PollEffect(
        key = order.id,
        enabled = order.status !in TERMINAL_STATUSES,
        action = { onRefresh() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Назад", tint = TextGray)
                }
                Text(
                    text = "Заказ #${order.id}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Text("Статус заказа", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(16.dp))
                OrderStatusStepper(status = order.status)

                Spacer(modifier = Modifier.height(24.dp))
            }

            HorizontalDivider(color = BorderLight, thickness = 6.dp)
            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Text("Детали работы", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(16.dp))

                DetailRow("Услуга", service?.title ?: "—")
                Spacer(modifier = Modifier.height(12.dp))
                DetailRow(
                    "Дата и время",
                    if (order.scheduledDate.isNotBlank() && order.scheduledTime.isNotBlank())
                        "${order.scheduledDate}, ${order.scheduledTime}"
                    else order.scheduledDate.ifBlank { "—" }
                )
                Spacer(modifier = Modifier.height(12.dp))
                DetailRow("Адрес", order.address.ifBlank { "—" })
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Цена", fontSize = 15.sp, color = TextGray, modifier = Modifier.weight(1f))
                    Text(
                        text = "${(order.finalPrice ?: order.estimatedPrice).toInt()} ₽",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryBlueDark
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            HorizontalDivider(color = BorderLight, thickness = 6.dp)
            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                val masterName = service?.masterName
                if (!masterName.isNullOrBlank()) {
                    Text("Назначенный мастер", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(SoftBlueBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlueDark, modifier = Modifier.size(30.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(masterName, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            service?.categoryName?.let { cat ->
                                Text(cat, fontSize = 13.sp, color = TextGray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { /* TODO: open chat */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text("Связаться с мастером", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Text(label, fontSize = 15.sp, color = TextGray, modifier = Modifier.weight(1f))
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}

@Composable
private fun OrderStatusStepper(status: OrderStatus) {
    val steps = listOf(
        OrderStatus.PENDING to "Новый",
        OrderStatus.ACCEPTED to "Принят",
        OrderStatus.IN_PROGRESS to "В работе",
        OrderStatus.COMPLETED to "Завершен"
    )
    val currentIndex = steps.indexOfFirst { it.first == status }.coerceAtLeast(0)

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
                                isDone -> StepDone
                                isActive -> StepActive
                                else -> StepInactive
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    } else if (isActive) {
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color.White))
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = if (isDone || isActive) PrimaryBlueDark else TextGray,
                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(2.dp)
                        .padding(bottom = 20.dp)
                        .background(if (isDone) StepDone else StepInactive)
                )
            }
        }
    }
}
