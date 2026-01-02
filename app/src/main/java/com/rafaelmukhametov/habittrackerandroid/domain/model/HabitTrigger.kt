package com.rafaelmukhametov.habittrackerandroid.domain.model

import java.util.UUID

enum class TriggerCondition(val description: String) {
    COMPLETED("Когда привычка выполнена"),
    NOT_COMPLETED("Когда привычка не выполнена"),
    STREAK_REACHED("Когда достигнут определенный стрик"),
    STREAK_BROKEN("Когда стрик прерван")
}

data class HabitTrigger(
    val id: String = UUID.randomUUID().toString(),
    val triggerHabitId: String, // Привычка-триггер
    val targetHabitId: String, // Привычка-цель
    val condition: TriggerCondition,
    val isEnabled: Boolean = true
)

