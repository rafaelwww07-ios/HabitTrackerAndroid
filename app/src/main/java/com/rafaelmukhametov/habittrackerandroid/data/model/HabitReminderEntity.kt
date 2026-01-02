package com.rafaelmukhametov.habittrackerandroid.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rafaelmukhametov.habittrackerandroid.data.converter.DaysOfWeekConverter

@Entity(
    tableName = "habit_reminders",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("habitId")]
)
@TypeConverters(DaysOfWeekConverter::class)
data class HabitReminderEntity(
    @PrimaryKey
    val id: String,
    val habitId: String,
    val time: Long, // Time in milliseconds since midnight
    val daysOfWeek: String, // Comma-separated string: "1,2,3" (1=Sunday, 2=Monday, etc.)
    val isEnabled: Boolean = true
)

