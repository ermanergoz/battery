package com.erman.battery.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

interface SettingsStore {
    var isOngoingNotificationEnabled: Boolean
    var isChargeAlarmEnabled: Boolean

    var chargeAlarmLimit: Int?

    var themeMode: ThemeMode
}

class PreferenceSettingsStore(context: Context) : SettingsStore {

    // Same file the earlier preference screen used, so existing settings are kept
    private val preferences: SharedPreferences = context.applicationContext.getSharedPreferences(
        "${context.packageName}_preferences", Context.MODE_PRIVATE
    )

    override var isOngoingNotificationEnabled: Boolean
        get() = preferences.getBoolean(KEY_ONGOING_NOTIFICATION, false)
        set(value) = preferences.edit { putBoolean(KEY_ONGOING_NOTIFICATION, value) }

    override var isChargeAlarmEnabled: Boolean
        get() = preferences.getBoolean(KEY_CHARGE_ALARM_ENABLED, false)
        set(value) = preferences.edit { putBoolean(KEY_CHARGE_ALARM_ENABLED, value) }

    override var chargeAlarmLimit: Int?
        get() = preferences.getInt(KEY_CHARGE_ALARM_LIMIT, NO_LIMIT).takeIf { it != NO_LIMIT }
        set(value) = preferences.edit {
            if (value == null) remove(KEY_CHARGE_ALARM_LIMIT) else putInt(KEY_CHARGE_ALARM_LIMIT, value)
        }

    override var themeMode: ThemeMode
        get() = ThemeMode.fromValue(preferences.getString(KEY_THEME, null))
        set(value) = preferences.edit { putString(KEY_THEME, value.value) }

    companion object {
        private const val KEY_ONGOING_NOTIFICATION = "ongoing_notification"
        private const val KEY_THEME = "theme"
        private const val KEY_CHARGE_ALARM_ENABLED = "charge_alarm_enabled"
        private const val KEY_CHARGE_ALARM_LIMIT = "charge_alarm_limit"
        private const val NO_LIMIT = -1
    }
}
