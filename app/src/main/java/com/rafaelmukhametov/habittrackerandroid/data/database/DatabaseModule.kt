package com.rafaelmukhametov.habittrackerandroid.data.database

import android.content.Context
import androidx.room.Room

object DatabaseModule {
    fun provideDatabase(context: Context): HabitTrackerDatabase {
        return Room.databaseBuilder(
            context,
            HabitTrackerDatabase::class.java,
            HabitTrackerDatabase.DATABASE_NAME
        ).build()
    }
}

