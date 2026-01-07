package com.rafaelmukhametov.habittrackerandroid.service

import android.content.Context
import android.content.SharedPreferences
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitTrigger

class TriggerService(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("triggers", Context.MODE_PRIVATE)
    private val triggersKey = "triggers"
    
    fun getAllTriggers(): List<HabitTrigger> {
        val json = prefs.getString(triggersKey, null) ?: return emptyList()
        // Упрощенная версия - можно использовать Gson
        return emptyList() // TODO: Implement JSON deserialization
    }
    
    fun saveTriggers(triggers: List<HabitTrigger>) {
        // TODO: Implement JSON serialization
    }
    
    fun createTrigger(trigger: HabitTrigger) {
        val triggers = getAllTriggers().toMutableList()
        triggers.add(trigger)
        saveTriggers(triggers)
    }
    
    fun deleteTrigger(triggerId: String) {
        val triggers = getAllTriggers().toMutableList()
        triggers.removeAll { it.id == triggerId }
        saveTriggers(triggers)
    }
    
    fun updateTrigger(trigger: HabitTrigger) {
        val triggers = getAllTriggers().toMutableList()
        val index = triggers.indexOfFirst { it.id == trigger.id }
        if (index >= 0) {
            triggers[index] = trigger
            saveTriggers(triggers)
        }
    }
}





