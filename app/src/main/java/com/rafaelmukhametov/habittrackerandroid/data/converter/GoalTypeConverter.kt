package com.rafaelmukhametov.habittrackerandroid.data.converter

import androidx.room.TypeConverter
import com.rafaelmukhametov.habittrackerandroid.domain.model.GoalType

class GoalTypeConverter {
    @TypeConverter
    fun fromGoalType(goalType: GoalType): Int {
        return goalType.ordinal
    }

    @TypeConverter
    fun toGoalType(ordinal: Int): GoalType {
        return GoalType.values()[ordinal]
    }
}





