package com.rafaelmukhametov.habittrackerandroid

import android.app.Application
import com.rafaelmukhametov.habittrackerandroid.data.database.DatabaseModule
import com.rafaelmukhametov.habittrackerandroid.data.database.HabitTrackerDatabase

class HabitTrackerApplication : Application() {
    val database: HabitTrackerDatabase by lazy {
        DatabaseModule.provideDatabase(this)
    }
}

