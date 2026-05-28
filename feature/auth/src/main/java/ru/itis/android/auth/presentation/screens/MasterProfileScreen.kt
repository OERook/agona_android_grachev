package ru.itis.android.auth.presentation.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.itis.android.auth.presentation.AuthEffect
import ru.itis.android.auth.presentation.AuthViewModel
import ru.itis.android.reparo.feature.auth.R

private val ColorTextPrimary = Color(0xFF101828)
private val ColorTextSecondary = Color(0xFF364153)
private val ColorTextGray = Color(0xFF6A7282)
private val ColorGreenMain = Color(0xFF4CAF50)
private val ColorGreenInfo = Color(0xFF00C950)
private val ColorGreenBg = Color(0xFFF0FDF4)
private val ColorBlueMain = Color(0xFF4A90E2)
private val ColorBorder = Color(0xFFD1D5DC)
private val ColorYellowBg = Color(0xFFFFFBEB)
private val ColorYellowBorder = Color(0xFFFEE685)
private val ColorWarningText = Color(0xFF7B3306)

data class ServiceCategory(val titleResId: Int, val icon: ImageVector, val color: Color)

@Composable
fun getAvailableCategories() = listOf(
    ServiceCategory(R.string.cat_plumbing, Icons.Default.WaterDrop, ColorBlueMain),
    ServiceCategory(R.string.cat_electric, Icons.Default.ElectricBolt, Color(0xFFFF9800)),
    ServiceCategory(R.string.cat_repair, Icons.Default.Build, Color(0xFF9C27B0)),
    ServiceCategory(R.string.cat_finish, Icons.Default.Brush, ColorGreenMain),
    ServiceCategory(R.string.cat_furniture, Icons.Default.Handyman, Color(0xFF795548)),
    ServiceCategory(R.string.cat_ac, Icons.Default.Air, Color(0xFF00BCD4))
)

@Composable
fun MasterProfileScreen(
    viewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onRegistrationSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    var showPhotoDialog by remember { mutableStateOf(false) }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        avatarUri = uri; avatarBitmap = null
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
        avatarBitmap = bmp; avatarUri = null
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AuthEffect.NavigateToMain -> onRegistrationSuccess()
            }
        }
    }

    if (showPhotoDialog) {
        MasterPhotoPickerDialog(
            onDismiss = { showPhotoDialog = false },
            onGallery = {
                showPhotoDialog = false
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onCamera = {
                showPhotoDialog = false
                cameraLauncher.launch(null)
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
            MasterScreenHeader(onBack = onBackClick, title = stringResource(R.string.master_reg_title))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                MasterInfoBanner()

                Spacer(modifier = Modifier.height(24.dp))
                MasterAvatarSection(
                    avatarBitmap = avatarBitmap,
                    avatarUri = avatarUri,
                    onClick = { showPhotoDialog = true }
                )

                Spacer(modifier = Modifier.height(24.dp))
                MasterInputField(stringResource(R.string.profile_name_label), state.fullName, stringResource(R.string.profile_name_placeholder), viewModel::updateFullName)
                MasterInputField(stringResource(R.string.profile_email_label), state.email, stringResource(R.string.profile_email_placeholder), viewModel::updateEmail)
                MasterInputField(stringResource(R.string.profile_city_label), state.city, stringResource(R.string.profile_city_placeholder), viewModel::updateCity)
                MasterInputField(
                    label = stringResource(R.string.master_exp_label),
                    value = state.experienceYears?.toString() ?: "",
                    placeholder = stringResource(R.string.master_exp_placeholder),
                    onValueChange = { viewModel.updateExperience(it.toIntOrNull() ?: 0) }
                )

                Spacer(modifier = Modifier.height(24.dp))
                MasterCategorySection(
                    selectedCategories = state.selectedCategories,
                    onToggle = viewModel::toggleCategory
                )

                Spacer(modifier = Modifier.height(24.dp))
                MasterAboutSection(about = state.about, onAboutChange = viewModel::updateAbout)

                Spacer(modifier = Modifier.height(24.dp))
                MasterWarningCard()
            }
        }

        MasterRegistrationFooter(
            modifier = Modifier.align(Alignment.BottomCenter),
            errorMessage = state.errorMessage,
            isLoading = state.isLoading,
            enabled = state.fullName.isNotBlank(),
            onRegister = { viewModel.register() }
        )
    }
}

@Composable
private fun MasterPhotoPickerDialog(onDismiss: () -> Unit, onGallery: () -> Unit, onCamera: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.photo_dialog_title), fontWeight = FontWeight.Bold) },
        text = { Text(stringResource(R.string.photo_dialog_text)) },
        containerColor = Color.White,
        confirmButton = {
            TextButton(onClick = onGallery) {
                Text(stringResource(R.string.photo_dialog_gallery), color = ColorBlueMain)
            }
        },
        dismissButton = {
            TextButton(onClick = onCamera) {
                Text(stringResource(R.string.photo_dialog_camera), color = ColorBlueMain)
            }
        }
    )
}

