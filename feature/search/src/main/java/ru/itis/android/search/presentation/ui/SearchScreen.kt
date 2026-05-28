package ru.itis.android.search.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.model.Category
import ru.itis.android.model.Service

private val ColorPrimaryBlue = Color(0xFF4A90E2)
private val ColorPrimaryBlueDark = Color(0xFF1E5FA6)
private val ColorLightBlueBg = Color(0xFFEFF6FF)
private val ColorSoftBlueBg = Color(0xFFDBEAFE)
private val ColorTextPrimary = Color(0xFF101828)
private val ColorTextSecondary = Color(0xFF364153)
private val ColorTextGray = Color(0xFF6A7282)
private val ColorBorderLight = Color(0xFFE5E7EB)
private val ColorSearchBg = Color(0xFFF3F4F6)

@Composable
fun SearchScreen(
    services: List<Service> = emptyList(),
    categories: List<Category> = emptyList(),
    isLoading: Boolean = false,
    onServiceClick: (Service) -> Unit = {}
) {
    var query by rememberSaveable { mutableStateOf("") }
    var selectedCategoryId by rememberSaveable { mutableStateOf<Long?>(null) }
    val focusManager = LocalFocusManager.current

    val filtered = remember(query, selectedCategoryId, services) {
        services
            .let { list ->
                if (selectedCategoryId == null) list
                else list.filter { it.categoryId == selectedCategoryId }
            }
            .let { list ->
                if (query.isBlank()) list
                else {
                    val q = query.trim().lowercase()
                    list.filter {
                        it.title.lowercase().contains(q) ||
                            it.description?.lowercase()?.contains(q) == true ||
                            it.masterName?.lowercase()?.contains(q) == true
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Поиск",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTextPrimary
            )
        }

        TextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = { Text("Название услуги или мастер...", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Очистить", tint = Color.Gray)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ColorSearchBg,
                unfocusedContainerColor = ColorSearchBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (categories.isNotEmpty()) {
            WrapRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalSpacing = 8.dp,
                verticalSpacing = 8.dp
            ) {
                SearchChip(
                    label = "Все",
                    isSelected = selectedCategoryId == null,
                    onClick = { selectedCategoryId = null }
                )
                categories.forEach { cat ->
                    SearchChip(
                        label = cat.name,
                        isSelected = selectedCategoryId == cat.id,
                        onClick = {
                            selectedCategoryId = if (selectedCategoryId == cat.id) null else cat.id
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        when {
            isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ColorPrimaryBlue)
            }

            filtered.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ничего не найдено", color = ColorTextGray, fontSize = 15.sp)
            }

            else -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Результаты",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ColorTextPrimary
                    )
                    Text("${filtered.size}", fontSize = 14.sp, color = ColorTextGray)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filtered) { svc ->
                        SearchResultCard(service = svc, onClick = { onServiceClick(svc) })
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun SearchChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) ColorPrimaryBlue else ColorLightBlueBg,
        border = if (isSelected) BorderStroke(1.dp, ColorPrimaryBlue) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label,
                color = if (isSelected) Color.White else ColorPrimaryBlueDark,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 13.sp
            )
        }
    }
}

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

@Composable
private fun SearchResultCard(service: Service, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, ColorBorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(ColorLightBlueBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = service.title.take(1).uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrimaryBlueDark
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    service.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ColorTextPrimary
                )
                val categoryName = service.categoryName
                val masterName = service.masterName
                if (!categoryName.isNullOrBlank() || !masterName.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = listOfNotNull(categoryName, masterName).joinToString(" · "),
                        fontSize = 12.sp,
                        color = ColorTextGray
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${service.price} ₽",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = ColorPrimaryBlueDark
            )
        }
    }
}
