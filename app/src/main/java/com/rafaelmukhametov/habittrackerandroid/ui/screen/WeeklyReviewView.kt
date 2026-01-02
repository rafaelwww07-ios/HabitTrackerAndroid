package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.service.GamificationService
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyReviewView(
    habits: List<Habit>,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val gamificationService = remember { GamificationService(context) }
    val progress = remember(gamificationService) { gamificationService.getUserProgress() }
    
    val stats = getWeeklyStats(habits)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Недельный обзор", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Week Overview
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Обзор недели",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    WeeklyReviewStatCard(
                        title = "Выполнено",
                        value = "${stats.completed}",
                        subtitle = "из ${stats.total} целей",
                        icon = "✅",
                        color = Color(0xFF4CAF50)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    WeeklyReviewStatCard(
                        title = "Процент успеха",
                        value = "${stats.successRate.toInt()}%",
                        subtitle = when {
                            stats.successRate >= 80 -> "Отлично!"
                            stats.successRate >= 60 -> "Хорошо"
                            else -> "Можно лучше"
                        },
                        icon = "📊",
                        color = when {
                            stats.successRate >= 80 -> Color(0xFF4CAF50)
                            stats.successRate >= 60 -> Color(0xFFFF6B35)
                            else -> Color(0xFFF44336)
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    WeeklyReviewStatCard(
                        title = "Средний стрик",
                        value = String.format("%.1f", stats.averageStreak),
                        subtitle = "дней подряд",
                        icon = "🔥",
                        color = Color(0xFFFF6B35)
                    )
                }
            }
            
            // Improvements
            val improvements = getImprovements(habits, stats)
            if (improvements.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Улучшения",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        improvements.forEach { improvement ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⬆️", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = improvement,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
            
            // What to Improve
            val improvementsList = getImprovementsList(habits, stats)
            if (improvementsList.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEB3B).copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Что можно улучшить",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        improvementsList.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("💡", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = item,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
            
            // Reward
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEB3B).copy(alpha = 0.2f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🏆", fontSize = 50.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Отличная работа на этой неделе!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Вы заработали баллы и достигли уровня ${progress.level}!",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun WeeklyReviewStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = color.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 24.sp)
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

data class WeeklyStats(
    val completed: Int,
    val total: Int,
    val successRate: Double,
    val averageStreak: Double
)

private fun getWeeklyStats(habits: List<Habit>): WeeklyStats {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val weekStart = calendar.timeInMillis
    
    val today = System.currentTimeMillis()
    var completed = 0
    var total = 0
    var totalStreak = 0
    
    habits.forEach { habit ->
        val weekCompletions = habit.completions.filter { completion ->
            completion.completedAt >= weekStart && completion.completedAt <= today
        }
        
        val expected = habit.goalValue
        total += expected
        completed += minOf(weekCompletions.size, expected)
        totalStreak += habit.currentStreak()
    }
    
    val successRate = if (total > 0) (completed.toDouble() / total.toDouble() * 100.0) else 0.0
    val averageStreak = if (habits.isEmpty()) 0.0 else (totalStreak.toDouble() / habits.size)
    
    return WeeklyStats(completed, total, successRate, averageStreak)
}

private fun getImprovements(habits: List<Habit>, stats: WeeklyStats): List<String> {
    val improvements = mutableListOf<String>()
    
    if (stats.successRate > 80) {
        improvements.add("Вы достигли более 80% успеха на этой неделе!")
    }
    
    val activeHabits = habits.filter { it.currentStreak() > 0 }
    if (activeHabits.size == habits.size) {
        improvements.add("Все ваши привычки имеют активные стрики!")
    }
    
    return improvements
}

private fun getImprovementsList(habits: List<Habit>, stats: WeeklyStats): List<String> {
    val items = mutableListOf<String>()
    
    if (stats.successRate < 60) {
        items.add("Попробуйте установить более реалистичные цели")
    }
    
    val lowStreakHabits = habits.filter { it.currentStreak() < 3 }
    if (lowStreakHabits.isNotEmpty()) {
        items.add("${lowStreakHabits.size} привычкам нужна большая последовательность")
    }
    
    return items
}

