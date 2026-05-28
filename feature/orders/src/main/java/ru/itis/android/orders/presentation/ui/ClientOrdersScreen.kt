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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.model.Order

@Composable
fun ClientOrdersScreen(
    orders: List<Order>,
    onOrderClick: (Order) -> Unit,
    onBack: () -> Unit,
    title: String = "Мои заказы",
    emptyTitle: String = "У вас пока нет заказов",
    emptySubtitle: String = "Создайте заказ, выбрав услугу",
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onRetry: () -> Unit = {}
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
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (errorMessage != null) {
            ErrorBanner(
                message = errorMessage,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                onRetry = onRetry
            )
        }

        when {
            isLoading && orders.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.CircularProgressIndicator(color = AppColors.PrimaryBlue)
                }
            }
            orders.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(emptyTitle, color = AppColors.TextGray, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(emptySubtitle, color = AppColors.TextGray, fontSize = 13.sp)
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 16.dp, vertical = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(orders) { order ->
                        ClientOrderCard(order = order, onClick = { onOrderClick(order) })
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun ClientOrderCard(order: Order, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.BorderLight)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Заказ #${order.id}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusChip(order.status)
                }

                if (order.address.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        order.address,
                        fontSize = 13.sp,
                        color = AppColors.TextGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (order.scheduledDate.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    val dateTime = buildString {
                        append(order.scheduledDate)
                        if (order.scheduledTime.isNotBlank()) append(", ${order.scheduledTime}")
                    }
                    Text(dateTime, fontSize = 12.sp, color = AppColors.TextGray)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${order.estimatedPrice.toInt()} ₽",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.PrimaryBlueDark
                )
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
