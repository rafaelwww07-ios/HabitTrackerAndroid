package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    
    // Get strings in composable context
    val pdfExportComingSoon = stringResource(R.string.pdf_export_coming_soon)
    val csvFileCreated = stringResource(R.string.csv_file_created)
    val errorCreatingCsv = stringResource(R.string.error_creating_csv)
    val jsonFileCreated = stringResource(R.string.json_file_created)
    val errorCreatingJson = stringResource(R.string.error_creating_json)
    val calendarExportComingSoon = stringResource(R.string.calendar_export_coming_soon)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (habit == null) stringResource(R.string.export_options) else stringResource(R.string.export_habit),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
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
                SettingsSection(title = stringResource(R.string.files)) {
                    SettingsItem(
                        title = stringResource(R.string.export_to_pdf),
                        subtitle = stringResource(R.string.pdf_report),
                        onClick = {
                            // TODO: Implement PDF export
                            exportMessage = pdfExportComingSoon
                            showAlert = true
                        }
                    )
                    SettingsItem(
                        title = stringResource(R.string.export_to_csv),
                        subtitle = stringResource(R.string.table_format),
                        onClick = {
                            val csv = exportService.exportToCSV(habitsToExport)
                            val file = exportService.saveFile(csv, "habits_${System.currentTimeMillis()}.csv")
                            exportMessage = if (file != null) csvFileCreated else errorCreatingCsv
                            showAlert = true
                        }
                    )
                    SettingsItem(
                        title = stringResource(R.string.export_to_json),
                        subtitle = stringResource(R.string.json_format),
                        onClick = {
                            val json = exportService.exportToJSON(habitsToExport)
                            val file = exportService.saveFile(json, "habits_${System.currentTimeMillis()}.json")
                            exportMessage = if (file != null) jsonFileCreated else errorCreatingJson
                            showAlert = true
                        }
                    )
                }
            }
            
            item {
                SettingsSection(title = stringResource(R.string.integrations)) {
                    SettingsItem(
                        title = stringResource(R.string.export_to_calendar),
                        subtitle = stringResource(R.string.add_to_calendar),
                        onClick = {
                            // TODO: Implement calendar export
                            exportMessage = calendarExportComingSoon
                            showAlert = true
                        }
                    )
                }
            }
        }
        
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text(stringResource(R.string.export_title)) },
                text = { Text(exportMessage ?: "") },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text(stringResource(R.string.ok))
                    }
                }
            )
        }
    }
}


