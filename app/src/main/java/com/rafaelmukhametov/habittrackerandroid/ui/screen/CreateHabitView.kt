package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelmukhametov.habittrackerandroid.R
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitCategory
import com.rafaelmukhametov.habittrackerandroid.domain.model.GoalType
import com.rafaelmukhametov.habittrackerandroid.ui.util.getIconImageVector
import com.rafaelmukhametov.habittrackerandroid.ui.viewmodel.CreateHabitViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHabitView(
    viewModel: CreateHabitViewModel,
    onSave: (Habit) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(viewModel.name) }
    var description by remember { mutableStateOf(viewModel.description) }
    var selectedColorHex by remember { mutableStateOf(viewModel.selectedColorHex) }
    var selectedIconName by remember { mutableStateOf(viewModel.selectedIconName) }
    var selectedCategory by remember { mutableStateOf(viewModel.selectedCategory) }
    var goalType by remember { mutableStateOf(viewModel.goalType) }
    var goalValue by remember { mutableStateOf(viewModel.goalValue) }
    var isReminderEnabled by remember { mutableStateOf(viewModel.isReminderEnabled) }
    
    val isValid = name.trim().isNotEmpty() && goalValue > 0
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (viewModel.existingHabit == null) stringResource(R.string.new_habit) else stringResource(R.string.edit),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.name = name
                            viewModel.description = description
                            viewModel.selectedColorHex = selectedColorHex
                            viewModel.selectedIconName = selectedIconName
                            viewModel.selectedCategory = selectedCategory
                            viewModel.goalType = goalType
                            viewModel.goalValue = goalValue
                            viewModel.isReminderEnabled = isReminderEnabled
                            onSave(viewModel.saveHabit())
                        },
                        enabled = isValid
                    ) {
                        Text(stringResource(R.string.save), fontWeight = FontWeight.Bold)
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
                .padding(16.dp)
        ) {
            // Basic Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.basic_info), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(R.string.habit_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(stringResource(R.string.description_optional)) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Category Picker
                    Text(stringResource(R.string.category), fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text(stringResource(R.string.no_category)) }
                        )
                        
                        HabitCategory.values().take(4).forEach { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { 
                                    selectedCategory = if (selectedCategory == category) null else category
                                },
                                label = { Text(stringResource(category.stringResId), fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Appearance
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.appearance), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Color Picker
                    Text(stringResource(R.string.color), fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(6),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(80.dp)
                    ) {
                        items(viewModel.availableColors.size) { index ->
                            val color = viewModel.availableColors[index]
                            val isSelected = selectedColorHex == color
                            
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(android.graphics.Color.parseColor(color)))
                                    .clickable { selectedColorHex = color },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Color.Transparent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(Color.White.copy(alpha = 0.3f))
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Icon Picker
                    Text(stringResource(R.string.icon), fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(120.dp)
                    ) {
                        items(viewModel.availableIcons.size) { index ->
                            val iconName = viewModel.availableIcons[index]
                            val isSelected = selectedIconName == iconName
                            
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) 
                                            Color(android.graphics.Color.parseColor(selectedColorHex)).copy(alpha = 0.2f)
                                        else 
                                            MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .clickable { selectedIconName = iconName },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getIconImageVector(iconName),
                                    contentDescription = iconName,
                                    tint = if (isSelected) 
                                        Color(android.graphics.Color.parseColor(selectedColorHex))
                                    else 
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.size(24.dp)
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(
                                                Color(android.graphics.Color.parseColor(selectedColorHex)).copy(alpha = 0.1f)
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Goal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.goal), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = goalType == GoalType.DAYS_PER_WEEK,
                            onClick = { goalType = GoalType.DAYS_PER_WEEK },
                            label = { Text(stringResource(GoalType.DAYS_PER_WEEK.stringResId)) }
                        )
                        
                        FilterChip(
                            selected = goalType == GoalType.CONSECUTIVE_DAYS,
                            onClick = { goalType = GoalType.CONSECUTIVE_DAYS },
                            label = { Text(stringResource(GoalType.CONSECUTIVE_DAYS.stringResId)) }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.target_value, goalValue))
                        
                        Row {
                            IconButton(onClick = { if (goalValue > 1) goalValue-- }) {
                                Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { if (goalValue < 365) goalValue++ }) {
                                Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}


