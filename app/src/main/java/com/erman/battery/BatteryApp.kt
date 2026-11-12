package com.erman.battery

import android.app.Application
import com.erman.battery.data.PreferenceSettingsStore
import com.erman.battery.ui.apply

class BatteryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceSettingsStore(this).themeMode.apply()
    }
}
