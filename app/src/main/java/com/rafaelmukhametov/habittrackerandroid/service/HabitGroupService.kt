package com.rafaelmukhametov.habittrackerandroid.service

import android.content.Context
import android.content.SharedPreferences
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitGroup

class HabitGroupService(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("habit_groups", Context.MODE_PRIVATE)
    private val groupsKey = "habitGroups"
    
    fun getAllGroups(): List<HabitGroup> {
        val json = prefs.getString(groupsKey, null) ?: return emptyList()
        // Упрощенная версия - можно использовать Gson
        return emptyList() // TODO: Implement JSON deserialization
    }
    
    fun saveGroups(groups: List<HabitGroup>) {
        // TODO: Implement JSON serialization
    }
    
    fun createGroup(group: HabitGroup) {
        val groups = getAllGroups().toMutableList()
        groups.add(group)
        saveGroups(groups)
    }
    
    fun deleteGroup(groupId: String) {
        val groups = getAllGroups().toMutableList()
        groups.removeAll { it.id == groupId }
        saveGroups(groups)
    }
    
    fun updateGroup(group: HabitGroup) {
        val groups = getAllGroups().toMutableList()
        val index = groups.indexOfFirst { it.id == group.id }
        if (index >= 0) {
            groups[index] = group
            saveGroups(groups)
        }
    }
    
    fun addHabitToGroup(habitId: String, groupId: String) {
        val groups = getAllGroups().toMutableList()
        val index = groups.indexOfFirst { it.id == groupId }
        if (index >= 0 && !groups[index].habitIds.contains(habitId)) {
            groups[index] = groups[index].copy(
                habitIds = groups[index].habitIds + habitId
            )
            saveGroups(groups)
        }
    }
    
    fun removeHabitFromGroup(habitId: String, groupId: String) {
        val groups = getAllGroups().toMutableList()
        val index = groups.indexOfFirst { it.id == groupId }
        if (index >= 0) {
            groups[index] = groups[index].copy(
                habitIds = groups[index].habitIds.filter { it != habitId }
            )
            saveGroups(groups)
        }
    }
}

