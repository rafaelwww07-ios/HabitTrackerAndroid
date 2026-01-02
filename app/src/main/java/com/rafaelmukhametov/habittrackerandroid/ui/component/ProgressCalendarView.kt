package com.rafaelmukhametov.habittrackerandroid.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun ProgressCalendarView(
    completions: List<Long>,
    habitColor: Color,
    modifier: Modifier = Modifier
) {
    val calendar = Calendar.getInstance()
    val today = System.currentTimeMillis()
    
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val monthStart = calendar.timeInMillis
    
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    
    // Day names
    val dayNames = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
    
    Column(modifier = modifier) {
        // Day names header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dayNames.forEach { dayName ->
                Text(
                    text = dayName,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Calendar grid
        var dayIndex = 1
        var weekIndex = 0
        
        while (dayIndex <= daysInMonth) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (dayOfWeek in 1..7) {
                    if (weekIndex == 0 && dayOfWeek < firstDayOfWeek) {
                        // Empty cells before first day
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    } else if (dayIndex <= daysInMonth) {
                        val dayTimestamp = monthStart + (dayIndex - 1) * 24L * 60 * 60 * 1000
                        val isCompleted = completions.any { completion ->
                            isSameDay(completion, dayTimestamp)
                        }
                        val isToday = isSameDay(today, dayTimestamp)
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .background(
                                    color = when {
                                        isCompleted -> habitColor.copy(alpha = 0.8f)
                                        isToday -> habitColor.copy(alpha = 0.2f)
                                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    },
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$dayIndex",
                                fontSize = 10.sp,
                                color = if (isCompleted || isToday) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                }
                            )
                        }
                        dayIndex++
                    } else {
                        // Empty cells after last day
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
            weekIndex++
        }
    }
}

private fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
    val cal1 = Calendar.getInstance()
    cal1.timeInMillis = timestamp1
    val cal2 = Calendar.getInstance()
    cal2.timeInMillis = timestamp2
    
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

