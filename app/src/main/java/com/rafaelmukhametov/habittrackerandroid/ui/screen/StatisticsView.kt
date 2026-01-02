package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.ui.util.getIconImageVector
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsView(
    habits: List<Habit>,
    onBack: () -> Unit
) {
    val totalCompletions = habits.sumOf { it.completions.size }
    val totalStreak = habits.sumOf { it.currentStreak() }
    val averageCompletion = if (habits.isNotEmpty()) {
        habits.map { it.overallCompletionPercentage() }.average()
    } else 0.0
    val mostActiveDay = calculateMostActiveDay(habits)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Статистика", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overall Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Всего выполнений",
                    value = "$totalCompletions",
                    color = Color(0xFF2196F3),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Общий стрик",
                    value = "$totalStreak",
                    color = Color(0xFFFF6B35),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Средний успех",
                    value = "${averageCompletion.toInt()}%",
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Активный день",
                    value = mostActiveDay,
                    color = Color(0xFF9C27B0),
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Habits Breakdown
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "По привычкам",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    habits.forEach { habit ->
                        HabitStatRow(habit = habit)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun HabitStatRow(habit: Habit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = getIconImageVector(habit.iconName),
            contentDescription = habit.name,
            tint = Color(android.graphics.Color.parseColor(habit.colorHex)),
            modifier = Modifier
                .size(24.dp)
                .padding(end = 12.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = habit.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${habit.completions.size} выполнений • Стрик: ${habit.currentStreak()}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Text(
            text = "${habit.overallCompletionPercentage().toInt()}%",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(android.graphics.Color.parseColor(habit.colorHex))
        )
    }
}

private fun calculateMostActiveDay(habits: List<Habit>): String {
    val dayCounts = IntArray(7)
    val calendar = Calendar.getInstance()
    
    habits.forEach { habit ->
        habit.completions.forEach { completion ->
            calendar.timeInMillis = completion.completedAt
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            dayCounts[dayOfWeek - 1]++
        }
    }
    
    val maxIndex = dayCounts.indices.maxByOrNull { dayCounts[it] } ?: 0
    val dayNames = listOf("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб")
    return dayNames[maxIndex]
}


