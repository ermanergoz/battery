package com.erman.battery.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erman.battery.R
import com.erman.battery.data.ThemeMode
import com.erman.battery.ui.components.SectionCard
import com.erman.battery.ui.labelRes
import com.erman.battery.ui.theme.BatteryTheme

@Composable
fun SettingsScreen(
    isOngoingNotificationEnabled: Boolean,
    themeMode: ThemeMode,
    onOngoingNotificationChanged: (Boolean) -> Unit,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onBack: () -> Unit
) {
    var isThemeDialogShown by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.navigate_up))
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = isOngoingNotificationEnabled,
                            role = Role.Switch,
                            onValueChange = onOngoingNotificationChanged
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SettingText(
                        title = stringResource(R.string.pref_ongoing_notification_title),
                        summary = stringResource(R.string.pref_ongoing_notification_summary),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(16.dp))
                    Switch(checked = isOngoingNotificationEnabled, onCheckedChange = null)
                }
            }
            SectionCard {
                SettingText(
                    title = stringResource(R.string.pref_theme_title),
                    summary = stringResource(themeMode.labelRes),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(role = Role.Button) { isThemeDialogShown = true }
                        .padding(vertical = 4.dp)
                )
            }
        }
    }

    if (isThemeDialogShown) {
        ThemeDialog(
            selected = themeMode,
            onSelect = {
                isThemeDialogShown = false
                onThemeModeChanged(it)
            },
            onDismiss = { isThemeDialogShown = false }
        )
    }
}

@Composable
private fun SettingText(title: String, summary: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(text = title, style = MaterialTheme.typography.subtitle1)
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(text = summary, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
private fun ThemeDialog(selected: ThemeMode, onSelect: (ThemeMode) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.pref_theme_title)) },
        text = {
            Column {
                ThemeMode.values().forEach { mode ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(selected = mode == selected, role = Role.RadioButton) { onSelect(mode) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = mode == selected, onClick = null)
                        Spacer(Modifier.width(16.dp))
                        Text(text = stringResource(mode.labelRes), style = MaterialTheme.typography.body1)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(android.R.string.cancel)) }
        },
        shape = MaterialTheme.shapes.large
    )
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    BatteryTheme { SettingsScreen(true, ThemeMode.SYSTEM, {}, {}, {}) }
}
