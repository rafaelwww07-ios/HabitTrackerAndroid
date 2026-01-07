package com.rafaelmukhametov.habittrackerandroid.domain.model

import java.util.UUID

enum class AchievementType(val title: String, val description: String, val iconEmoji: String) {
    FIRST_HABIT("First Habit", "Create your first habit", "⭐"),
    WEEK_STREAK("Week Streak", "Keep a streak for 7 consecutive days", "🔥"),
    MONTH_STREAK("Month Streak", "Keep a streak for 30 consecutive days", "🔥"),
    QUARTER_STREAK("Quarter Streak", "Keep a streak for 90 consecutive days", "🔥"),
    YEAR_STREAK("Year Streak", "Keep a streak for 365 consecutive days", "🔥"),
    PERFECT_WEEK("Perfect Week", "Complete all goals in a week", "✅"),
    PERFECT_MONTH("Perfect Month", "Complete all goals in a month", "✅"),
    HUNDRED_COMPLETIONS("Hundred Completions", "Complete a habit 100 times", "💯")
}

data class Achievement(
    val id: String = UUID.randomUUID().toString(),
    val type: AchievementType,
    val habitId: String? = null,
    val unlockedAt: Long = System.currentTimeMillis(),
    val value: Int? = null
)



