package com.rafaelmukhametov.habittrackerandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rafaelmukhametov.habittrackerandroid.data.repository.HabitRepository
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch

class HabitListViewModel(
    private val repository: HabitRepository,
    private val context: android.content.Context? = null
) : ViewModel() {
    
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _showingCreateHabit = MutableStateFlow(false)
    val showingCreateHabit: StateFlow<Boolean> = _showingCreateHabit.asStateFlow()
    
    private val _editingHabit = MutableStateFlow<Habit?>(null)
    val editingHabit: StateFlow<Habit?> = _editingHabit.asStateFlow()
    
    init {
        loadHabits()
    }
    
    fun loadHabits() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllHabitsFlow()
                .catch { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
                .collect { habitsList ->
                    _habits.value = habitsList
                    _isLoading.value = false
                }
        }
    }
    
    fun refreshHabits() {
        viewModelScope.launch {
            try {
                val habitsList = repository.getAllHabits()
                _habits.value = habitsList
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
    
    fun toggleCompletion(habit: Habit) {
        viewModelScope.launch {
            try {
                val wasCompleted = habit.isCompletedToday()
                repository.toggleCompletion(habit.id)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
    
    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                repository.deleteHabit(habit)
                // Remove notifications if context is available
                context?.let {
                    val notificationService = com.rafaelmukhametov.habittrackerandroid.service.NotificationService(it)
                    notificationService.removeNotifications(habit.id)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
    
    fun showCreateHabit() {
        _editingHabit.value = null
        _showingCreateHabit.value = true
    }
    
    fun editHabit(habit: Habit) {
        _editingHabit.value = habit
        _showingCreateHabit.value = true
    }
    
    fun hideCreateHabit() {
        _showingCreateHabit.value = false
        _editingHabit.value = null
    }
    
    fun saveHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                repository.saveHabit(habit)
                // Schedule notifications if context is available
                context?.let {
                    val notificationService = com.rafaelmukhametov.habittrackerandroid.service.NotificationService(it)
                    notificationService.scheduleNotifications(habit)
                }
                hideCreateHabit()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
    
    fun archiveHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                val updatedHabit = habit.copy(isArchived = true)
                repository.saveHabit(updatedHabit)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
    
    fun unarchiveHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                val updatedHabit = habit.copy(isArchived = false)
                repository.saveHabit(updatedHabit)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}

class HabitListViewModelFactory(
    private val repository: HabitRepository,
    private val context: android.content.Context? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitListViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

