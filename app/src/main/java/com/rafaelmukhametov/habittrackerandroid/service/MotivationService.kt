package com.rafaelmukhametov.habittrackerandroid.service

import java.util.Calendar

data class MotivationalQuote(
    val text: String,
    val author: String? = null
)

object MotivationService {
    private val quotes = listOf(
        MotivationalQuote("Успех — это сумма небольших усилий, повторяемых изо дня в день.", "Роберт Коллье"),
        MotivationalQuote("Привычка — это вторая натура.", "Аристотель"),
        MotivationalQuote("Не сдавайся. Обычно ключ поворачивается на последней попытке."),
        MotivationalQuote("Лучшее время для посадки дерева было 20 лет назад. Следующее лучшее время — сейчас.", "Китайская мудрость"),
        MotivationalQuote("Постоянство — это секрет успеха."),
        MotivationalQuote("Мы то, что мы делаем постоянно. Совершенство, следовательно, не действие, а привычка.", "Аристотель"),
        MotivationalQuote("Маленькие изменения со временем приводят к большим результатам."),
        MotivationalQuote("Не ждите идеального момента. Начните прямо сейчас."),
        MotivationalQuote("Победа принадлежит тем, кто настойчив."),
        MotivationalQuote("Путь в тысячу миль начинается с одного шага.", "Лао-цзы"),
        MotivationalQuote("Каждый день — это новый шанс стать лучше."),
        MotivationalQuote("Привычки формируют характер, характер определяет судьбу."),
        MotivationalQuote("Успех — это не случайность. Это результат подготовки, упорного труда и извлечения уроков из неудач.", "Колин Пауэлл"),
        MotivationalQuote("Верь в себя и все, что ты есть. Знай, что внутри тебя есть что-то большее, чем любое препятствие."),
        MotivationalQuote("Прогресс, а не совершенство.")
    )
    
    fun randomQuote(): MotivationalQuote {
        return quotes.random()
    }
    
    fun quoteOfTheDay(): MotivationalQuote {
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val index = dayOfYear % quotes.size
        return quotes[index]
    }
    
    fun messageForStreak(streak: Int): String {
        return when {
            streak == 0 -> "Начните свой путь к успеху сегодня!"
            streak < 3 -> "Отличное начало! Продолжайте в том же духе!"
            streak < 7 -> "Вы на правильном пути! Еще немного!"
            streak < 14 -> "Неделя подряд! Это впечатляет!"
            streak < 30 -> "Две недели! Вы формируете настоящую привычку!"
            streak < 90 -> "Месяц подряд! Вы молодец!"
            else -> "Невероятно! Вы настоящий мастер дисциплины!"
        }
    }
}

