package com.rafaelmukhametov.habittrackerandroid.domain.model

data class UserProgress(
    val totalPoints: Int = 0,
    val level: Int = 1,
    val daysTracked: Int = 0,
    val totalCompletions: Int = 0,
    val longestStreak: Int = 0,
    val badgesEarned: Set<String> = emptySet()
) {
    val pointsToNextLevel: Int
        get() = 1000 - (totalPoints % 1000)
    
    val levelProgress: Double
        get() {
            val pointsInLevel = totalPoints % 1000
            return pointsInLevel.toDouble() / 1000.0
        }
}
