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
    THIRTY_DAYS("30-дневный вызов", "30 дней подряд без пропусков", 30),
    PERFECT_WEEK("Идеальная неделя", "7 дней идеального выполнения", 7),
    MORNING_ROUTINE("Утренний распорядок", "Утренние привычки 7 дней", 7),
    EVENING_ROUTINE("Вечерний распорядок", "Вечерние привычки 7 дней", 7),
    WEEKEND_WARRIOR("Выходной воин", "Активность на выходных", 14);
    
    companion object {
        fun values(): Array<ChallengeTemplate> = arrayOf(
            THIRTY_DAYS, PERFECT_WEEK, MORNING_ROUTINE, EVENING_ROUTINE, WEEKEND_WARRIOR
        )
    }
}
