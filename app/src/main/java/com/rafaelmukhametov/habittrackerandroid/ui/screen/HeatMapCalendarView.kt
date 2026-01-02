package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.ui.util.formatDate
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeatMapCalendarView(
    habit: Habit,
    year: Int = Calendar.getInstance().get(Calendar.YEAR),
    onBack: () -> Unit
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    val weeks = remember { generateWeeks(habit, year) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Годовая активность $year", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Меньше",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    for (i in 0..4) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    color = intensityColor(i / 4.0),
                                    shape = RoundedCornerShape(3.dp)
                                )
                        )
                    }
                }
                
                Text(
                    text = "Больше",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            // Calendar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Day labels
                Column(
                    modifier = Modifier.width(20.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    listOf("Пн", "Ср", "Пт").forEach { day ->
                        Text(
                            text = day,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.height(12.dp)
                        )
                    }
                }
                
                // Weeks
                HorizontalScrollableHeatMap(
                    weeks = weeks,
                    habitColor = Color(android.graphics.Color.parseColor(habit.colorHex)),
                    onDayClick = { date -> selectedDate = date }
                )
            }
            
            // Selected day info
            selectedDate?.let { date ->
                val completions = habit.completions.filter { completion ->
                    isSameDay(completion.completedAt, date)
                }
                
                if (completions.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = formatDate(date),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Выполнено ${completions.size} раз",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalScrollableHeatMap(
    weeks: List<WeekData>,
    habitColor: Color,
    onDayClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        weeks.forEach { week ->
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "${week.weekNumber}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.height(12.dp)
                )
                week.days.forEach { dayData ->
                    if (dayData != null) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    color = intensityColor(dayData.intensity, habitColor),
                                    shape = RoundedCornerShape(3.dp)
                                )
                                .clickable { onDayClick(dayData.date) }
                        )
                    } else {
                        Spacer(modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}

data class DayData(
    val date: Long,
    val intensity: Double
)

data class WeekData(
    val weekNumber: Int,
    val days: List<DayData?>
)

fun generateWeeks(habit: Habit, year: Int): List<WeekData> {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, Calendar.JANUARY)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val yearStart = calendar.timeInMillis
    
    calendar.set(Calendar.MONTH, Calendar.DECEMBER)
    calendar.set(Calendar.DAY_OF_MONTH, 31)
    val yearEnd = calendar.timeInMillis
    
    val weeks = mutableListOf<WeekData>()
    var currentDate = yearStart
    var currentWeek = mutableListOf<DayData?>()
    var weekNumber = 1
    
    // Start from Monday
    calendar.timeInMillis = yearStart
    val weekday = calendar.get(Calendar.DAY_OF_WEEK)
    val daysToMonday = (weekday + 5) % 7
    
    // Fill empty days before Monday
    repeat(daysToMonday) {
        currentWeek.add(null)
    }
    
    while (currentDate <= yearEnd) {
        val completions = habit.completions.filter { completion ->
            isSameDay(completion.completedAt, currentDate)
        }
        
        val intensity = (completions.size.toDouble() / 3.0).coerceAtMost(1.0)
        currentWeek.add(DayData(date = currentDate, intensity = intensity))
        
        calendar.timeInMillis = currentDate
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            weeks.add(WeekData(weekNumber = weekNumber, days = currentWeek.toList()))
            currentWeek = mutableListOf()
            weekNumber++
        }
        
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        currentDate = calendar.timeInMillis
    }
    
    // Add last week
    if (currentWeek.isNotEmpty()) {
        while (currentWeek.size < 7) {
            currentWeek.add(null)
        }
        weeks.add(WeekData(weekNumber = weekNumber, days = currentWeek.toList()))
    }
    
    return weeks
}

fun intensityColor(intensity: Double, baseColor: Color = Color(0xFF2196F3)): Color {
    return when {
        intensity == 0.0 -> Color.Gray.copy(alpha = 0.2f)
        intensity <= 0.25 -> baseColor.copy(alpha = 0.4f)
        intensity <= 0.5 -> baseColor.copy(alpha = 0.6f)
        intensity <= 0.75 -> baseColor.copy(alpha = 0.8f)
        else -> baseColor
    }
}

fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
    val cal1 = Calendar.getInstance()
    cal1.timeInMillis = timestamp1
    val cal2 = Calendar.getInstance()
    cal2.timeInMillis = timestamp2
    
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}


