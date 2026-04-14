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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.itis.android.auth.presentation.AuthViewModel
import ru.itis.android.reparo.feature.auth.R

private val ColorTextPrimary = Color(0xFF101828)
private val ColorTextSecondary = Color(0xFF364153)
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

    var showPhotoDialog by remember { mutableStateOf(false) }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        avatarUri = uri
        avatarBitmap = null
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        avatarBitmap = bitmap
        avatarUri = null
    }

    if (showPhotoDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text(stringResource(R.string.photo_dialog_title), fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.photo_dialog_text)) },
            containerColor = Color.White,
            confirmButton = {
                TextButton(onClick = {
                    showPhotoDialog = false
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) { Text(stringResource(R.string.photo_dialog_gallery), color = ColorPrimaryBlue) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPhotoDialog = false
                    cameraLauncher.launch(null)
                }) { Text(stringResource(R.string.photo_dialog_camera), color = ColorPrimaryBlue) }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(bottom = 120.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(97.dp)
                    .border(width = 0.5.dp, color = Color(0xFFE5E7EB))
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp).size(40.dp)
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = ColorTextSecondary)
                }
                Text(
                    text = stringResource(R.string.profile_reg_client),
                    fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ColorTextPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
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
                            Text(stringResource(R.string.profile_client_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorTextPrimary)
                            Text(stringResource(R.string.profile_client_subtitle), fontSize = 14.sp, color = ColorTextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.size(112.dp).clickable { showPhotoDialog = true }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().clip(CircleShape).background(Color(0xFFE5E7EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (avatarBitmap != null) {
                                Image(bitmap = avatarBitmap!!.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            } else if (avatarUri != null) {
                                AsyncImage(model = avatarUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            } else {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFF99A1AF), modifier = Modifier.size(36.dp))
                            }
                        }
                        Box(
                            modifier = Modifier.size(40.dp).align(Alignment.BottomEnd).shadow(elevation = 6.dp, shape = CircleShape).clip(CircleShape).background(ColorPrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                    Text(stringResource(R.string.profile_add_photo), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextSecondary, modifier = Modifier.padding(top = 12.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))
                ClientInputField(stringResource(R.string.profile_name_label), state.fullName, stringResource(R.string.profile_name_placeholder), viewModel::updateFullName)
                ClientInputField(stringResource(R.string.profile_email_label), state.email, stringResource(R.string.profile_email_placeholder), viewModel::updateEmail)
                ClientInputField(stringResource(R.string.profile_city_label), state.city, stringResource(R.string.profile_city_placeholder), viewModel::updateCity)

                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isAgreed,
                        onCheckedChange = { isAgreed = it },
                        colors = CheckboxDefaults.colors(checkedColor = ColorPrimaryBlue)
                    )
                    Text(stringResource(R.string.profile_agree), fontSize = 14.sp, color = ColorTextSecondary)
                }
            }
        }

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
                    enabled = state.fullName.isNotBlank() && state.city.isNotBlank() && isAgreed && !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text(stringResource(R.string.profile_create_button), fontSize = 16.sp, fontWeight = FontWeight.Medium)
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
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            placeholder = { Text(placeholder, color = Color.Black.copy(0.5f), fontSize = 16.sp) },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = ColorBorder, focusedBorderColor = Color(0xFF4A90E2))
        )
    }
}
