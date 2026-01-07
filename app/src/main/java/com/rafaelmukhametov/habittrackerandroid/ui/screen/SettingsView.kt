package com.rafaelmukhametov.habittrackerandroid.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelmukhametov.habittrackerandroid.R
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.ui.util.SettingsSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    habits: List<Habit>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings), fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
                SettingsSection(title = stringResource(R.string.appearance_settings)) {
                    // Theme picker would go here
                    SettingsItem(
                        title = stringResource(R.string.theme),
                        subtitle = stringResource(R.string.system_default),
                        onClick = { /* TODO: Show theme picker */ }
                    )
                }
            }
            
            item {
                SettingsSection(title = stringResource(R.string.data_settings)) {
                    SettingsItem(
                        title = stringResource(R.string.export_data),
                        subtitle = stringResource(R.string.export_data_subtitle),
                        onClick = { /* TODO: Show export options */ }
                    )
                    SettingsItem(
                        title = stringResource(R.string.import_data),
                        subtitle = stringResource(R.string.import_data_subtitle),
                        onClick = { /* TODO: Show import options */ }
                    )
                    SettingsItem(
                        title = stringResource(R.string.backup_data),
                        subtitle = stringResource(R.string.backup_data_subtitle),
                        onClick = { /* TODO: Show backup options */ }
                    )
                }
            }
            
            item {
                SettingsSection(title = stringResource(R.string.about_app)) {
                    SettingsItem(
                        title = stringResource(R.string.version),
                        subtitle = "1.0.0",
                        onClick = null
                    )
                }
            }
        }
    }
}


@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    onClick: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        if (onClick != null) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.rotate(180f)
            )
        }
    }
}

