package com.rafaelmukhametov.habittrackerandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitCategory
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitReminder
import com.rafaelmukhametov.habittrackerandroid.domain.model.GoalType
import java.util.Calendar

class CreateHabitViewModel(
    existingHabit: Habit? = null
) : ViewModel() {
    
    var name: String = existingHabit?.name ?: ""
    var description: String = existingHabit?.description ?: ""
    var selectedColorHex: String = existingHabit?.colorHex ?: "#007AFF"
    var selectedIconName: String = existingHabit?.iconName ?: "star.fill"
    var selectedCategory: HabitCategory? = existingHabit?.category
    var goalType: GoalType = existingHabit?.goalType ?: GoalType.DAYS_PER_WEEK
    var goalValue: Int = existingHabit?.goalValue ?: 7
    var reminderTime: Long = existingHabit?.reminders?.firstOrNull()?.time 
        ?: (Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis % (24 * 60 * 60 * 1000))
    var reminderDays: Set<Int> = existingHabit?.reminders?.firstOrNull()?.daysOfWeek 
        ?: setOf(2, 3, 4, 5, 6) // Monday-Friday
    var isReminderEnabled: Boolean = existingHabit?.reminders?.firstOrNull()?.isEnabled ?: false
    
    val existingHabit: Habit? = existingHabit
    
    val isValid: Boolean
        get() = name.trim().isNotEmpty() && goalValue > 0
    
    val availableColors = listOf(
        "#FF0000", "#FF6B00", "#FFD700", "#00FF00", "#00CED1", 
        "#1E90FF", "#8A2BE2", "#FF1493", "#CD853F", "#808080"
    )
    
    val availableIcons = listOf(
        "star.fill", "heart.fill", "flame.fill", "leaf.fill",
        "figure.run", "dumbbell.fill", "book.fill", "pencil",
        "moon.fill", "sun.max.fill", "drop.fill", "airplane",
        "gamecontroller.fill", "music.note", "camera.fill", "brain.head.profile"
    )
    
    fun saveHabit(): Habit {
        val reminders = if (isReminderEnabled) {
            listOf(
                HabitReminder(
                    habitId = existingHabit?.id ?: "",
                    time = reminderTime,
                    daysOfWeek = reminderDays,
                    isEnabled = true
                )
            )
        } else {
            emptyList()
        }
        
        return if (existingHabit != null) {
            existingHabit.copy(
                name = name,
                description = description,
                colorHex = selectedColorHex,
                iconName = selectedIconName,
                category = selectedCategory,
                goalType = goalType,
                goalValue = goalValue,
                reminders = reminders
            )
        } else {
            Habit(
                name = name,
                description = description,
                colorHex = selectedColorHex,
                iconName = selectedIconName,
                category = selectedCategory,
                goalType = goalType,
                goalValue = goalValue,
                reminders = reminders
            )
        }
    }
}

