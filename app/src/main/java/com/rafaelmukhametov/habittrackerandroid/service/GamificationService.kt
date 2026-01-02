package com.rafaelmukhametov.habittrackerandroid.service

import android.content.Context
import android.content.SharedPreferences
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitCompletion
import com.rafaelmukhametov.habittrackerandroid.domain.model.UserProgress
import java.util.Calendar

class GamificationService(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("gamification", Context.MODE_PRIVATE)
    private val progressKey = "userProgress"
    
    fun getUserProgress(): UserProgress {
        val totalPoints = prefs.getInt("totalPoints", 0)
        val level = prefs.getInt("level", 1)
        val daysTracked = prefs.getInt("daysTracked", 0)
        val totalCompletions = prefs.getInt("totalCompletions", 0)
        val longestStreak = prefs.getInt("longestStreak", 0)
        val badgesString = prefs.getString("badgesEarned", "") ?: ""
        val badgesEarned = if (badgesString.isEmpty()) {
            emptySet()
        } else {
            badgesString.split(",").toSet()
        }
        
        return UserProgress(
            totalPoints = totalPoints,
            level = level,
            daysTracked = daysTracked,
            totalCompletions = totalCompletions,
            longestStreak = longestStreak,
            badgesEarned = badgesEarned
        )
    }
    
    fun saveUserProgress(progress: UserProgress) {
        prefs.edit()
            .putInt("totalPoints", progress.totalPoints)
            .putInt("level", progress.level)
            .putInt("daysTracked", progress.daysTracked)
            .putInt("totalCompletions", progress.totalCompletions)
            .putInt("longestStreak", progress.longestStreak)
            .putString("badgesEarned", progress.badgesEarned.joinToString(","))
            .apply()
    }
    
    fun calculatePoints(habit: Habit, completion: HabitCompletion): Int {
        var points = PointsReward.DAILY_COMPLETION.points
        
        val streak = habit.currentStreak()
        if (streak > 0) {
            points += PointsReward.STREAK_DAY.points
            
            if (streak % 7 == 0) {
                points += PointsReward.STREAK_WEEK.points
            }
            if (streak % 30 == 0) {
                points += PointsReward.STREAK_MONTH.points
            }
        }
        
        if (hasPerfectWeek(habit)) {
            points += PointsReward.PERFECT_WEEK.points
        }
        
        return points
    }
    
    fun updateProgressAfterCompletion(habit: Habit, completion: HabitCompletion) {
        val progress = getUserProgress()
        val points = calculatePoints(habit, completion)
        val updatedProgress = progress.copy(
            totalPoints = progress.totalPoints + points,
            totalCompletions = progress.totalCompletions + 1,
            longestStreak = maxOf(progress.longestStreak, habit.currentStreak())
        )
        saveUserProgress(updatedProgress)
    }
    
    fun checkAndAwardAchievements(habit: Habit): UserProgress {
        var progress = getUserProgress()
        val streak = habit.currentStreak()
        val badges = progress.badgesEarned.toMutableSet()
        
        if (streak >= 7 && !badges.contains("WEEK_STREAK")) {
            badges.add("WEEK_STREAK")
            progress = progress.copy(
                totalPoints = progress.totalPoints + PointsReward.ACHIEVEMENT.points,
                badgesEarned = badges
            )
        }
        
        if (streak >= 30 && !badges.contains("MONTH_STREAK")) {
            badges.add("MONTH_STREAK")
            progress = progress.copy(
                totalPoints = progress.totalPoints + PointsReward.ACHIEVEMENT.points,
                badgesEarned = badges
            )
        }
        
        if (habit.completions.size >= 100 && !badges.contains("HUNDRED_COMPLETIONS")) {
            badges.add("HUNDRED_COMPLETIONS")
            progress = progress.copy(
                totalPoints = progress.totalPoints + PointsReward.ACHIEVEMENT.points,
                badgesEarned = badges
            )
        }
        
        saveUserProgress(progress)
        return progress
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
        val weekCompletions = habit.completions.count { completion ->
            completion.completedAt >= weekStart && completion.completedAt <= today
        }
        
        return weekCompletions >= habit.goalValue
    }
}

enum class PointsReward(val points: Int) {
    DAILY_COMPLETION(10),
    STREAK_DAY(5),
    STREAK_WEEK(50),
    STREAK_MONTH(200),
    PERFECT_WEEK(100),
    ACHIEVEMENT(500)
}
