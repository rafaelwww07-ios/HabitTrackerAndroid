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
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeOfDayStatsView(
    habits: List<Habit>,
    onBack: () -> Unit
) {
    val hourData = getHourDistribution(habits)
    val topHours = getTopActiveHours(habits, limit = 5)
    val recommendation = generateRecommendation(habits)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Активность по времени", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Time Distribution Chart
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Распределение активности",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SimpleBarChart(data = hourData)
                }
            }
            
            // Most Active Hours
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Самые активные часы",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    topHours.forEachIndexed { index, hourData ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 1}.",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.width(30.dp)
                            )
                            Text(
                                text = "${hourData.hour}:00 - ${hourData.hour + 1}:00",
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${hourData.count} выполнений",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(6.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(3.dp)
                                    )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(
                                            (100.dp * (hourData.count.toFloat() / (topHours.firstOrNull()?.count ?: 1).toFloat())).coerceAtMost(100.dp)
                                        )
                                        .background(
                                            color = Color(0xFF2196F3),
                                            shape = RoundedCornerShape(3.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
            
            // Recommendations
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEB3B).copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("💡", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = recommendation,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleBarChart(data: List<Int>, modifier: Modifier = Modifier) {
    val maxValue = data.maxOrNull() ?: 1
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEachIndexed { hour, count ->
            val height = (count.toFloat() / maxValue).coerceIn(0.1f, 1.0f)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(height)
                        .background(
                            color = Color(0xFF2196F3).copy(alpha = 0.7f),
                            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$hour",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

private fun getHourDistribution(habits: List<Habit>): List<Int> {
    val hourCounts = IntArray(24) { 0 }
    val calendar = Calendar.getInstance()
    
    habits.forEach { habit ->
        habit.completions.forEach { completion ->
            calendar.timeInMillis = completion.completedAt
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            hourCounts[hour]++
        }
    }
    
    return hourCounts.toList()
}

private fun getTopActiveHours(habits: List<Habit>, limit: Int): List<HourData> {
    val hourData = getHourDistribution(habits)
    return hourData.mapIndexed { hour, count ->
        HourData(hour = hour, count = count)
    }
        .sortedByDescending { it.count }
        .take(limit)
}

data class HourData(
    val hour: Int,
    val count: Int
)

private fun generateRecommendation(habits: List<Habit>): String {
    val topHours = getTopActiveHours(habits, limit = 1)
    val mostActive = topHours.firstOrNull() ?: return "Начните отслеживать привычки в разное время дня, чтобы увидеть паттерны активности."
    
    return when {
        mostActive.hour < 9 -> "Вы наиболее активны рано утром! Рассмотрите возможность установки утренних напоминаний для новых привычек."
        mostActive.hour < 17 -> "Ваша активность сосредоточена в дневное время. Это отличное время для продуктивных привычек!"
        else -> "Вы предпочитаете выполнять привычки вечером. Убедитесь, что у вас есть достаточно времени и энергии."
    }
}

