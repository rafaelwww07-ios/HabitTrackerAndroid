package com.rafaelmukhametov.habittrackerandroid.data.converter

import androidx.room.TypeConverter

class DaysOfWeekConverter {
    @TypeConverter
    fun fromDaysOfWeek(days: Set<Int>): String {
        return days.joinToString(",")
    }

    @TypeConverter
    fun toDaysOfWeek(daysString: String): Set<Int> {
        return if (daysString.isEmpty()) {
            emptySet()
        } else {
            daysString.split(",").mapNotNull { it.toIntOrNull() }.toSet()
        }
    }
}





