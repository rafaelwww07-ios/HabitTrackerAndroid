package com.rafaelmukhametov.habittrackerandroid.service

import com.rafaelmukhametov.habittrackerandroid.domain.model.Achievement
import com.rafaelmukhametov.habittrackerandroid.domain.model.AchievementType
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import java.util.Calendar

object AchievementService {
    fun checkAchievements(habit: Habit, allHabits: List<Habit>, existingAchievements: List<Achievement>): List<Achievement> {
        val achievements = mutableListOf<Achievement>()
        val streak = habit.currentStreak()
        
        if (streak >= 7 && !hasAchievement(AchievementType.WEEK_STREAK, habit.id, existingAchievements)) {
            achievements.add(Achievement(type = AchievementType.WEEK_STREAK, habitId = habit.id, value = 7))
        }
        
        if (streak >= 30 && !hasAchievement(AchievementType.MONTH_STREAK, habit.id, existingAchievements)) {
            achievements.add(Achievement(type = AchievementType.MONTH_STREAK, habitId = habit.id, value = 30))
        }
        
        if (streak >= 90 && !hasAchievement(AchievementType.QUARTER_STREAK, habit.id, existingAchievements)) {
            achievements.add(Achievement(type = AchievementType.QUARTER_STREAK, habitId = habit.id, value = 90))
        }
        
        if (streak >= 365 && !hasAchievement(AchievementType.YEAR_STREAK, habit.id, existingAchievements)) {
            achievements.add(Achievement(type = AchievementType.YEAR_STREAK, habitId = habit.id, value = 365))
        }
        
        if (habit.completions.size >= 100 && !hasAchievement(AchievementType.HUNDRED_COMPLETIONS, habit.id, existingAchievements)) {
            achievements.add(Achievement(type = AchievementType.HUNDRED_COMPLETIONS, habitId = habit.id, value = 100))
        }
        
        if (hasPerfectWeek(habit) && !hasAchievement(AchievementType.PERFECT_WEEK, habit.id, existingAchievements)) {
            achievements.add(Achievement(type = AchievementType.PERFECT_WEEK, habitId = habit.id))
        }
        
        if (hasPerfectMonth(habit) && !hasAchievement(AchievementType.PERFECT_MONTH, habit.id, existingAchievements)) {
            achievements.add(Achievement(type = AchievementType.PERFECT_MONTH, habitId = habit.id))
        }
        
        return achievements
    }
    
    fun checkGlobalAchievements(allHabits: List<Habit>, existingAchievements: List<Achievement>): List<Achievement> {
        val achievements = mutableListOf<Achievement>()
        
        if (allHabits.isNotEmpty() && !existingAchievements.any { it.type == AchievementType.FIRST_HABIT }) {
            achievements.add(Achievement(type = AchievementType.FIRST_HABIT))
        }
        
        return achievements
    }
    
    private fun hasAchievement(type: AchievementType, habitId: String?, achievements: List<Achievement>): Boolean {
        return achievements.any { it.type == type && it.habitId == habitId }
    }
    
    private fun hasPerfectWeek(habit: Habit): Boolean {
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val weekStart = calendar.timeInMillis
        
        val today = System.currentTimeMillis()
        val weekCompletions = habit.completions.filter { completion ->
            completion.completedAt >= weekStart && completion.completedAt <= today
        }
        
        return weekCompletions.size >= habit.goalValue
    }
    
    private fun hasPerfectMonth(habit: Habit): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val monthStart = calendar.timeInMillis
        
        val today = System.currentTimeMillis()
        val monthCompletions = habit.completions.filter { completion ->
            completion.completedAt >= monthStart && completion.completedAt <= today
        }
        
        if (habit.goalType.ordinal == 0) { // DAYS_PER_WEEK
            val weeksInMonth = 4
            val expectedCompletions = habit.goalValue * weeksInMonth
            return monthCompletions.size >= expectedCompletions
        } else {
            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            return monthCompletions.size >= daysInMonth
        }
    }
}

