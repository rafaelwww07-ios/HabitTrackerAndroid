package com.rafaelmukhametov.habittrackerandroid.data.dao

import androidx.room.*
import com.rafaelmukhametov.habittrackerandroid.data.model.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY createdAt DESC")
    fun getAllHabitsFlow(): Flow<List<HabitEntity>>
    
    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY createdAt DESC")
    suspend fun getAllHabits(): List<HabitEntity>
    
    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: String): HabitEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)
    
    @Update
    suspend fun updateHabit(habit: HabitEntity)
    
    @Delete
    suspend fun deleteHabit(habit: HabitEntity)
    
    @Query("SELECT * FROM habits WHERE isArchived = 1 ORDER BY createdAt DESC")
    suspend fun getArchivedHabits(): List<HabitEntity>
}





