package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitCategory
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitTemplate
import com.rafaelmukhametov.habittrackerandroid.ui.util.getIconImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplatesView(
    onSelectTemplate: (HabitTemplate) -> Unit,
    onBack: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    
    val filteredTemplates = if (searchText.isEmpty()) {
        HabitTemplate.templates
    } else {
        HabitTemplate.templates.filter { template ->
            template.name.contains(searchText, ignoreCase = true) ||
            template.description.contains(searchText, ignoreCase = true)
        }
    }
    
    val groupedTemplates = filteredTemplates.groupBy { it.category }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Шаблоны привычек", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
        ) {
            // Search
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Поиск шаблонов...") },
                leadingIcon = {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HabitCategory.values().forEach { category ->
                    val templates = groupedTemplates[category]
                    if (templates != null && templates.isNotEmpty()) {
                        item {
                            Text(
                                text = category.displayName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        
                        items(templates) { template ->
                            TemplateRowView(
                                template = template,
                                onSelect = { onSelectTemplate(template) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TemplateRowView(
    template: HabitTemplate,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        onClick = onSelect
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = Color(android.graphics.Color.parseColor(template.colorHex)).copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getIconImageVector(template.iconName),
                    contentDescription = template.name,
                    tint = Color(android.graphics.Color.parseColor(template.colorHex)),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = template.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = template.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Icon(
                Icons.Default.Add,
                contentDescription = "Добавить",
                tint = Color(0xFF2196F3)
            )
        }
    }
}


