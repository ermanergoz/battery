package com.erman.battery.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.erman.battery.data.ThemeMode
import com.erman.battery.ui.settings.SettingsScreen
import com.erman.battery.ui.theme.BatteryTheme

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BatteryTheme {
                val isOngoingNotificationEnabled by viewModel.isOngoingNotificationEnabled.observeAsState(false)
                val themeMode by viewModel.themeMode.observeAsState(ThemeMode.SYSTEM)

                SettingsScreen(
                    isOngoingNotificationEnabled = isOngoingNotificationEnabled,
                    themeMode = themeMode,
                    onOngoingNotificationChanged = viewModel::onOngoingNotificationChanged,
                    onThemeModeChanged = viewModel::onThemeModeChanged,
                    onBack = ::finish
                )
            }
        }
    }
}
