package ru.itis.android.auth.presentation.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.itis.android.auth.presentation.AuthEffect
import ru.itis.android.auth.presentation.AuthViewModel

// Цвета для клиента
private val ColorTextPrimary = Color(0xFF101828)
private val ColorTextSecondary = Color(0xFF364153)
private val ColorTextGray = Color(0xFF6A7282)
private val ColorPrimaryBlue = Color(0xFF4A90E2)
private val ColorBlueBg = Color(0xFFEFF6FF)
private val ColorBorder = Color(0xFFD1D5DC)

@Composable
fun ClientProfileScreen(
    viewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onRegistrationSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    var isAgreed by remember { mutableStateOf(false) }

    // Состояния для хранения картинок и диалога
    var showPhotoDialog by remember { mutableStateOf(false) }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Лаунчер для выбора фото из ГАЛЕРЕИ
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        avatarUri = uri
        avatarBitmap = null // Сбрасываем фото с камеры, если выбрали из галереи
    }

    // Лаунчер для селфи с КАМЕРЫ
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        avatarBitmap = bitmap
        avatarUri = null // Сбрасываем галерею, если сделали новое фото
    }

    // Перехват событий из ViewModel
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AuthEffect.NavigateToMain -> onRegistrationSuccess()
            }
        }
    }

    // Диалог выбора источника фото
    if (showPhotoDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text("Фото профиля", fontWeight = FontWeight.Bold) },
            text = { Text("Откуда вы хотите загрузить фотографию?") },
            containerColor = Color.White,
            confirmButton = {
                TextButton(onClick = {
                    showPhotoDialog = false
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) { Text("Из галереи", color = ColorPrimaryBlue) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPhotoDialog = false
                    cameraLauncher.launch(null)
                }) { Text("Сделать селфи", color = ColorPrimaryBlue) }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(bottom = 120.dp) // Отступ для нижней панели
        ) {
            // 1. Шапка
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(97.dp)
                    .border(width = 0.5.dp, color = Color(0xFFE5E7EB))
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                        .size(40.dp)
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = ColorTextSecondary)
                }
                Text(
                    text = "Регистрация клиента",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                // 2. Информационная карточка
                Spacer(modifier = Modifier.height(24.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    color = ColorBlueBg,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(2.dp, ColorPrimaryBlue.copy(alpha = 0.3f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = ColorPrimaryBlue, modifier = Modifier.size(24.dp))
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text("Профиль клиента", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorTextPrimary)
                            Text("Заполните данные для создания заказов", fontSize = 14.sp, color = ColorTextSecondary)
                        }
                    }
                }

                // 3. Фото профиля
                Spacer(modifier = Modifier.height(24.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(112.dp)
                            .clickable { showPhotoDialog = true } // Показываем диалог
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFFE5E7EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (avatarBitmap != null) {
                                Image(
                                    bitmap = avatarBitmap!!.asImageBitmap(),
                                    contentDescription = "Аватар с камеры",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else if (avatarUri != null) {
                                AsyncImage(
                                    model = avatarUri,
                                    contentDescription = "Аватар из галереи",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFF99A1AF), modifier = Modifier.size(36.dp))
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.BottomEnd)
                                .shadow(elevation = 6.dp, shape = CircleShape)
                                .clip(CircleShape)
                                .background(ColorPrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                    Text("Добавить фото профиля", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextSecondary, modifier = Modifier.padding(top = 12.dp))
                    Text("Необязательно, но так мастерам будет проще", fontSize = 12.sp, color = ColorTextGray)
                }

                // 4. Поля ввода
                Spacer(modifier = Modifier.height(24.dp))
                ClientInputField("Имя и фамилия", state.fullName, "Иван Иванов", viewModel::updateFullName)
                ClientInputField("Email", state.email, "ivan@example.com", viewModel::updateEmail)

                // 5. Согласие с правилами
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isAgreed,
                        onCheckedChange = { isAgreed = it },
                        colors = CheckboxDefaults.colors(checkedColor = ColorPrimaryBlue)
                    )
                    Text("Я согласен с правилами сервиса", fontSize = 14.sp, color = ColorTextSecondary)
                }
            }
        }

        // 6. Нижняя панель
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(104.dp),
            border = BorderStroke(width = 1.dp, color = Color(0xFFE5E7EB)),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (state.errorMessage != null) {
                    Text(
                        text = state.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = { viewModel.register() },
                    modifier = Modifier.fillMaxWidth().height(56.dp).shadow(elevation = 6.dp, shape = CircleShape),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimaryBlue, contentColor = Color.White),
                    // Блокируем кнопку, если идет загрузка или не все заполнено
                    enabled = state.fullName.isNotBlank() && isAgreed && !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Создать профиль", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun ClientInputField(label: String, value: String, placeholder: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextSecondary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            placeholder = { Text(placeholder, color = Color.Black.copy(0.5f), fontSize = 16.sp) },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = ColorBorder, focusedBorderColor = Color(0xFF4A90E2))
        )
    }
}