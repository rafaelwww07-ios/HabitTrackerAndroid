package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rafaelmukhametov.habittrackerandroid.R
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.ui.component.HabitCard
import com.rafaelmukhametov.habittrackerandroid.ui.component.ProgressCalendarView
import com.rafaelmukhametov.habittrackerandroid.ui.util.getIconImageVector
import com.rafaelmukhametov.habittrackerandroid.ui.util.formatDate
import com.rafaelmukhametov.habittrackerandroid.ui.viewmodel.HabitDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailView(
    habit: Habit,
    repository: com.rafaelmukhametov.habittrackerandroid.data.repository.HabitRepository,
    onBack: () -> Unit
) {
    val viewModel: HabitDetailViewModel = viewModel(
        factory = HabitDetailViewModelFactory(repository, habit)
    )
    
    val currentHabit by viewModel.habit.collectAsState()
    val monthlyCompletions by viewModel.monthlyCompletions.collectAsState()
    val weeklyStats by viewModel.weeklyStats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val habitColor = Color(android.graphics.Color.parseColor(currentHabit.colorHex))
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentHabit.name, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.toggleCompletion() }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (currentHabit.isCompletedToday()) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = habitColor)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(R.string.completed_short), color = habitColor)
                            } else {
                                Text(stringResource(R.string.mark_complete_short), color = habitColor)
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .background(
                        Color.White.copy(alpha = 0.1f)
                            .copy(red = habitColor.red * 0.1f, green = habitColor.green * 0.1f, blue = habitColor.blue * 0.1f)
                    )
            ) {
                // Header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = getIconImageVector(currentHabit.iconName),
                            contentDescription = currentHabit.name,
                            tint = habitColor,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (currentHabit.description.isNotEmpty()) {
                            Text(
                                text = currentHabit.description,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
                
                // Stats
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = stringResource(R.string.current_streak),
                        value = "${viewModel.currentStreak}",
                        icon = "🔥",
                        color = Color(0xFFFF6B35),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = stringResource(R.string.best_streak),
                        value = "${viewModel.bestStreak}",
                        icon = "⭐",
                        color = Color(0xFFFFD700),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = stringResource(R.string.success),
                        value = "${viewModel.successRate.toInt()}%",
                        icon = "✅",
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Calendar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.progress_calendar),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ProgressCalendarView(
                            completions = monthlyCompletions,
                            habitColor = habitColor
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Weekly Stats
                if (weeklyStats.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.weekly_progress),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            SimpleBarChart(data = weeklyStats, color = habitColor)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Recent Completions
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.recent_completions),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        val recentCompletions = currentHabit.completions.sortedByDescending { it.completedAt }.take(10)
                        if (recentCompletions.isEmpty()) {
                            Text(
                                text = stringResource(R.string.no_completions),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        } else {
                            recentCompletions.forEach { completion ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = habitColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = formatDate(completion.completedAt),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun SimpleBarChart(data: List<Double>, color: Color, modifier: Modifier = Modifier) {
    val maxValue = data.maxOrNull() ?: 100.0
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { value ->
            val height = (value / maxValue).coerceIn(0.1, 1.0)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 2.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(height.toFloat())
                        .background(
                            color = color.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        )
                )
            }
        }
    }
}


class HabitDetailViewModelFactory(
    private val repository: com.rafaelmukhametov.habittrackerandroid.data.repository.HabitRepository,
    private val habit: Habit
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitDetailViewModel(repository, habit) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

