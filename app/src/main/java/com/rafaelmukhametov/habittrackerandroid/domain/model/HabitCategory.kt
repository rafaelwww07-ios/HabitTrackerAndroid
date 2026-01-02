package com.rafaelmukhametov.habittrackerandroid.domain.model

enum class HabitCategory(val displayName: String, val iconName: String) {
    HEALTH("Здоровье", "favorite"),
    FITNESS("Фитнес", "fitness_center"),
    LEARNING("Обучение", "menu_book"),
    WORK("Работа", "work"),
    PERSONAL("Личное", "person"),
    SOCIAL("Социальное", "group"),
    CREATIVITY("Творчество", "palette"),
    FINANCE("Финансы", "attach_money"),
    OTHER("Другое", "more_horiz")
}

