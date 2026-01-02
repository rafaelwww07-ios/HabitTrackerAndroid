package com.rafaelmukhametov.habittrackerandroid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rafaelmukhametov.habittrackerandroid.data.converter.GoalTypeConverter

@Entity(tableName = "habits")
@TypeConverters(GoalTypeConverter::class)
data class HabitEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String = "",
    val colorHex: String = "#007AFF",
    val iconName: String = "star.fill",
    val category: String? = null,
    val goalType: Int, // 0 = daysPerWeek, 1 = consecutiveDays
    val goalValue: Int,
    val createdAt: Long,
    val isArchived: Boolean = false
)

