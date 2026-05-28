package ru.itis.android.profile.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.model.Category
import ru.itis.android.profile.R
import ru.itis.android.profile.presentation.CreateServiceState
import ru.itis.android.presentation.theme.AppColors

@Composable
fun CreateServiceScreen(
    categories: List<Category>,
    createState: CreateServiceState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCategorySelected: (Long) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit = {}
) {
    LaunchedEffect(createState) {
        if (createState is CreateServiceState.Success) onSuccess()
    }

    val idle = createState as? CreateServiceState.Idle ?: CreateServiceState.Idle()

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 120.dp)
        ) {
            ServiceFormTopBar(isEditing = idle.isEditing, onDismiss = onDismiss)

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                ServiceFormFields(
                    idle = idle,
                    onTitleChange = onTitleChange,
                    onDescriptionChange = onDescriptionChange,
                    onPriceChange = onPriceChange
                )

                Spacer(modifier = Modifier.height(20.dp))

                CategorySelector(
                    categories = categories,
                    selectedId = idle.categoryId,
                    onCategorySelected = onCategorySelected
                )

                idle.errorMessage?.let { message ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(message, color = AppColors.Error, fontSize = 13.sp)
                }
            }
        }

        ServiceFormSubmitBar(
            isEditing = idle.isEditing,
            isSubmitting = idle.isSubmitting,
            onSubmit = onSubmit,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ServiceFormTopBar(isEditing: Boolean, onDismiss: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(72.dp)) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
                .size(40.dp)
        ) {
            Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = AppColors.TextSecondary)
        }
        Text(
            text = stringResource(
                if (isEditing) R.string.service_form_edit_title else R.string.service_form_create_title
            ),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ServiceFormFields(
    idle: CreateServiceState.Idle,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit
) {
    LabeledField(label = stringResource(R.string.service_form_name_label)) {
        OutlinedTextField(
            value = idle.title,
            onValueChange = onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(stringResource(R.string.service_form_name_hint), color = Color.Black.copy(0.4f)) },
            shape = RoundedCornerShape(10.dp),
            colors = appOutlinedColors()
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    LabeledField(label = stringResource(R.string.service_form_description_label)) {
        OutlinedTextField(
            value = idle.description,
            onValueChange = onDescriptionChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
            placeholder = { Text(stringResource(R.string.service_form_description_hint), color = Color.Black.copy(0.4f)) },
            shape = RoundedCornerShape(10.dp),
            colors = appOutlinedColors()
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    LabeledField(label = stringResource(R.string.service_form_price_label)) {
        OutlinedTextField(
            value = idle.price,
            onValueChange = onPriceChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(stringResource(R.string.service_form_price_hint), color = Color.Black.copy(0.4f)) },
            shape = RoundedCornerShape(10.dp),
            colors = appOutlinedColors()
        )
    }
}

@Composable
private fun CategorySelector(
    categories: List<Category>,
    selectedId: Long?,
    onCategorySelected: (Long) -> Unit
) {
    Text(
        text = stringResource(R.string.service_form_category_label),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = AppColors.TextSecondary
    )
    Spacer(modifier = Modifier.height(8.dp))

    WrapRow(horizontalSpacing = 8.dp, verticalSpacing = 8.dp) {
        categories.forEach { category ->
            CategoryChip(
                name = category.name,
                isSelected = selectedId == category.id,
                onClick = { onCategorySelected(category.id) }
            )
        }
    }
}

@Composable
private fun ServiceFormSubmitBar(
    isEditing: Boolean,
    isSubmitting: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        border = BorderStroke(0.5.dp, AppColors.BorderLight)
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            Button(
                onClick = onSubmit,
                enabled = !isSubmitting,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(
                            if (isEditing) R.string.service_form_submit_save else R.string.service_form_submit_create
                        ),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun LabeledField(label: String, content: @Composable () -> Unit) {
    Column {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = AppColors.TextSecondary)
        Spacer(modifier = Modifier.height(6.dp))
        content()
    }
}

@Composable
private fun CategoryChip(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) AppColors.PrimaryBlue else AppColors.LightBlueBg,
        onClick = onClick,
        border = if (isSelected) BorderStroke(1.dp, AppColors.PrimaryBlue) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = name,
                color = if (isSelected) Color.White else AppColors.PrimaryBlueDark,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun appOutlinedColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = AppColors.Border,
    focusedBorderColor = AppColors.PrimaryBlue
)

@Composable
private fun WrapRow(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val hPx = horizontalSpacing.roundToPx()
        val vPx = verticalSpacing.roundToPx()
        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0)) }
        val positions = mutableListOf<Pair<Int, Int>>()
        var x = 0; var y = 0; var rowH = 0
        for (p in placeables) {
            if (x > 0 && x + p.width > constraints.maxWidth) { x = 0; y += rowH + vPx; rowH = 0 }
            positions += x to y
            x += p.width + hPx
            if (p.height > rowH) rowH = p.height
        }
        layout(constraints.maxWidth, if (placeables.isEmpty()) 0 else y + rowH) {
            placeables.forEachIndexed { i, p -> p.placeRelative(positions[i].first, positions[i].second) }
        }
    }
}
