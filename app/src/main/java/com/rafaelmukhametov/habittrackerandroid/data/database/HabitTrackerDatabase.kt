package com.rafaelmukhametov.habittrackerandroid.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rafaelmukhametov.habittrackerandroid.data.dao.HabitCompletionDao
import com.rafaelmukhametov.habittrackerandroid.data.dao.HabitDao
import com.rafaelmukhametov.habittrackerandroid.data.dao.HabitReminderDao
import com.rafaelmukhametov.habittrackerandroid.data.model.HabitCompletionEntity
import com.rafaelmukhametov.habittrackerandroid.data.model.HabitEntity
import com.rafaelmukhametov.habittrackerandroid.data.model.HabitReminderEntity

@Database(
    entities = [HabitEntity::class, HabitCompletionEntity::class, HabitReminderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HabitTrackerDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao
    abstract fun habitReminderDao(): HabitReminderDao
    
    companion object {
        const val DATABASE_NAME = "habit_tracker_db"
    }
}