@Composable
private fun MasterScreenHeader(onBack: () -> Unit, title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(97.dp)
            .border(width = 0.5.dp, color = Color(0xFFE5E7EB))
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp).size(40.dp)
        ) {
            Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = ColorTextSecondary)
        }
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ColorTextPrimary,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun MasterInfoBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth().height(127.dp),
        color = ColorGreenBg,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(2.dp, ColorGreenInfo)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00A63E), modifier = Modifier.size(20.dp))
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(stringResource(R.string.master_info_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorTextPrimary)
                Text(stringResource(R.string.master_info_subtitle), fontSize = 14.sp, color = ColorTextSecondary, lineHeight = 20.sp)
            }
        }
    }
}

@Composable
private fun MasterAvatarSection(avatarBitmap: Bitmap?, avatarUri: Uri?, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.size(112.dp).clip(CircleShape).clickable(onClick = onClick)) {
            Box(
                modifier = Modifier.fillMaxSize().clip(CircleShape).background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                avatarBitmap?.let { bmp ->
                    Image(bitmap = bmp.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } ?: if (avatarUri != null) {
                    AsyncImage(model = avatarUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
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
                    .background(ColorBlueMain),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
        Text(stringResource(R.string.profile_add_photo), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextSecondary, modifier = Modifier.padding(top = 12.dp))
        Text(stringResource(R.string.master_photo_trust), fontSize = 12.sp, color = ColorTextGray)
    }
}

@Composable
private fun MasterCategorySection(selectedCategories: List<String>, onToggle: (String) -> Unit) {
    val availableCategories = getAvailableCategories()
    Text(stringResource(R.string.master_services_label), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextSecondary)
    Text(
        stringResource(R.string.master_services_subtitle),
        fontSize = 12.sp,
        color = ColorTextGray,
        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )
    Column {
        for (i in availableCategories.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                val cat1 = availableCategories[i]
                val title1 = stringResource(cat1.titleResId)
                ServiceCard(
                    title = title1,
                    category = cat1,
                    isSelected = selectedCategories.contains(title1),
                    onClick = { onToggle(title1) },
                    modifier = Modifier.weight(1f)
                )
                if (i + 1 < availableCategories.size) {
                    val cat2 = availableCategories[i + 1]
                    val title2 = stringResource(cat2.titleResId)
                    ServiceCard(
                        title = title2,
                        category = cat2,
                        isSelected = selectedCategories.contains(title2),
                        onClick = { onToggle(title2) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun MasterAboutSection(about: String?, onAboutChange: (String) -> Unit) {
    Text(stringResource(R.string.master_about_label), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextSecondary)
    OutlinedTextField(
        value = about ?: "",
        onValueChange = onAboutChange,
        modifier = Modifier.fillMaxWidth().heightIn(min = 124.dp).padding(top = 8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = ColorBorder, focusedBorderColor = ColorBlueMain),
        placeholder = { Text(stringResource(R.string.master_about_placeholder), color = Color.Gray.copy(0.5f)) }
    )
    Text(stringResource(R.string.master_about_footer), fontSize = 12.sp, color = ColorTextGray, modifier = Modifier.padding(top = 4.dp))
}

@Composable
private fun MasterWarningCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ColorYellowBg,
        border = BorderStroke(1.dp, ColorYellowBorder),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            stringResource(R.string.master_warning),
            fontSize = 12.sp,
            color = ColorWarningText,
            modifier = Modifier.padding(16.dp),
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun MasterRegistrationFooter(
    modifier: Modifier,
    errorMessage: String?,
    isLoading: Boolean,
    enabled: Boolean,
    onRegister: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth().height(105.dp),
        border = BorderStroke(0.5.dp, Color(0xFFE5E7EB)),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Button(
                onClick = onRegister,
                modifier = Modifier.fillMaxWidth().height(56.dp).shadow(elevation = 8.dp, shape = CircleShape),
                colors = ButtonDefaults.buttonColors(containerColor = ColorGreenMain),
                shape = CircleShape,
                enabled = enabled && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text(stringResource(R.string.master_submit_button), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun MasterInputField(label: String, value: String, placeholder: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextSecondary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            placeholder = { Text(placeholder, color = Color.Black.copy(0.5f), fontSize = 16.sp) },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = ColorBorder, focusedBorderColor = ColorBlueMain)
        )
    }
}

@Composable
fun ServiceCard(title: String, category: ServiceCategory, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    val cardHeight = if (title.length > 12) 132.dp else 112.dp

    Surface(
        onClick = onClick,
        modifier = modifier.height(cardHeight),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) category.color.copy(alpha = 0.08f) else Color.White,
        border = BorderStroke(2.dp, if (isSelected) category.color else Color(0xFFE5E7EB))
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            Column {
                Box(
                    modifier = Modifier.size(48.dp).background(category.color.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(category.icon, contentDescription = null, tint = category.color, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextPrimary, maxLines = 2)
            }
            if (isSelected) {
                Box(
                    modifier = Modifier.align(Alignment.TopEnd).size(20.dp).background(category.color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}
