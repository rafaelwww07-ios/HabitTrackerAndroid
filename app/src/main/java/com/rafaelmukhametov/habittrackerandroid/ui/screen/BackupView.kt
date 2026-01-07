package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelmukhametov.habittrackerandroid.R
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
    
    // Get strings in composable context
    val createBackupSuccess = stringResource(R.string.create_backup_success)
    val backupErrorPrefix = stringResource(R.string.backup_error)
    val fileSaveError = stringResource(R.string.file_save_error)
    val backupSavedPrefix = stringResource(R.string.backup_saved)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.backup), fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
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
                        text = stringResource(R.string.backup),
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
                                alertMessage = createBackupSuccess
                                showAlert = true
                            } catch (e: Exception) {
                                alertMessage = "$backupErrorPrefix ${e.message ?: ""}"
                                showAlert = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text("☁️", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.create_backup))
                        }
                    }
                    
                    if (backupData != null) {
                        Button(
                            onClick = {
                                val filename = "HabitTracker_Backup_${System.currentTimeMillis()}.json"
                                val file = exportService.saveFile(backupData!!, filename)
                                alertMessage = if (file != null) {
                                    backupSavedPrefix.replace("%1\$s", file.name)
                                } else {
                                    fileSaveError
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
                                Text(stringResource(R.string.share_backup))
                            }
                        }
                    }
                    
                    Text(
                        text = stringResource(R.string.backup_description),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
        
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text(stringResource(R.string.backup_title)) },
                text = { Text(alertMessage ?: "") },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text(stringResource(R.string.ok))
                    }
                }
            )
        }
    }
}



