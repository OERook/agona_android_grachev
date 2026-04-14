package ru.itis.android.auth.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.auth.domain.validation.AuthValidator
import ru.itis.android.auth.presentation.AuthEffect
import ru.itis.android.auth.presentation.AuthViewModel
import ru.itis.android.reparo.feature.auth.R

private val ColorPrimaryBlue = Color(0xFF4A90E2)
private val ColorTextDark = Color(0xFF101828)
private val ColorTextMedium = Color(0xFF364153)
private val ColorTextLight = Color(0xFF4A5565)
private val ColorTextGray = Color(0xFF6A7282)
private val ColorBorder = Color(0xFFD1D5DC)
val ColorError = Color(0xFFEF4444)
private val ColorSuccess = Color(0xFF10B981)

@Composable
fun PhoneScreen(
    viewModel: AuthViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val isPhoneValid = AuthValidator.validatePhone("+7${state.phone}")
    val isPasswordValid = AuthValidator.validatePassword(state.password)
    val isPasswordsMatch = state.password == confirmPassword && confirmPassword.isNotEmpty()

    val isLoginMode = state.isLoginMode


    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp).size(40.dp)
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = stringResource(R.string.phone_back), tint = ColorTextMedium)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isLoginMode) "Вход в аккаунт" else stringResource(R.string.phone_title),
                    fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ColorTextDark, lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isLoginMode) "С возвращением! Рады видеть вас снова." else stringResource(R.string.phone_subtitle),
                    fontSize = 14.sp, color = ColorTextLight, lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(stringResource(R.string.phone_label), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextMedium, lineHeight = 20.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.width(89.75.dp).height(56.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(2.dp, ColorBorder),
                        color = Color.White
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("+7", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = ColorTextDark)
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = ColorTextLight, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    CustomInputField(
                        value = state.phone,
                        onValueChange = viewModel::updatePhone,
                        placeholder = stringResource(R.string.phone_placeholder),
                        keyboardType = KeyboardType.Phone,
                        isSuccess = isPhoneValid,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                if (!isLoginMode) {
                    Text(stringResource(R.string.phone_footer), fontSize = 12.sp, color = ColorTextGray, lineHeight = 16.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = if (isLoginMode) "Ваш пароль" else stringResource(R.string.password_title),
                    fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ColorTextDark
                )

                if (!isLoginMode) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.password_subtitle), fontSize = 14.sp, color = ColorTextLight)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(stringResource(R.string.password_label), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextMedium)
                Spacer(modifier = Modifier.height(8.dp))
                CustomInputField(
                    value = state.password,
                    onValueChange = viewModel::updatePassword,
                    placeholder = stringResource(R.string.password_placeholder),
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    isPasswordVisible = isPasswordVisible,
                    onTogglePassword = { isPasswordVisible = !isPasswordVisible },
                    isSuccess = if (isLoginMode) state.password.isNotEmpty() else isPasswordValid,
                    modifier = Modifier.fillMaxWidth()
                )

                if (!isLoginMode) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(stringResource(R.string.password_confirm_label), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ColorTextMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomInputField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = stringResource(R.string.password_confirm_placeholder),
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        isPasswordVisible = isPasswordVisible,
                        onTogglePassword = { isPasswordVisible = !isPasswordVisible },
                        isError = state.password != confirmPassword && confirmPassword.isNotEmpty(),
                        isSuccess = isPasswordsMatch,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (state.password != confirmPassword && confirmPassword.isNotEmpty()) {
                        Text(stringResource(R.string.password_error_mismatch), color = ColorError, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    PasswordRequirement(stringResource(R.string.password_req_length), state.password.length >= 6)
                    PasswordRequirement(stringResource(R.string.password_req_upper), state.password.any { it.isUpperCase() })
                    PasswordRequirement(stringResource(R.string.password_req_digit), state.password.any { it.isDigit() })
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.errorMessage != null) {
                    Text(
                        text = state.errorMessage!!,
                        color = ColorError,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Button(
                    onClick = {
                        if (isLoginMode) {
                            viewModel.login()
                        } else {
                            onNext()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp).shadow(elevation = 6.dp, shape = CircleShape),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimaryBlue, contentColor = Color.White),
                    enabled = if (isLoginMode) {
                        isPhoneValid && state.password.isNotEmpty() && !state.isLoading
                    } else {
                        isPhoneValid && isPasswordValid && isPasswordsMatch && !state.isLoading
                    }
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = if (isLoginMode) "Войти" else stringResource(R.string.continue_button),
                            fontSize = 16.sp, fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isLoginMode) "Ещё нет аккаунта? Зарегистрироваться" else "Уже есть аккаунт? Войти",
                    color = ColorPrimaryBlue,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable {
                            viewModel.clearError()
                            viewModel.toggleLoginMode()
                        }
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun PasswordRequirement(text: String, isMet: Boolean) {
    Row(modifier = Modifier.padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = if (isMet) ColorSuccess else ColorTextGray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 12.sp, color = if (isMet) ColorTextDark else ColorTextGray)
    }
}

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {},
    isError: Boolean = false,
    isSuccess: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = when {
        isError -> ColorError
        isSuccess -> ColorSuccess
        isFocused -> ColorPrimaryBlue
        else -> ColorBorder
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(56.dp)
            .border(2.dp, borderColor, RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp)
            .onFocusChanged { isFocused = it.isFocused },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = LocalTextStyle.current.copy(color = Color.Black, fontSize = 16.sp),
        cursorBrush = SolidColor(ColorPrimaryBlue),
        decorationBox = { innerTextField ->
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(placeholder, color = Color.Black.copy(alpha = 0.5f), fontSize = 16.sp)
                    innerTextField()
                }
                if (isPassword) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = ColorTextLight,
                        modifier = Modifier.size(20.dp).clickable(onClick = onTogglePassword)
                    )
                }
            }
        }
    )
}