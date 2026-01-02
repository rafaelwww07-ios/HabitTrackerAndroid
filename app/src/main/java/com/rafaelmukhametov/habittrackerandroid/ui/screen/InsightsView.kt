package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsView(
    habits: List<Habit>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Инсайты", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Main Insight
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEB3B).copy(alpha = 0.1f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💡", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Главный инсайт",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = generateMainInsight(habits),
                        fontSize = 14.sp
                    )
                }
            }
            
            // Recommendations
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Рекомендации",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    generateRecommendations(habits).take(5).forEach { recommendation ->
                        InsightCard(
                            icon = "⭐",
                            title = recommendation,
                            color = Color(0xFF2196F3)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            
            // Patterns
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Паттерны",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    detectPatterns(habits).take(3).forEach { pattern ->
                        InsightCard(
                            icon = "📊",
                            title = pattern,
                            color = Color(0xFF9C27B0)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            
            // Predictions
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Прогнозы",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    generatePredictions(habits).take(3).forEach { prediction ->
                        InsightCard(
                            icon = "🔮",
                            title = prediction,
                            color = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun InsightCard(
    icon: String,
    title: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun generateMainInsight(habits: List<Habit>): String {
    if (habits.isEmpty()) {
        return "Начните отслеживать привычки, чтобы получить персональные инсайты!"
    }
    
    val totalStreak = habits.sumOf { it.currentStreak() }
    val averageStreak = totalStreak.toDouble() / habits.size
    
    return when {
        averageStreak >= 7 -> "🎉 Отличная работа! Вы поддерживаете средний стрик ${averageStreak.toInt()} дней. Это показывает высокий уровень дисциплины!"
        averageStreak >= 3 -> "Вы на правильном пути! Средний стрик ${averageStreak.toInt()} дней - хорошее начало. Попробуйте довести его до недели."
        else -> "Начните регулярно отслеживать привычки, чтобы увидеть реальный прогресс. Небольшие шаги каждый день приведут к большим результатам!"
    }
}

private fun generateRecommendations(habits: List<Habit>): List<String> {
    val recommendations = mutableListOf<String>()
    
    habits.forEach { habit ->
        val streak = habit.currentStreak()
        val successRate = habit.overallCompletionPercentage()
        
        when {
            streak == 0 && successRate < 50 -> {
                recommendations.add("Попробуйте установить напоминание для \"${habit.name}\" - это поможет не забывать о привычке")
            }
            successRate < 70 -> {
                recommendations.add("Для \"${habit.name}\" попробуйте снизить цель или выбрать более реалистичное время выполнения")
            }
            streak >= 7 -> {
                recommendations.add("\"${habit.name}\" идет отлично! Подумайте о добавлении новой связанной привычки")
            }
        }
    }
    
    if (recommendations.isEmpty()) {
        recommendations.add("Отличная работа! Продолжайте в том же духе")
    }
    
    return recommendations
}

private fun detectPatterns(habits: List<Habit>): List<String> {
    val patterns = mutableListOf<String>()
    val calendar = Calendar.getInstance()
    val weekdayCompletions = mutableMapOf<Int, Int>()
    
    habits.forEach { habit ->
        habit.completions.forEach { completion ->
            calendar.timeInMillis = completion.completedAt
            val weekday = calendar.get(Calendar.DAY_OF_WEEK)
            weekdayCompletions[weekday] = (weekdayCompletions[weekday] ?: 0) + 1
        }
    }
    
    weekdayCompletions.maxByOrNull { it.value }?.let { (day, _) ->
        val dayNames = listOf("", "Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб")
        patterns.add("Вы наиболее активны в ${dayNames.getOrNull(day) ?: "неизвестный день"}")
    }
    
    val morningCompletions = habits.flatMap { it.completions }.count { completion ->
        calendar.timeInMillis = completion.completedAt
        calendar.get(Calendar.HOUR_OF_DAY) < 12
    }
    
    val totalCompletions = habits.sumOf { it.completions.size }
    if (totalCompletions > 0) {
        val morningPercentage = (morningCompletions.toDouble() / totalCompletions) * 100
        when {
            morningPercentage > 60 -> patterns.add("Вы предпочитаете выполнять привычки утром (${morningPercentage.toInt()}%)")
            morningPercentage < 30 -> patterns.add("Большинство ваших привычек выполняется вечером (${(100 - morningPercentage).toInt()}%)")
        }
    }
    
    return patterns
}

private fun generatePredictions(habits: List<Habit>): List<String> {
    val predictions = mutableListOf<String>()
    
    habits.forEach { habit ->
        val streak = habit.currentStreak()
        val successRate = habit.overallCompletionPercentage()
        
        if (streak >= 3 && successRate > 75) {
            predictions.add("При текущем темпе, \"${habit.name}\" достигнет 30-дневного стрика через ${maxOf(1, 30 - streak)} дней")
        }
    }
    
    if (predictions.isEmpty()) {
        predictions.add("Продолжайте отслеживать привычки для получения прогнозов")
    }
    
    return predictions
}

