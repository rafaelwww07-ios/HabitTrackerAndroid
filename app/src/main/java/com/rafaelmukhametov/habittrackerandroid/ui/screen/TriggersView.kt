package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitTrigger
import com.rafaelmukhametov.habittrackerandroid.domain.model.TriggerCondition
import com.rafaelmukhametov.habittrackerandroid.service.TriggerService
import com.rafaelmukhametov.habittrackerandroid.ui.util.getIconImageVector
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriggersView(
    habits: List<Habit>,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val triggerService = remember { TriggerService(context) }
    
    var triggers by remember { mutableStateOf<List<HabitTrigger>>(emptyList()) }
    var showCreateTrigger by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        triggers = triggerService.getAllTriggers()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Триггеры", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { showCreateTrigger = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Создать триггер")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (triggers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("⚡", fontSize = 60.sp)
                    Text(
                        text = "Нет триггеров",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Создайте триггер, чтобы автоматически напоминать о связанных привычках",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(triggers) { trigger ->
                    TriggerRowView(
                        trigger = trigger,
                        habits = habits
                    )
                }
            }
        }
    }
}

@Composable
fun TriggerRowView(
    trigger: HabitTrigger,
    habits: List<Habit>
) {
    val triggerHabit = habits.find { it.id == trigger.triggerHabitId }
    val targetHabit = habits.find { it.id == trigger.targetHabitId }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = getIconImageVector(triggerHabit?.iconName ?: ""),
                        contentDescription = triggerHabit?.name,
                        tint = if (triggerHabit != null) Color(android.graphics.Color.parseColor(triggerHabit.colorHex)) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = triggerHabit?.name ?: "Неизвестная привычка",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (!trigger.isEnabled) {
                    Text("⏸️", fontSize = 20.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("→", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = trigger.condition.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = getIconImageVector(targetHabit?.iconName ?: ""),
                    contentDescription = targetHabit?.name,
                    tint = if (targetHabit != null) Color(android.graphics.Color.parseColor(targetHabit.colorHex)) else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = targetHabit?.name ?: "Неизвестная привычка",
                    fontSize = 14.sp
                )
            }
        }
    }
}


