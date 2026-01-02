package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelmukhametov.habittrackerandroid.Screen
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.service.MotivationService
import com.rafaelmukhametov.habittrackerandroid.ui.component.HabitCard
import com.rafaelmukhametov.habittrackerandroid.ui.viewmodel.HabitListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListView(
    viewModel: HabitListViewModel,
    onCreateHabitClick: () -> Unit,
    onHabitClick: (Habit) -> Unit,
    onMenuClick: (Screen) -> Unit = {}
) {
    val habits by viewModel.habits.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showingCreateHabit by viewModel.showingCreateHabit.collectAsState()
    val editingHabit by viewModel.editingHabit.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    
    val totalStreak = habits.sumOf { it.currentStreak() }
    val quote = MotivationService.quoteOfTheDay()
    val message = MotivationService.messageForStreak(totalStreak)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои привычки", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Меню")
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Дашборд") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Dashboard)
                            },
                            leadingIcon = { Text("📊") }
                        )
                        DropdownMenuItem(
                            text = { Text("Статистика") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Statistics)
                            },
                            leadingIcon = { Text("📈") }
                        )
                        DropdownMenuItem(
                            text = { Text("Аналитика") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Analytics)
                            },
                            leadingIcon = { Text("📊") }
                        )
                        DropdownMenuItem(
                            text = { Text("Инсайты") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Insights)
                            },
                            leadingIcon = { Text("💡") }
                        )
                        DropdownMenuItem(
                            text = { Text("Сравнение") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Comparison)
                            },
                            leadingIcon = { Text("⚖️") }
                        )
                        DropdownMenuItem(
                            text = { Text("Недельный обзор") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.WeeklyReview)
                            },
                            leadingIcon = { Text("📅") }
                        )
                        DropdownMenuItem(
                            text = { Text("Активность по времени") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.TimeOfDayStats)
                            },
                            leadingIcon = { Text("⏰") }
                        )
                        DropdownMenuItem(
                            text = { Text("Челленджи") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Challenges)
                            },
                            leadingIcon = { Text("🎯") }
                        )
                        DropdownMenuItem(
                            text = { Text("Шаблоны") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Templates)
                            },
                            leadingIcon = { Text("📋") }
                        )
                        DropdownMenuItem(
                            text = { Text("Группы") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Groups)
                            },
                            leadingIcon = { Text("📁") }
                        )
                        DropdownMenuItem(
                            text = { Text("Триггеры") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Triggers)
                            },
                            leadingIcon = { Text("⚡") }
                        )
                        DropdownMenuItem(
                            text = { Text("Достижения") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Achievements)
                            },
                            leadingIcon = { Text("🏆") }
                        )
                        DropdownMenuItem(
                            text = { Text("Профиль") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Profile)
                            },
                            leadingIcon = { Text("👤") }
                        )
                        DropdownMenuItem(
                            text = { Text("Настройки") },
                            onClick = {
                                showMenu = false
                                onMenuClick(Screen.Settings)
                            },
                            leadingIcon = { Text("⚙️") }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateHabitClick
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить привычку")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Motivation Quote Card
            if (habits.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF9C27B0).copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💬", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Цитата дня",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = quote.text,
                            fontSize = 13.sp
                        )
                        if (message.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🔥", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = message,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
            
            when {
                isLoading && habits.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                habits.isEmpty() -> {
                    EmptyHabitsView(
                        onCreateHabitClick = onCreateHabitClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(habits.filter { !it.isArchived }) { habit ->
                            HabitCard(
                                habit = habit,
                                onTap = { viewModel.toggleCompletion(habit) },
                                onLongPress = { onHabitClick(habit) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyHabitsView(
    onCreateHabitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "✨",
            fontSize = 60.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Создайте свою первую привычку!",
            fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Начните свой путь к лучшей версии себя",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onCreateHabitClick) {
            Text("Создать привычку")
        }
    }
}

