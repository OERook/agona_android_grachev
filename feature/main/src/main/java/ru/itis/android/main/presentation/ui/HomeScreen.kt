package ru.itis.android.main.presentation.ui

import ru.itis.android.presentation.theme.AppColors

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Carpenter
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.main.presentation.MainScreenState
import ru.itis.android.model.Category
import ru.itis.android.model.Service
import ru.itis.android.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: MainScreenState,
    onRefresh: () -> Unit,
    onCategorySelected: (Long?) -> Unit,
    onServiceClick: (Service) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppColors.PrimaryBlueDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("R", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Reparo", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = AppColors.TextPrimary)
                }
            },
            actions = {
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Обновить", tint = AppColors.TextSecondary)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Поиск услуг и мастеров...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = AppColors.SearchBg,
                    unfocusedContainerColor = AppColors.SearchBg,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            CategoryRow(
                categories = state.categories,
                selectedCategoryId = state.selectedCategoryId,
                onCategorySelected = onCategorySelected
            )

            Spacer(modifier = Modifier.height(20.dp))

            val filtered = filterServices(state.services, searchText, state.selectedCategoryId)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.selectedCategoryId != null)
                        state.categories.find { it.id == state.selectedCategoryId }?.name ?: "Услуги"
                    else
                        "Популярные услуги",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
                if (filtered.isNotEmpty()) {
                    Text(
                        text = "${filtered.size}",
                        fontSize = 14.sp,
                        color = AppColors.TextGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ServicesContent(
                isLoading = state.isLoading,
                services = filtered,
                onServiceClick = onServiceClick,
                emptyMessage = if (state.selectedCategoryId != null)
                    "В этой категории пока нет услуг"
                else if (state.role == UserRole.MASTER)
                    "Нажмите + чтобы создать первую услугу"
                else
                    "Услуги ещё не добавлены"
            )
        }
    }
}

private fun filterServices(
    services: List<Service>,
    query: String,
    categoryId: Long?
): List<Service> {
    val byCategory = if (categoryId == null) services
    else services.filter { it.categoryId == categoryId }

    if (query.isBlank()) return byCategory
    val q = query.trim().lowercase()
    return byCategory.filter {
        it.title.lowercase().contains(q) ||
            (it.description?.lowercase()?.contains(q) == true) ||
            (it.masterName?.lowercase()?.contains(q) == true)
    }
}

@Composable
private fun CategoryRow(
    categories: List<Category>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long?) -> Unit
) {
    if (categories.isEmpty()) return

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            CategoryChip(
                label = "Все",
                isSelected = selectedCategoryId == null,
                onClick = { onCategorySelected(null) }
            )
        }
        items(categories) { category ->
            CategoryChip(
                label = category.name,
                isSelected = selectedCategoryId == category.id,
                onClick = {
                    onCategorySelected(
                        if (selectedCategoryId == category.id) null else category.id
                    )
                }
            )
        }
    }
}

@Composable
private fun CategoryChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) AppColors.PrimaryBlue else AppColors.LightBlueBg,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else AppColors.PrimaryBlueDark,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ServicesContent(
    isLoading: Boolean,
    services: List<Service>,
    onServiceClick: (Service) -> Unit,
    emptyMessage: String
) {
    when {
        isLoading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator(color = AppColors.PrimaryBlue) }

        services.isEmpty() -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(emptyMessage, color = AppColors.TextGray, fontSize = 14.sp)
        }

        else -> LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(services) { service -> ServiceCard(service, onServiceClick) }
        }
    }
}

private fun serviceIcon(categoryName: String?): ImageVector {
    val name = categoryName?.lowercase() ?: ""
    return when {
        "сантех" in name || "кран" in name || "труб" in name -> Icons.Default.Plumbing
        "электр" in name || "люстр" in name || "свет" in name -> Icons.Default.ElectricalServices
        "убор" in name || "чист" in name -> Icons.Default.CleaningServices
        "мебел" in name || "сбор" in name || "плотн" in name -> Icons.Default.Carpenter
        else -> Icons.Default.Handyman
    }
}

@Composable
private fun ServiceCard(service: Service, onClick: (Service) -> Unit) {
    Surface(
        onClick = { onClick(service) },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(AppColors.LightBlueBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = serviceIcon(service.categoryName),
                    contentDescription = null,
                    tint = AppColors.PrimaryBlueDark,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = service.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "От",
                fontSize = 12.sp,
                color = AppColors.TextGray
            )
            Text(
                text = "${service.price} ₽/час",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.PrimaryBlueDark
            )
        }
    }
}
