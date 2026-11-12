package com.erman.battery.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erman.battery.data.PreferenceSettingsStore
import com.erman.battery.data.SettingsStore
import com.erman.battery.data.ThemeMode
import com.erman.battery.service.BatteryMonitor

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settings: SettingsStore = PreferenceSettingsStore(application)
    private val monitor = BatteryMonitor(application)

    private val _isOngoingNotificationEnabled = MutableLiveData(settings.isOngoingNotificationEnabled)
    val isOngoingNotificationEnabled: LiveData<Boolean> = _isOngoingNotificationEnabled

    private val _themeMode = MutableLiveData(settings.themeMode)
    val themeMode: LiveData<ThemeMode> = _themeMode

    fun onOngoingNotificationChanged(enabled: Boolean) {
        settings.isOngoingNotificationEnabled = enabled
        _isOngoingNotificationEnabled.value = enabled
        monitor.sync()
    }

    fun onThemeModeChanged(mode: ThemeMode) {
        settings.themeMode = mode
        _themeMode.value = mode
        mode.apply()
    }
}
