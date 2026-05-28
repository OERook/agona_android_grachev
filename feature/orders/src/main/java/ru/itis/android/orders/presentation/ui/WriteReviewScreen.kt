package ru.itis.android.orders.presentation.ui

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import ru.itis.android.orders.presentation.PendingAttachment
import ru.itis.android.orders.presentation.WriteReviewViewModel
import ru.itis.android.presentation.components.ErrorBanner
import ru.itis.android.presentation.theme.AppColors
import java.io.File

@Composable
fun WriteReviewScreen(
    viewModel: WriteReviewViewModel,
    orderId: Long,
    onBack: () -> Unit,
    onSubmitted: () -> Unit
) {
    LaunchedEffect(orderId) { viewModel.init(orderId) }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.submitted) {
        if (state.submitted) onSubmitted()
    }

    val context = LocalContext.current

    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris -> viewModel.addAttachments(uris) }

    val filePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris -> viewModel.addAttachments(uris) }

    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { ok ->
        val uri = pendingCameraUri
        if (ok && uri != null) viewModel.addAttachments(listOf(uri))
        pendingCameraUri = null
    }

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
                text = "Оставить отзыв",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Оценка", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextSecondary)
            Spacer(modifier = Modifier.height(10.dp))
            RatingPicker(rating = state.rating, onChange = viewModel::setRating)

            Spacer(modifier = Modifier.height(24.dp))
            Text("Комментарий", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextSecondary)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = state.comment,
                onValueChange = viewModel::updateComment,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = { Text("Расскажите о работе мастера", color = AppColors.TextGray) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.PrimaryBlue,
                    unfocusedBorderColor = AppColors.BorderLight
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Фото и файлы",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))
            AttachmentSourceRow(
                onPickPhoto = {
                    photoPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onPickFile = { filePicker.launch("*/*") },
                onTakePhoto = {
                    val uri = createImageUri(context)
                    pendingCameraUri = uri
                    cameraLauncher.launch(uri)
                }
            )

            if (state.attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                AttachmentList(
                    attachments = state.attachments,
                    onRemove = viewModel::removeAttachment
                )
            }

            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(14.dp))
                ErrorBanner(message = state.errorMessage!!)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 8.dp) {
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                Button(
                    onClick = viewModel::submit,
                    enabled = !state.isSubmitting && state.rating in 1..5,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
                ) {
                    if (state.isSubmitting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(
                            "Отправить отзыв",
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
private fun RatingPicker(rating: Int, onChange: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        for (i in 1..5) {
            val filled = i <= rating
            IconButton(onClick = { onChange(i) }, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = if (filled) Icons.Default.Star else Icons.Outlined.StarOutline,
                    contentDescription = "Оценка $i",
                    tint = if (filled) Color(0xFFFFB300) else AppColors.TextGray,
                    modifier = Modifier.size(34.dp)
                )
            }
        }
    }
}

@Composable
private fun AttachmentSourceRow(
    onPickPhoto: () -> Unit,
    onPickFile: () -> Unit,
    onTakePhoto: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        SourceButton(Icons.Default.PhotoLibrary, "Фото", onPickPhoto, Modifier.weight(1f))
        SourceButton(Icons.Default.CameraAlt, "Снять", onTakePhoto, Modifier.weight(1f))
        SourceButton(Icons.Default.AttachFile, "Файл", onPickFile, Modifier.weight(1f))
    }
}

@Composable
private fun SourceButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(12.dp),
        color = AppColors.LightBlueBg,
        border = BorderStroke(1.dp, AppColors.PrimaryBlue.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = AppColors.PrimaryBlueDark, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontSize = 13.sp, color = AppColors.PrimaryBlueDark, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun AttachmentList(
    attachments: List<PendingAttachment>,
    onRemove: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(attachments, key = { it.uri }) { att ->
            AttachmentThumb(att = att, onRemove = { onRemove(att.uri) })
        }
    }
}

@Composable
private fun AttachmentThumb(att: PendingAttachment, onRemove: () -> Unit) {
    val isImage = att.mimeType.startsWith("image/")
    Box(modifier = Modifier.size(96.dp)) {
        if (isImage) {
            AsyncImage(
                model = att.uri,
                contentDescription = att.displayName,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppColors.LightBlueBg)
            )
        } else {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                color = AppColors.LightBlueBg
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.AttachFile,
                        contentDescription = null,
                        tint = AppColors.PrimaryBlueDark,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = att.displayName,
                        fontSize = 10.sp,
                        color = AppColors.TextSecondary,
                        maxLines = 2,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Удалить",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private fun createImageUri(context: Context): Uri {
    val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        ?: context.cacheDir
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, "review_${System.currentTimeMillis()}.jpg")
    file.createNewFile()
    val authority = context.packageName + ".fileprovider"
    return FileProvider.getUriForFile(context, authority, file)
}
