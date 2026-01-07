package com.rafaelmukhametov.habittrackerandroid.data.dao

import androidx.room.*
import com.rafaelmukhametov.habittrackerandroid.data.model.HabitReminderEntity

@Dao
interface HabitReminderDao {
    @Query("SELECT * FROM habit_reminders WHERE habitId = :habitId")
    suspend fun getRemindersForHabit(habitId: String): List<HabitReminderEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: HabitReminderEntity)
    
    @Delete
    suspend fun deleteReminder(reminder: HabitReminderEntity)
    
    @Query("DELETE FROM habit_reminders WHERE habitId = :habitId")
    suspend fun deleteRemindersForHabit(habitId: String)
}





