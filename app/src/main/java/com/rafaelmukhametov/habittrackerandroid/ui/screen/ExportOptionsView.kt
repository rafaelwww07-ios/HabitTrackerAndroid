package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.service.ExportService
import com.rafaelmukhametov.habittrackerandroid.ui.util.SettingsSection
import androidx.compose.ui.platform.LocalContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportOptionsView(
    habit: Habit?,
    allHabits: List<Habit>,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val exportService = remember { ExportService(context) }
    
    var exportMessage by remember { mutableStateOf<String?>(null) }
    var showAlert by remember { mutableStateOf(false) }
    
    val habitsToExport = if (habit != null) listOf(habit) else allHabits
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (habit == null) "Экспорт данных" else "Экспорт привычки",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                SettingsSection(title = "Файлы") {
                    SettingsItem(
                        title = "Экспорт в PDF",
                        subtitle = "PDF отчет",
                        onClick = {
                            // TODO: Implement PDF export
                            exportMessage = "PDF экспорт будет реализован"
                            showAlert = true
                        }
                    )
                    SettingsItem(
                        title = "Экспорт в CSV",
                        subtitle = "Табличный формат",
                        onClick = {
                            val csv = exportService.exportToCSV(habitsToExport)
                            val file = exportService.saveFile(csv, "habits_${System.currentTimeMillis()}.csv")
                            exportMessage = if (file != null) "CSV файл создан успешно" else "Ошибка создания CSV"
                            showAlert = true
                        }
                    )
                    SettingsItem(
                        title = "Экспорт в JSON",
                        subtitle = "JSON формат",
                        onClick = {
                            val json = exportService.exportToJSON(habitsToExport)
                            val file = exportService.saveFile(json, "habits_${System.currentTimeMillis()}.json")
                            exportMessage = if (file != null) "JSON файл создан успешно" else "Ошибка создания JSON"
                            showAlert = true
                        }
                    )
                }
            }
            
            item {
                SettingsSection(title = "Интеграции") {
                    SettingsItem(
                        title = "Экспорт в календарь",
                        subtitle = "Добавить в календарь",
                        onClick = {
                            // TODO: Implement calendar export
                            exportMessage = "Экспорт в календарь будет реализован"
                            showAlert = true
                        }
                    )
                }
            }
        }
        
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text("Экспорт") },
                text = { Text(exportMessage ?: "") },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}


