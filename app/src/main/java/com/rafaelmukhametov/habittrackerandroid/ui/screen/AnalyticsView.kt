package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.background
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
fun AnalyticsView(
    habits: List<Habit>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Аналитика", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Predictions
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Прогнозы",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    habits.take(3).forEach { habit ->
                        PredictionCard(habit = habit)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            
            // Trends
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Тренды",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ваши привычки за последние 30 дней",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SimpleTrendChart(habits = habits)
                }
            }
            
            // Activity Analysis
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Анализ активности",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Самый активный день",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = getMostActiveDay(habits),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Менее активный день",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = getLeastActiveDay(habits),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            // Recommendations
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Рекомендации",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    generateRecommendations(habits).take(5).forEach { recommendation ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("💡", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = recommendation,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PredictionCard(habit: Habit) {
    val habitColor = Color(android.graphics.Color.parseColor(habit.colorHex))
    val prediction = calculatePrediction(habit)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = habitColor.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
                Icon(
                    imageVector = getIconImageVector(habit.iconName),
                    contentDescription = habit.name,
                    tint = Color(android.graphics.Color.parseColor(habit.colorHex)),
                    modifier = Modifier.size(24.dp)
                )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Прогноз: $prediction",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun SimpleTrendChart(habits: List<Habit>) {
    // Simplified trend visualization
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "График трендов (упрощенная версия)",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

private fun calculatePrediction(habit: Habit): String {
    val streak = habit.currentStreak()
    val successRate = habit.overallCompletionPercentage()
    
    return when {
        successRate > 80 -> "Отличный результат, продолжайте!"
        streak > 7 -> "Хороший прогресс, стрик растет!"
        else -> "Нужно больше активности"
    }
}

private fun getMostActiveDay(habits: List<Habit>): String {
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

private fun getLeastActiveDay(habits: List<Habit>): String {
    val dayCounts = IntArray(7)
    val calendar = Calendar.getInstance()
    
    habits.forEach { habit ->
        habit.completions.forEach { completion ->
            calendar.timeInMillis = completion.completedAt
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            dayCounts[dayOfWeek - 1]++
        }
    }
    
    val minIndex = dayCounts.indices.minByOrNull { dayCounts[it] } ?: 0
    val dayNames = listOf("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб")
    return dayNames[minIndex]
}

private fun generateRecommendations(habits: List<Habit>): List<String> {
    val recommendations = mutableListOf<String>()
    
    habits.forEach { habit ->
        val streak = habit.currentStreak()
        if (streak == 0) {
            recommendations.add("Начните выполнять '${habit.name}' - это поможет вам вернуться в ритм!")
        } else if (streak < 7) {
            recommendations.add("Отличный старт с '${habit.name}'! Попробуйте дойти до 7 дней подряд!")
        }
    }
    
    if (recommendations.isEmpty()) {
        recommendations.add("Вы отлично справляетесь! Продолжайте в том же духе.")
    }
    
    return recommendations
}


