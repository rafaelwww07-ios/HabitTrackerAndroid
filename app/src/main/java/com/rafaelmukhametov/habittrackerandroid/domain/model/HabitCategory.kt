package com.rafaelmukhametov.habittrackerandroid.domain.model

enum class HabitCategory(val stringResId: Int, val iconName: String) {
    HEALTH(com.rafaelmukhametov.habittrackerandroid.R.string.category_health, "favorite"),
    FITNESS(com.rafaelmukhametov.habittrackerandroid.R.string.category_fitness, "fitness_center"),
    LEARNING(com.rafaelmukhametov.habittrackerandroid.R.string.category_learning, "menu_book"),
    WORK(com.rafaelmukhametov.habittrackerandroid.R.string.category_work, "work"),
    PERSONAL(com.rafaelmukhametov.habittrackerandroid.R.string.category_personal, "person"),
    SOCIAL(com.rafaelmukhametov.habittrackerandroid.R.string.category_social, "group"),
    CREATIVITY(com.rafaelmukhametov.habittrackerandroid.R.string.category_creativity, "palette"),
    FINANCE(com.rafaelmukhametov.habittrackerandroid.R.string.category_finance, "attach_money"),
    OTHER(com.rafaelmukhametov.habittrackerandroid.R.string.category_other, "more_horiz")
}



