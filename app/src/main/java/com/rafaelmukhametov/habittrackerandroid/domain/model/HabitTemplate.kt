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
                name = "Утренняя зарядка",
                description = "15-30 минут физических упражнений",
                iconName = "directions_run",
                colorHex = "#FF6B6B",
                category = HabitCategory.FITNESS,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 5
            ),
            HabitTemplate(
                name = "Чтение",
                description = "Читать книги для саморазвития",
                iconName = "menu_book",
                colorHex = "#4ECDC4",
                category = HabitCategory.LEARNING,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 7
            ),
            HabitTemplate(
                name = "Медитация",
                description = "Успокаивающая медитация",
                iconName = "eco",
                colorHex = "#95E1D3",
                category = HabitCategory.PERSONAL,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 7
            ),
            HabitTemplate(
                name = "Пить воду",
                description = "Выпивать 8 стаканов воды",
                iconName = "opacity",
                colorHex = "#3498DB",
                category = HabitCategory.HEALTH,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 7
            ),
            HabitTemplate(
                name = "Планирование дня",
                description = "Составлять план на день",
                iconName = "create",
                colorHex = "#9B59B6",
                category = HabitCategory.WORK,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 5
            ),
            HabitTemplate(
                name = "Изучение языка",
                description = "Практика иностранного языка",
                iconName = "menu_book",
                colorHex = "#E74C3C",
                category = HabitCategory.LEARNING,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 6
            ),
            HabitTemplate(
                name = "Спать 8 часов",
                description = "Здоровый сон",
                iconName = "nights_stay",
                colorHex = "#34495E",
                category = HabitCategory.HEALTH,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 7
            ),
            HabitTemplate(
                name = "Прогулка",
                description = "Прогулка на свежем воздухе",
                iconName = "directions_run",
                colorHex = "#2ECC71",
                category = HabitCategory.HEALTH,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 5
            ),
            HabitTemplate(
                name = "Вести дневник",
                description = "Записывать мысли и события",
                iconName = "menu_book",
                colorHex = "#F39C12",
                category = HabitCategory.PERSONAL,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 5
            ),
            HabitTemplate(
                name = "Отказ от социальных сетей",
                description = "Не использовать соцсети до вечера",
                iconName = "psychology",
                colorHex = "#E67E22",
                category = HabitCategory.PERSONAL,
                goalType = GoalType.DAYS_PER_WEEK,
                goalValue = 5
            )
        )
    }
}

