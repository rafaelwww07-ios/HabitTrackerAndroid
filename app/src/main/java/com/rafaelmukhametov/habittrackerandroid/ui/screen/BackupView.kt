package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupView(
    habits: List<Habit>,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val exportService = remember { ExportService(context) }
    
    var backupData by remember { mutableStateOf<String?>(null) }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf<String?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Резервное копирование", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Резервное копирование",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Button(
                        onClick = {
                            try {
                                backupData = exportService.exportToJSON(habits)
                                // Save to SharedPreferences for quick access
                                context.getSharedPreferences("backup", android.content.Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("lastBackup", backupData)
                                    .putLong("lastBackupDate", System.currentTimeMillis())
                                    .apply()
                                alertMessage = "Резервная копия создана успешно"
                                showAlert = true
                            } catch (e: Exception) {
                                alertMessage = "Ошибка создания резервной копии: ${e.message}"
                                showAlert = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text("☁️", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Создать резервную копию")
                        }
                    }
                    
                    if (backupData != null) {
                        Button(
                            onClick = {
                                val filename = "HabitTracker_Backup_${System.currentTimeMillis()}.json"
                                val file = exportService.saveFile(backupData!!, filename)
                                alertMessage = if (file != null) {
                                    "Резервная копия сохранена: ${file.name}"
                                } else {
                                    "Ошибка сохранения файла"
                                }
                                showAlert = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                Text("📤", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Поделиться резервной копией")
                            }
                        }
                    }
                    
                    Text(
                        text = "Создайте резервную копию ваших данных, чтобы не потерять прогресс. Рекомендуется делать это регулярно.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
        
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text("Резервное копирование") },
                text = { Text(alertMessage ?: "") },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

