package com.rafaelmukhametov.habittrackerandroid.domain.model

import java.util.UUID

enum class TriggerCondition(val description: String) {
    COMPLETED("When habit is completed"),
    NOT_COMPLETED("When habit is not completed"),
    STREAK_REACHED("When a certain streak is reached"),
    STREAK_BROKEN("When streak is broken")
}

data class HabitTrigger(
    val id: String = UUID.randomUUID().toString(),
    val triggerHabitId: String, // Привычка-триггер
    val targetHabitId: String, // Привычка-цель
    val condition: TriggerCondition,
    val isEnabled: Boolean = true
)



