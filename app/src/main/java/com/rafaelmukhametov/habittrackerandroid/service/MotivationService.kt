package com.rafaelmukhametov.habittrackerandroid.service

import java.util.Calendar

data class MotivationalQuote(
    val text: String,
    val author: String? = null
)

object MotivationService {
    private val quotes = listOf(
        MotivationalQuote("Success is the sum of small efforts, repeated day in and day out.", "Robert Collier"),
        MotivationalQuote("Habit is second nature.", "Aristotle"),
        MotivationalQuote("Don't give up. Usually the key turns on the last try."),
        MotivationalQuote("The best time to plant a tree was 20 years ago. The next best time is now.", "Chinese Proverb"),
        MotivationalQuote("Consistency is the secret to success."),
        MotivationalQuote("We are what we repeatedly do. Excellence, then, is not an act, but a habit.", "Aristotle"),
        MotivationalQuote("Small changes over time lead to big results."),
        MotivationalQuote("Don't wait for the perfect moment. Start right now."),
        MotivationalQuote("Victory belongs to those who are persistent."),
        MotivationalQuote("A journey of a thousand miles begins with a single step.", "Lao Tzu"),
        MotivationalQuote("Every day is a new chance to be better."),
        MotivationalQuote("Habits form character, character determines destiny."),
        MotivationalQuote("Success is no accident. It is the result of preparation, hard work, and learning from failure.", "Colin Powell"),
        MotivationalQuote("Believe in yourself and all that you are. Know that there is something inside you greater than any obstacle."),
        MotivationalQuote("Progress, not perfection.")
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
            streak == 0 -> "Start your journey to success today!"
            streak < 3 -> "Great start! Keep it up!"
            streak < 7 -> "You're on the right track! Keep going!"
            streak < 14 -> "A week in a row! That's impressive!"
            streak < 30 -> "Two weeks! You're forming a real habit!"
            streak < 90 -> "A month in a row! You're doing great!"
            else -> "Incredible! You're a true master of discipline!"
        }
    }
}



