package com.erman.battery.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

interface SettingsStore {
    val isOngoingNotificationEnabled: Boolean
    var isChargeAlarmEnabled: Boolean

    var chargeAlarmLimit: Int?

    val themeMode: ThemeMode
}

class PreferenceSettingsStore(context: Context) : SettingsStore {

    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    override val isOngoingNotificationEnabled: Boolean
        get() = preferences.getBoolean(KEY_ONGOING_NOTIFICATION, false)

    override var isChargeAlarmEnabled: Boolean
        get() = preferences.getBoolean(KEY_CHARGE_ALARM_ENABLED, false)
        set(value) = preferences.edit { putBoolean(KEY_CHARGE_ALARM_ENABLED, value) }

    override var chargeAlarmLimit: Int?
        get() = preferences.getInt(KEY_CHARGE_ALARM_LIMIT, NO_LIMIT).takeIf { it != NO_LIMIT }
        set(value) = preferences.edit {
            if (value == null) remove(KEY_CHARGE_ALARM_LIMIT) else putInt(KEY_CHARGE_ALARM_LIMIT, value)
        }

    override val themeMode: ThemeMode
        get() = ThemeMode.fromValue(preferences.getString(KEY_THEME, null))

    companion object {
        const val KEY_ONGOING_NOTIFICATION = "ongoing_notification"
        const val KEY_THEME = "theme"
        private const val KEY_CHARGE_ALARM_ENABLED = "charge_alarm_enabled"
        private const val KEY_CHARGE_ALARM_LIMIT = "charge_alarm_limit"
        private const val NO_LIMIT = -1
    }
}
