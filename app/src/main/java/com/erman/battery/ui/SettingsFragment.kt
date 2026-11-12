package com.erman.battery.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.erman.battery.R
import com.erman.battery.data.PreferenceSettingsStore
import com.erman.battery.service.BatteryMonitor

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            PreferenceSettingsStore.KEY_ONGOING_NOTIFICATION -> BatteryMonitor(requireContext()).sync()
            PreferenceSettingsStore.KEY_THEME -> PreferenceSettingsStore(requireContext()).themeMode.apply()
        }
    }
}
