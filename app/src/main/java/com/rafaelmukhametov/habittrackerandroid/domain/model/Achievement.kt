package com.rafaelmukhametov.habittrackerandroid.domain.model

import java.util.UUID

enum class AchievementType(val title: String, val description: String, val iconEmoji: String) {
    FIRST_HABIT("Первая привычка", "Создайте свою первую привычку", "⭐"),
    WEEK_STREAK("Неделя подряд", "Держите стрик 7 дней подряд", "🔥"),
    MONTH_STREAK("Месяц подряд", "Держите стрик 30 дней подряд", "🔥"),
    QUARTER_STREAK("Квартал подряд", "Держите стрик 90 дней подряд", "🔥"),
    YEAR_STREAK("Год подряд", "Держите стрик 365 дней подряд", "🔥"),
    PERFECT_WEEK("Идеальная неделя", "Выполните все цели на неделе", "✅"),
    PERFECT_MONTH("Идеальный месяц", "Выполните все цели в месяце", "✅"),
    HUNDRED_COMPLETIONS("Сотня выполнений", "Выполните привычку 100 раз", "💯")
}

data class Achievement(
    val id: String = UUID.randomUUID().toString(),
    val type: AchievementType,
    val habitId: String? = null,
    val unlockedAt: Long = System.currentTimeMillis(),
    val value: Int? = null
)

