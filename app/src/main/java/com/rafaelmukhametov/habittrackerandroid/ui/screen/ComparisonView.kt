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
fun ComparisonView(
    habits: List<Habit>,
    onBack: () -> Unit
) {
    val currentStats = getCurrentMonthStats(habits)
    val previousStats = getPreviousMonthStats(habits)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Сравнение периодов", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Overall Comparison
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Общее сравнение",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ComparisonCard(
                        title = "Всего выполнений",
                        current = currentStats.totalCompletions.toDouble(),
                        previous = previousStats.totalCompletions.toDouble(),
                        unit = ""
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ComparisonCard(
                        title = "Средний стрик",
                        current = currentStats.averageStreak,
                        previous = previousStats.averageStreak,
                        unit = " дней"
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ComparisonCard(
                        title = "Процент успеха",
                        current = currentStats.successRate,
                        previous = previousStats.successRate,
                        unit = "%"
                    )
                }
            }
            
            // Habits Comparison
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
                        val current = getHabitCompletionsForMonth(habit, isCurrent = true)
                        val previous = getHabitCompletionsForMonth(habit, isCurrent = false)
                        val trend = when {
                            current > previous -> "↑"
                            current < previous -> "↓"
                            else -> "→"
                        }
                        val trendColor = when {
                            current > previous -> Color(0xFF4CAF50)
                            current < previous -> Color(0xFFF44336)
                            else -> Color.Gray
                        }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
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
                                    text = "$current vs $previous выполнений",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            Text(
                                text = trend,
                                fontSize = 24.sp,
                                color = trendColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComparisonCard(
    title: String,
    current: Double,
    previous: Double,
    unit: String
) {
    val difference = current - previous
    val percentageChange = if (previous != 0.0) {
        (difference / previous) * 100.0
    } else 0.0
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Этот месяц",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "${current.toInt()}$unit",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Прошлый месяц",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "${previous.toInt()}$unit",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (difference >= 0) "Улучшение" else "Снижение",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "${if (difference >= 0) "+" else ""}${String.format("%.1f", difference)}$unit (${String.format("%.1f", percentageChange)}%)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (difference >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}

data class MonthStats(
    val totalCompletions: Int = 0,
    val averageStreak: Double = 0.0,
    val successRate: Double = 0.0
)

private fun getCurrentMonthStats(habits: List<Habit>): MonthStats {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val monthStart = calendar.timeInMillis
    
    val today = System.currentTimeMillis()
    val completions = getAllCompletions(habits, monthStart, today)
    val streaks = habits.map { it.currentStreak() }
    
    return MonthStats(
        totalCompletions = completions.size,
        averageStreak = if (streaks.isEmpty()) 0.0 else streaks.average(),
        successRate = calculateSuccessRate(habits, monthStart, today)
    )
}

private fun getPreviousMonthStats(habits: List<Habit>): MonthStats {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val monthStart = calendar.timeInMillis
    
    calendar.add(Calendar.MONTH, -1)
    val previousMonthStart = calendar.timeInMillis
    
    val completions = getAllCompletions(habits, previousMonthStart, monthStart)
    
    return MonthStats(
        totalCompletions = completions.size,
        averageStreak = 0.0,
        successRate = calculateSuccessRate(habits, previousMonthStart, monthStart)
    )
}

private fun getAllCompletions(habits: List<Habit>, startDate: Long, endDate: Long): List<com.rafaelmukhametov.habittrackerandroid.domain.model.HabitCompletion> {
    return habits.flatMap { habit ->
        habit.completions.filter { completion ->
            completion.completedAt >= startDate && completion.completedAt <= endDate
        }
    }
}

private fun getHabitCompletionsForMonth(habit: Habit, isCurrent: Boolean): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val monthStart = calendar.timeInMillis
    
    val startDate: Long
    val endDate: Long
    
    if (isCurrent) {
        startDate = monthStart
        endDate = System.currentTimeMillis()
    } else {
        calendar.add(Calendar.MONTH, -1)
        startDate = calendar.timeInMillis
        endDate = monthStart
    }
    
    return habit.completions.count { completion ->
        completion.completedAt >= startDate && completion.completedAt <= endDate
    }
}

private fun calculateSuccessRate(habits: List<Habit>, startDate: Long, endDate: Long): Double {
    val days = ((endDate - startDate) / (24 * 60 * 60 * 1000L)).toInt()
    val totalCompletions = getAllCompletions(habits, startDate, endDate).size
    val expectedCompletions = habits.sumOf { it.goalValue } * days / 7
    
    return if (expectedCompletions > 0) {
        (totalCompletions.toDouble() / expectedCompletions.toDouble() * 100.0)
    } else 0.0
}


