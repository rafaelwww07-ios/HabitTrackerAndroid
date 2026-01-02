package com.rafaelmukhametov.habittrackerandroid.data.repository

import com.rafaelmukhametov.habittrackerandroid.data.dao.HabitCompletionDao
import com.rafaelmukhametov.habittrackerandroid.data.dao.HabitDao
import com.rafaelmukhametov.habittrackerandroid.data.dao.HabitReminderDao
import com.rafaelmukhametov.habittrackerandroid.data.mapper.toDomain
import com.rafaelmukhametov.habittrackerandroid.data.mapper.toEntity
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import java.util.Calendar

class HabitRepository(
    private val habitDao: HabitDao,
    private val completionDao: HabitCompletionDao,
    private val reminderDao: HabitReminderDao
) {
    fun getAllHabitsFlow(): Flow<List<Habit>> {
        return habitDao.getAllHabitsFlow().flatMapLatest { entities ->
            flow {
                val habits = entities.map { entity ->
                    val completions = completionDao.getAllCompletionsForHabit(entity.id)
                    val reminders = reminderDao.getRemindersForHabit(entity.id)
                    entity.toDomain(
                        completions.map { it.toDomain() },
                        reminders.map { it.toDomain() }
                    )
                }
                emit(habits)
            }
        }.flowOn(Dispatchers.IO)
    }
    
    suspend fun getAllHabits(): List<Habit> {
        val entities = habitDao.getAllHabits()
        return entities.map { entity ->
            val completions = completionDao.getCompletionsForHabit(entity.id)
            val reminders = reminderDao.getRemindersForHabit(entity.id)
            entity.toDomain(
                completions.map { it.toDomain() },
                reminders.map { it.toDomain() }
            )
        }
    }
    
    suspend fun getHabitById(id: String): Habit? {
        val entity = habitDao.getHabitById(id) ?: return null
        val completions = completionDao.getCompletionsForHabit(id)
        val reminders = reminderDao.getRemindersForHabit(id)
        return entity.toDomain(
            completions.map { it.toDomain() },
            reminders.map { it.toDomain() }
        )
    }
    
    suspend fun saveHabit(habit: Habit) {
        val entity = habit.toEntity()
        habitDao.insertHabit(entity)
        
        // Update completions
        val existingCompletions = completionDao.getAllCompletions(habit.id)
        existingCompletions.forEach { completionDao.deleteCompletion(it) }
        habit.completions.forEach { completion ->
            completionDao.insertCompletion(completion.toEntity())
        }
        
        // Update reminders
        reminderDao.deleteRemindersForHabit(habit.id)
        habit.reminders.forEach { reminder ->
            reminderDao.insertReminder(reminder.toEntity())
        }
    }
    
    suspend fun deleteHabit(habit: Habit) {
        val entity = habit.toEntity()
        habitDao.deleteHabit(entity)
    }
    
    suspend fun toggleCompletion(habitId: String, date: Long = System.currentTimeMillis()) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.timeInMillis
        
        val existingCompletions = completionDao.getCompletionsInRange(habitId, startOfDay, endOfDay)
        
        if (existingCompletions.isEmpty()) {
            // Create new completion
            val completion = com.rafaelmukhametov.habittrackerandroid.domain.model.HabitCompletion(
                habitId = habitId,
                completedAt = startOfDay
            )
            completionDao.insertCompletion(completion.toEntity())
        } else {
            // Delete existing completion
            existingCompletions.forEach { completionDao.deleteCompletion(it) }
        }
    }
    
    suspend fun getCompletionsInRange(habitId: String, startDate: Long, endDate: Long): List<com.rafaelmukhametov.habittrackerandroid.domain.model.HabitCompletion> {
        val entities = completionDao.getCompletionsInRange(habitId, startDate, endDate)
        return entities.map { it.toDomain() }
    }
}

