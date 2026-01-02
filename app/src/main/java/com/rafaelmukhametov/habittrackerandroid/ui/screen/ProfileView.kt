package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.service.GamificationService
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    habits: List<Habit>,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val gamificationService = remember { GamificationService(context) }
    var progress by remember { mutableStateOf(gamificationService.getUserProgress()) }
    
    LaunchedEffect(habits) {
        // Update progress based on habits
        val totalDays = habits.flatMap { it.completions }
            .map { completion ->
                val cal = Calendar.getInstance()
                cal.timeInMillis = completion.completedAt
                cal.get(Calendar.DAY_OF_YEAR)
            }
            .distinct()
            .size
        
        val totalCompletions = habits.sumOf { it.completions.size }
        val longestStreak = habits.maxOfOrNull { it.currentStreak() } ?: 0
        
        progress = progress.copy(
            daysTracked = totalDays,
            totalCompletions = totalCompletions,
            longestStreak = maxOf(progress.longestStreak, longestStreak)
        )
        gamificationService.saveUserProgress(progress)
    }
    
    val levelProgress = remember(progress) {
        val pointsInLevel = progress.totalPoints % 1000
        pointsInLevel.toFloat() / 1000f
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = levelProgress,
        animationSpec = tween(600),
        label = "levelProgress"
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Level Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 12.dp.toPx()
                            val radius = (size.minDimension - strokeWidth) / 2
                            val center = Offset(size.width / 2, size.height / 2)
                            
                            // Background circle
                            drawCircle(
                                color = Color.Gray.copy(alpha = 0.2f),
                                radius = radius,
                                center = center,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                            
                            // Progress arc
                            drawArc(
                                color = Color(0xFF2196F3),
                                startAngle = -90f,
                                sweepAngle = animatedProgress * 360f,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                                size = Size(radius * 2, radius * 2),
                                topLeft = Offset(center.x - radius, center.y - radius)
                            )
                        }
                        
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Уровень",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${progress.level}",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "${progress.totalPoints} баллов",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "До следующего уровня:",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${1000 - (progress.totalPoints % 1000)} баллов",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            
            // Stats Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Статистика",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatBox(
                            title = "Всего дней",
                            value = "${progress.daysTracked}",
                            icon = "📅",
                            color = Color(0xFF2196F3),
                            modifier = Modifier.weight(1f)
                        )
                        StatBox(
                            title = "Выполнений",
                            value = "${progress.totalCompletions}",
                            icon = "✅",
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatBox(
                            title = "Лучший стрик",
                            value = "${progress.longestStreak}",
                            icon = "🔥",
                            color = Color(0xFFFF6B35),
                            modifier = Modifier.weight(1f)
                        )
                        StatBox(
                            title = "Бейджей",
                            value = "${progress.badgesEarned.size}",
                            icon = "🏆",
                            color = Color(0xFF9C27B0),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Badges Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Заработанные бейджи",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (progress.badgesEarned.isEmpty()) {
                        Text(
                            text = "Начните отслеживать привычки, чтобы заработать бейджи!",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            progress.badgesEarned.take(3).forEach { badge ->
                                BadgeView(
                                    badgeName = badge,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatBox(
    title: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun BadgeView(
    badgeName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = badgeColor(badgeName).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = badgeIcon(badgeName),
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = badgeTitle(badgeName),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun badgeIcon(name: String): String {
    return when (name) {
        "WEEK_STREAK" -> "🔥"
        "MONTH_STREAK" -> "🔥"
        "HUNDRED_COMPLETIONS" -> "💯"
        else -> "⭐"
    }
}

private fun badgeColor(name: String): Color {
    return when (name) {
        "WEEK_STREAK" -> Color(0xFFFF6B35)
        "MONTH_STREAK" -> Color(0xFFF44336)
        "HUNDRED_COMPLETIONS" -> Color(0xFF2196F3)
        else -> Color.Gray
    }
}

private fun badgeTitle(name: String): String {
    return when (name) {
        "WEEK_STREAK" -> "Неделя"
        "MONTH_STREAK" -> "Месяц"
        "HUNDRED_COMPLETIONS" -> "100 раз"
        else -> "Бейдж"
    }
}

