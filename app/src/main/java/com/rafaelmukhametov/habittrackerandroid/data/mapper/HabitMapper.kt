package com.rafaelmukhametov.habittrackerandroid.data.mapper

import com.rafaelmukhametov.habittrackerandroid.data.model.HabitCompletionEntity
import com.rafaelmukhametov.habittrackerandroid.data.model.HabitEntity
import com.rafaelmukhametov.habittrackerandroid.data.model.HabitReminderEntity
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitCategory
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitCompletion
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitReminder
import com.rafaelmukhametov.habittrackerandroid.domain.model.GoalType

fun HabitEntity.toDomain(completions: List<HabitCompletion>, reminders: List<HabitReminder>): Habit {
    val category = category?.let { 
        HabitCategory.values().find { cat -> cat.displayName == it }
    }
    
    return Habit(
        id = id,
        name = name,
        description = description,
        colorHex = colorHex,
        iconName = iconName,
        category = category,
        goalType = GoalType.values().getOrNull(goalType) ?: GoalType.DAYS_PER_WEEK,
        goalValue = goalValue,
        createdAt = createdAt,
        isArchived = isArchived,
        completions = completions,
        reminders = reminders
    )
}

fun Habit.toEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        name = name,
        description = description,
        colorHex = colorHex,
        iconName = iconName,
        category = category?.displayName,
        goalType = goalType.ordinal,
        goalValue = goalValue,
        createdAt = createdAt,
        isArchived = isArchived
    )
}

fun HabitCompletionEntity.toDomain(): HabitCompletion {
    return HabitCompletion(
        id = id,
        habitId = habitId,
        completedAt = completedAt,
        notes = notes
    )
}

fun HabitCompletion.toEntity(): HabitCompletionEntity {
    return HabitCompletionEntity(
        id = id,
        habitId = habitId,
        completedAt = completedAt,
        notes = notes
    )
}

fun HabitReminderEntity.toDomain(): HabitReminder {
    val daysSet = if (daysOfWeek.isEmpty()) {
        emptySet()
    } else {
        daysOfWeek.split(",").mapNotNull { it.toIntOrNull() }.toSet()
    }
    
    return HabitReminder(
        id = id,
        habitId = habitId,
        time = time,
        daysOfWeek = daysSet,
        isEnabled = isEnabled
    )
}

fun HabitReminder.toEntity(): HabitReminderEntity {
    return HabitReminderEntity(
        id = id,
        habitId = habitId,
        time = time,
        daysOfWeek = daysOfWeek.joinToString(","),
        isEnabled = isEnabled
    )
}

