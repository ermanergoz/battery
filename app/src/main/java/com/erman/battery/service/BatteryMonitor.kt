package com.erman.battery.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.erman.battery.data.PreferenceSettingsStore
import com.erman.battery.data.SettingsStore

class BatteryMonitor(context: Context) {

    private val appContext = context.applicationContext
    private val settings: SettingsStore = PreferenceSettingsStore(appContext)

    fun sync() {
        val intent = Intent(appContext, BatteryService::class.java)
        if (settings.isOngoingNotificationEnabled || settings.isChargeAlarmEnabled) {
            ContextCompat.startForegroundService(appContext, intent)
        } else {
            appContext.stopService(intent)
        }
    }
}
