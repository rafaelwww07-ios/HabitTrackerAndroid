package com.rafaelmukhametov.habittrackerandroid.data.dao

import androidx.room.*
import com.rafaelmukhametov.habittrackerandroid.data.model.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitCompletionDao {
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY completedAt DESC")
    fun getCompletionsForHabitFlow(habitId: String): Flow<List<HabitCompletionEntity>>
    
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY completedAt DESC")
    suspend fun getCompletionsForHabit(habitId: String): List<HabitCompletionEntity>
    
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId")
    suspend fun getAllCompletionsForHabit(habitId: String): List<HabitCompletionEntity>
    
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND completedAt >= :startDate AND completedAt < :endDate ORDER BY completedAt ASC")
    suspend fun getCompletionsInRange(habitId: String, startDate: Long, endDate: Long): List<HabitCompletionEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: HabitCompletionEntity)
    
    @Delete
    suspend fun deleteCompletion(completion: HabitCompletionEntity)
    
    @Query("DELETE FROM habit_completions WHERE habitId = :habitId AND completedAt >= :startDate AND completedAt < :endDate")
    suspend fun deleteCompletionsInRange(habitId: String, startDate: Long, endDate: Long)
    
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId")
    suspend fun getAllCompletions(habitId: String): List<HabitCompletionEntity>
}

