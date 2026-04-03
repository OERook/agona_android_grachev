package ru.itis.android.auth.presentation.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.auth.presentation.AuthViewModel
import ru.itis.android.reparo.feature.auth.R

private val ColorPrimaryBlue = Color(0xFF4A90E2)
private val ColorTextDark = Color(0xFF101828)
private val ColorTextMedium = Color(0xFF364153)
private val ColorTextLight = Color(0xFF4A5565)
private val ColorBorder = Color(0xFFD1D5DC)
private val ColorGrayBg = Color(0xFFE5E7EB)
private val ColorIconGray = Color(0xFF99A1AF)

@Composable
fun ClientProfileScreen(
    viewModel: AuthViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    var isAgreed by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(bottom = 104.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(97.dp)
                    .border(width = 1.dp, color = ColorGrayBg)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp).size(40.dp)
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = stringResource(R.string.phone_back), tint = ColorTextMedium)
                }
                Text(
                    text = stringResource(R.string.profile_creation_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextDark,
                    lineHeight = 36.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(96.dp)) {
                        Box(
                            modifier = Modifier.fillMaxSize().clip(CircleShape).background(ColorGrayBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = ColorIconGray, modifier = Modifier.size(32.dp))
                        }
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.BottomEnd)
                                .shadow(elevation = 6.dp, shape = CircleShape)
                                .clip(CircleShape)
                                .background(ColorPrimaryBlue)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(stringResource(R.string.profile_add_photo), fontSize = 14.sp, color = ColorTextLight, lineHeight = 20.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))

                ClientInputField(stringResource(R.string.profile_name_label), state.fullName, stringResource(R.string.profile_name_placeholder), viewModel::updateFullName)
                ClientInputField(stringResource(R.string.profile_email_label), state.email, stringResource(R.string.profile_email_placeholder), viewModel::updateEmail)
                ClientInputField(stringResource(R.string.profile_city_label), state.city, stringResource(R.string.profile_city_placeholder), viewModel::updateCity)

                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(stringResource(R.string.profile_about_label), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextMedium, lineHeight = 20.sp)
                    OutlinedTextField(
                        value = state.about ?: "",
                        onValueChange = viewModel::updateAbout,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 124.dp).padding(top = 8.dp),
                        placeholder = { Text(stringResource(R.string.profile_about_placeholder), color = Color.Black.copy(alpha = 0.5f), fontSize = 16.sp) },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = ColorBorder, focusedBorderColor = ColorPrimaryBlue)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().height(32.dp).padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isAgreed,
                        onCheckedChange = { isAgreed = it },
                        modifier = Modifier.size(20.dp),
                        colors = CheckboxDefaults.colors(checkedColor = ColorPrimaryBlue)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.profile_agree), fontSize = 14.sp, color = ColorTextMedium)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        Surface(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(104.dp),
            border = BorderStroke(width = 1.dp, color = ColorGrayBg),
            color = Color.White
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Button(
                    onClick = { viewModel.register() },
                    modifier = Modifier.fillMaxWidth().height(56.dp).shadow(elevation = 6.dp, shape = CircleShape),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimaryBlue, contentColor = Color.White),
                    enabled = state.fullName.isNotBlank() && isAgreed
                ) {
                    Text(stringResource(R.string.profile_create_button), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun ClientInputField(label: String, value: String, placeholder: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextMedium, lineHeight = 20.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            placeholder = { Text(placeholder, color = Color.Black.copy(alpha = 0.5f), fontSize = 16.sp) },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = ColorBorder, focusedBorderColor = ColorPrimaryBlue)
        )
    }
}
