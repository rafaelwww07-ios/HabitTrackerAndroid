package com.rafaelmukhametov.habittrackerandroid.domain.model

import java.util.UUID
import java.util.Calendar

data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val colorHex: String = "#007AFF",
    val iconName: String = "star.fill",
    val category: HabitCategory? = null,
    val goalType: GoalType = GoalType.DAYS_PER_WEEK,
    val goalValue: Int = 7,
    val createdAt: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false,
    val completions: List<HabitCompletion> = emptyList(),
    val reminders: List<HabitReminder> = emptyList()
) {
    fun isCompletedToday(): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val todayStart = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val todayEnd = calendar.timeInMillis
        
        return completions.any { completion ->
            completion.completedAt >= todayStart && completion.completedAt < todayEnd
        }
    }

    fun currentStreak(): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        
        val sortedCompletions = completions.sortedByDescending { it.completedAt }
        if (sortedCompletions.isEmpty()) return 0
        
        var streak = 0
        var currentDate = calendar.timeInMillis
        
        // If not completed today, start from yesterday
        if (!isCompletedToday()) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            currentDate = calendar.timeInMillis
        }
        
        for (completion in sortedCompletions) {
            val completionDate = completion.completedAt
            val completionDayStart = Calendar.getInstance().apply {
                timeInMillis = completionDate
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            
            val currentDayStart = Calendar.getInstance().apply {
                timeInMillis = currentDate
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            
            if (completionDayStart == currentDayStart) {
                streak++
                calendar.timeInMillis = currentDate
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                currentDate = calendar.timeInMillis
            } else if (completionDayStart < currentDayStart) {
                // Missed days - streak broken
                break
            }
        }
        
        return streak
    }

    fun weeklyCompletionPercentage(): Double {
        val calendar = Calendar.getInstance()
        val today = System.currentTimeMillis()
        
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val weekStart = calendar.timeInMillis
        
        val weekCompletions = completions.filter { completion ->
            completion.completedAt >= weekStart && completion.completedAt <= today
        }
        
        val daysCount = ((today - weekStart) / (24 * 60 * 60 * 1000L)).toInt()
        if (daysCount <= 0) return 0.0
        
        return (weekCompletions.size.toDouble() / goalValue.toDouble() * 100.0).coerceAtMost(100.0)
    }

    fun overallCompletionPercentage(): Double {
        val daysSinceCreation = ((System.currentTimeMillis() - createdAt) / (24 * 60 * 60 * 1000L)).toInt()
        if (daysSinceCreation <= 0) return 0.0
        
        return (completions.size.toDouble() / daysSinceCreation.toDouble() * 100.0).coerceAtMost(100.0)
    }
}

data class HabitCompletion(
    val id: String = UUID.randomUUID().toString(),
    val habitId: String,
    val completedAt: Long = System.currentTimeMillis(),
    val notes: String? = null
)

data class HabitReminder(
    val id: String = UUID.randomUUID().toString(),
    val habitId: String,
    val time: Long, // Time in milliseconds since midnight
    val daysOfWeek: Set<Int> = emptySet(), // 1=Sunday, 2=Monday, ..., 7=Saturday
    val isEnabled: Boolean = true
)

