package com.rafaelmukhametov.habittrackerandroid.domain.model

import java.util.Calendar
import java.util.UUID

data class Challenge(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val duration: Int = 30,
    val habitIds: List<String> = emptyList(),
    val startDate: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val isCompleted: Boolean = false,
    val currentDay: Int = 1,
    val completions: List<String> = emptyList()
) {
    val progress: Double
        get() = if (duration > 0) completions.size.toDouble() / duration.toDouble() else 0.0
    
    val daysRemaining: Int
        get() = maxOf(0, duration - currentDay + 1)
    
    val endDate: Long
        get() {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = startDate
            calendar.add(Calendar.DAY_OF_MONTH, duration - 1)
            return calendar.timeInMillis
        }
}

enum class ChallengeTemplate(val displayName: String, val description: String, val duration: Int) {
    THIRTY_DAYS("30-Day Challenge", "30 consecutive days without skipping", 30),
    PERFECT_WEEK("Perfect Week", "7 days of perfect completion", 7),
    MORNING_ROUTINE("Morning Routine", "Morning habits for 7 days", 7),
    EVENING_ROUTINE("Evening Routine", "Evening habits for 7 days", 7),
    WEEKEND_WARRIOR("Weekend Warrior", "Activity on weekends", 14);
    
    companion object {
        fun values(): Array<ChallengeTemplate> = arrayOf(
            THIRTY_DAYS, PERFECT_WEEK, MORNING_ROUTINE, EVENING_ROUTINE, WEEKEND_WARRIOR
        )
    }
}
