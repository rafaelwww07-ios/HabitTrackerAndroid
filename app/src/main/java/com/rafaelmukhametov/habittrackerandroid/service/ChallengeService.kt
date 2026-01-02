package com.rafaelmukhametov.habittrackerandroid.service

import android.content.Context
import android.content.SharedPreferences
import com.rafaelmukhametov.habittrackerandroid.domain.model.Challenge
import com.rafaelmukhametov.habittrackerandroid.domain.model.ChallengeTemplate
import java.util.Calendar
import java.util.UUID

class ChallengeService(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("challenges", Context.MODE_PRIVATE)
    private val challengesKey = "challenges"
    
    fun getAllChallenges(): List<Challenge> {
        val json = prefs.getString(challengesKey, null) ?: return emptyList()
        // Упрощенная версия - можно использовать Gson
        return emptyList() // TODO: Implement JSON deserialization
    }
    
    fun saveChallenges(challenges: List<Challenge>) {
        // TODO: Implement JSON serialization
    }
    
    fun createChallenge(template: ChallengeTemplate, habitIds: List<String>): Challenge {
        return Challenge(
            name = template.displayName,
            description = template.description,
            duration = template.duration,
            habitIds = habitIds,
            startDate = System.currentTimeMillis()
        )
    }
    
    fun updateChallengeProgress(challengeId: String, completedDayId: String) {
        val challenges = getAllChallenges().toMutableList()
        val index = challenges.indexOfFirst { it.id == challengeId }
        if (index >= 0) {
            val challenge = challenges[index]
            if (!challenge.completions.contains(completedDayId)) {
                val updatedCompletions = challenge.completions + completedDayId
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = challenge.startDate
                val daysSinceStart = ((System.currentTimeMillis() - challenge.startDate) / (24 * 60 * 60 * 1000L)).toInt()
                val currentDay = minOf(daysSinceStart + 1, challenge.duration)
                
                val isCompleted = updatedCompletions.size >= challenge.duration
                
                challenges[index] = challenge.copy(
                    completions = updatedCompletions,
                    currentDay = currentDay,
                    isCompleted = isCompleted,
                    isActive = !isCompleted
                )
                saveChallenges(challenges)
            }
        }
    }
    
    fun getActiveChallenges(): List<Challenge> {
        return getAllChallenges().filter { it.isActive && !it.isCompleted }
    }
    
    fun getCompletedChallenges(): List<Challenge> {
        return getAllChallenges().filter { it.isCompleted }
    }
}

