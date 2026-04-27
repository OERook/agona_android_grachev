package ru.itis.android.reparo.feature.main.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.main.presentation.MainScreenState
import ru.itis.android.main.presentation.MainViewModel
import ru.itis.android.model.Category


// ---------------------------------------------------------------------------
// 1. ГЛАВНЫЙ ЭКРАН (Сборка всех деталей)
// ---------------------------------------------------------------------------
@Composable
fun MainScreen(viewModel: MainViewModel) {
    // Читаем текущее состояние из ViewModel (Загрузка, Успех или Ошибка)
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { ReparoTopBar() },
        bottomBar = { ReparoBottomBar() },
        containerColor = Color.White // Белый фон всего экрана
    ) { paddingValues ->

        // Колонка для основного контента
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Отступы от Scaffold (чтобы контент не залез под Tab Bar)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            ReparoSearchBar()
            Spacer(modifier = Modifier.height(24.dp))

            // Показываем контент в зависимости от состояния
            when (val currentState = state) {
                is MainScreenState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Blue)
                    }
                }
                is MainScreenState.Success -> {
                    ReparoCategories(categories = currentState.categories)
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Популярные услуги",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ReparoServicesGrid()
                }
                is MainScreenState.Error -> {
                    Text(text = "Ошибка: ${currentState.message}", color = Color.Red)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 2. ВЕРХНЯЯ ПАНЕЛЬ (Логотип и Профиль)
// ---------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReparoTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Синий круг с буквой R
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0056D2)), // Красивый синий цвет
                    contentAlignment = Alignment.Center
                ) {
                    Text("R", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                // Название
                Text("Reparo", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.Black)
            }
        },
        actions = {
            // Иконка профиля справа
            IconButton(onClick = { /* TODO: Переход в профиль */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Меню", tint = Color.Black)
            }
        }
    )
}

// ---------------------------------------------------------------------------
// 3. СТРОКА ПОИСКА
// ---------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReparoSearchBar() {
    var searchText by remember { mutableStateOf("") }

    TextField(
        value = searchText,
        onValueChange = { searchText = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Поиск сантехника, электрика...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск", tint = Color.Gray) },
        shape = RoundedCornerShape(12.dp), // Закругленные углы
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF0F0F0),
            unfocusedContainerColor = Color(0xFFF0F0F0),
            focusedIndicatorColor = Color.Transparent, // Убираем нижнюю линию
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

// ---------------------------------------------------------------------------
// 4. СПИСОК КАТЕГОРИЙ (Горизонтальный)
// ---------------------------------------------------------------------------
@Composable
fun ReparoCategories(categories: List<Category>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            // Кнопка-чипс
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFE3EFFF), // Светло-синий фон
                onClick = { /* TODO: Фильтрация по категории */ }
            ) {
                Text(
                    text = category.name,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color(0xFF0056D2), // Темно-синий текст
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 5. СЕТКА ПОПУЛЯРНЫХ УСЛУГ (Заглушка для красоты)
// ---------------------------------------------------------------------------
@Composable
fun ReparoServicesGrid() {
    // Пока у нас нет данных об услугах с сервера, сделаем 4 пустые карточки
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 колонки
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(4) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.height(120.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Услуга", color = Color.Gray)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 6. НИЖНЕЕ МЕНЮ (Tab Bar)
// ---------------------------------------------------------------------------
@Composable
fun ReparoBottomBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        // Главная (Активная)
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Главная") },
            label = { Text("Главная") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF0056D2),
                indicatorColor = Color(0xFFE3EFFF) // Синий фон для активной иконки
            )
        )
        // Поиск (Неактивная)
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
            label = { Text("Поиск") }
        )
        // Заказы (Неактивная)
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Заказы") },
            label = { Text("Заказы") }
        )
        // Профиль (Неактивная)
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Person, contentDescription = "Профиль") },
            label = { Text("Профиль") }
        )
    }
}