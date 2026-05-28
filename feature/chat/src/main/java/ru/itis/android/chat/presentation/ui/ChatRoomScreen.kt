package ru.itis.android.chat.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.chat.presentation.ChatRoomViewModel
import ru.itis.android.model.ChatMessage
import ru.itis.android.model.MessageStatus
import ru.itis.android.presentation.components.ErrorBanner
import ru.itis.android.presentation.theme.AppColors

@Composable
fun ChatRoomScreen(
    viewModel: ChatRoomViewModel,
    roomId: String,
    peerName: String,
    orderId: Long,
    onBack: () -> Unit,
    onOrderClick: (Long) -> Unit = {}
) {
    LaunchedEffect(roomId) { viewModel.bindRoom(roomId, peerName) }
    DisposableEffect(Unit) { onDispose { viewModel.unbind() } }

    val ui by viewModel.ui.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.statusBars)
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
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
                text = ui.peerName.ifBlank { peerName.ifBlank { "Чат" } },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        OrderLinkBanner(orderId = orderId, onClick = { onOrderClick(orderId) })

        if (ui.errorMessage != null) {
            ErrorBanner(
                message = ui.errorMessage!!,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                onRetry = viewModel::clearError
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(messages, key = { it.clientMessageId }) { msg ->
                MessageBubble(msg)
            }
        }

        Composer(
            text = ui.draft,
            onTextChange = viewModel::updateDraft,
            onSend = viewModel::send
        )
        Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
    }
}

@Composable
private fun OrderLinkBanner(orderId: Long, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        color = AppColors.LightBlueBg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Receipt,
                contentDescription = null,
                tint = AppColors.PrimaryBlueDark,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Заказ #$orderId",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.PrimaryBlueDark,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Открыть",
                fontSize = 13.sp,
                color = AppColors.PrimaryBlue,
                fontWeight = FontWeight.Medium
            )
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = AppColors.PrimaryBlue,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isOwn = message.senderId == "self" || message.status == MessageStatus.PENDING
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isOwn) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isOwn) 14.dp else 4.dp,
                bottomEnd = if (isOwn) 4.dp else 14.dp
            ),
            color = if (isOwn) AppColors.PrimaryBlue else AppColors.LightBlueBg
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(
                    text = message.content,
                    color = if (isOwn) Color.White else AppColors.TextPrimary,
                    fontSize = 14.sp
                )
                if (message.status == MessageStatus.PENDING) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Отправляется…",
                        color = if (isOwn) Color.White.copy(alpha = 0.7f) else AppColors.TextGray,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun Composer(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 8.dp) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Сообщение", color = AppColors.TextGray) },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.PrimaryBlue,
                    unfocusedBorderColor = AppColors.BorderLight
                ),
                maxLines = 4
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSend,
                enabled = text.isNotBlank(),
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = if (text.isNotBlank()) AppColors.PrimaryBlue else AppColors.BorderLight,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            ) {
                Icon(Icons.Default.Send, contentDescription = "Отправить", tint = Color.White)
            }
        }
    }
}
