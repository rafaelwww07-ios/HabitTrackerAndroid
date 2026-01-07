package com.rafaelmukhametov.habittrackerandroid.domain.model

data class HabitTemplate(
    val name: String,
    val description: String,
    val iconName: String,
    val colorHex: String,
    val category: HabitCategory,
    val goalType: GoalType,
    val goalValue: Int
) {
    fun toHabit(): Habit {
        return Habit(
            name = name,
            description = description,
            colorHex = colorHex,
            iconName = iconName,
            category = category,
            goalType = goalType,
            goalValue = goalValue
        )
    }
    
    companion object {
        val templates = listOf(
            HabitTemplate(
                name = "Morning Exercise",
                description = "15-30 minutes of physical exercise",
                iconName = "directions_run",
                colorHex = "#FF6B6B",
                category = HabitCategory.FITNESS,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 5
            ),
            HabitTemplate(
                name = "Reading",
                description = "Read books for self-development",
                iconName = "menu_book",
                colorHex = "#4ECDC4",
                category = HabitCategory.LEARNING,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 7
            ),
            HabitTemplate(
                name = "Meditation",
                description = "Calming meditation",
                iconName = "eco",
                colorHex = "#95E1D3",
                category = HabitCategory.PERSONAL,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 7
            ),
            HabitTemplate(
                name = "Drink Water",
                description = "Drink 8 glasses of water",
                iconName = "opacity",
                colorHex = "#3498DB",
                category = HabitCategory.HEALTH,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 7
            ),
            HabitTemplate(
                name = "Daily Planning",
                description = "Make a plan for the day",
                iconName = "create",
                colorHex = "#9B59B6",
                category = HabitCategory.WORK,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 5
            ),
            HabitTemplate(
                name = "Language Learning",
                description = "Practice a foreign language",
                iconName = "menu_book",
                colorHex = "#E74C3C",
                category = HabitCategory.LEARNING,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 6
            ),
            HabitTemplate(
                name = "Sleep 8 Hours",
                description = "Healthy sleep",
                iconName = "nights_stay",
                colorHex = "#34495E",
                category = HabitCategory.HEALTH,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 7
            ),
            HabitTemplate(
                name = "Walking",
                description = "Walk in the fresh air",
                iconName = "directions_run",
                colorHex = "#2ECC71",
                category = HabitCategory.HEALTH,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 5
            ),
            HabitTemplate(
                name = "Journaling",
                description = "Write down thoughts and events",
                iconName = "menu_book",
                colorHex = "#F39C12",
                category = HabitCategory.PERSONAL,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 5
            ),
            HabitTemplate(
                name = "Social Media Detox",
                description = "Don't use social media until evening",
                iconName = "psychology",
                colorHex = "#E67E22",
                category = HabitCategory.PERSONAL,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 5
            )
        )
    }
}



