package com.rafaelmukhametov.habittrackerandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelmukhametov.habittrackerandroid.data.repository.HabitRepository
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class HabitDetailViewModel(
    private val repository: HabitRepository,
    initialHabit: Habit
) : ViewModel() {
    
    private val _habit = MutableStateFlow(initialHabit)
    val habit: StateFlow<Habit> = _habit.asStateFlow()
    
    private val _monthlyCompletions = MutableStateFlow<List<Long>>(emptyList())
    val monthlyCompletions: StateFlow<List<Long>> = _monthlyCompletions.asStateFlow()
    
    private val _weeklyStats = MutableStateFlow<List<Double>>(emptyList())
    val weeklyStats: StateFlow<List<Double>> = _weeklyStats.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    val currentStreak: Int
        get() = _habit.value.currentStreak()
    
    val successRate: Double
        get() = _habit.value.overallCompletionPercentage()
    
    val bestStreak: Int
        get() = calculateBestStreak()
    
    init {
        loadData()
    }
    
    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            loadMonthlyCompletions()
            loadWeeklyStats()
            refreshHabit()
            _isLoading.value = false
        }
    }
    
    fun toggleCompletion() {
        viewModelScope.launch {
            try {
                repository.toggleCompletion(_habit.value.id)
                refreshHabit()
                loadMonthlyCompletions()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    private suspend fun loadMonthlyCompletions() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val monthStart = calendar.timeInMillis
        
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val monthEnd = calendar.timeInMillis
        
        try {
            val completions = repository.getCompletionsInRange(_habit.value.id, monthStart, monthEnd)
            _monthlyCompletions.value = completions.map { it.completedAt }
        } catch (e: Exception) {
            _monthlyCompletions.value = emptyList()
        }
    }
    
    private suspend fun loadWeeklyStats() {
        val calendar = Calendar.getInstance()
        val stats = mutableListOf<Double>()
        
        for (weekOffset in 11 downTo 0) {
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.firstDayOfWeek = Calendar.MONDAY
            calendar.add(Calendar.WEEK_OF_YEAR, -weekOffset)
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val weekStart = calendar.timeInMillis
            
            calendar.add(Calendar.DAY_OF_MONTH, 6)
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            val weekEnd = calendar.timeInMillis
            
            try {
                val completions = repository.getCompletionsInRange(_habit.value.id, weekStart, weekEnd)
                val percentage = if (_habit.value.goalType.ordinal == 0) {
                    completions.size.toDouble() / _habit.value.goalValue.toDouble() * 100.0
                } else {
                    if (completions.size >= _habit.value.goalValue) 100.0 else 0.0
                }
                stats.add(percentage.coerceAtMost(100.0))
            } catch (e: Exception) {
                stats.add(0.0)
            }
        }
        
        _weeklyStats.value = stats
    }
    
    private suspend fun refreshHabit() {
        try {
            val updatedHabit = repository.getHabitById(_habit.value.id)
            if (updatedHabit != null) {
                _habit.value = updatedHabit
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    private fun calculateBestStreak(): Int {
        val sortedCompletions = _habit.value.completions.sortedBy { it.completedAt }
        if (sortedCompletions.isEmpty()) return 0
        
        val calendar = Calendar.getInstance()
        var maxStreak = 1
        var currentStreak = 1
        
        for (i in 1 until sortedCompletions.size) {
            calendar.timeInMillis = sortedCompletions[i - 1].completedAt
            val prevDay = calendar.get(Calendar.DAY_OF_YEAR)
            val prevYear = calendar.get(Calendar.YEAR)
            
            calendar.timeInMillis = sortedCompletions[i].completedAt
            val currDay = calendar.get(Calendar.DAY_OF_YEAR)
            val currYear = calendar.get(Calendar.YEAR)
            
            val daysDiff = if (currYear == prevYear) {
                currDay - prevDay
            } else {
                val daysInPrevYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
                (daysInPrevYear - prevDay) + currDay
            }
            
            if (daysDiff == 1) {
                currentStreak++
                maxStreak = maxOf(maxStreak, currentStreak)
            } else {
                currentStreak = 1
            }
        }
        
        return maxStreak
    }
}





