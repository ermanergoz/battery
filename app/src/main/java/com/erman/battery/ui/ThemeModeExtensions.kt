package com.erman.battery.ui

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.erman.battery.R
import com.erman.battery.data.ThemeMode

fun ThemeMode.apply() {
    AppCompatDelegate.setDefaultNightMode(
        when (this) {
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
    )
}

@get:StringRes
val ThemeMode.labelRes: Int
    get() = when (this) {
        ThemeMode.SYSTEM -> R.string.theme_system
        ThemeMode.LIGHT -> R.string.theme_light
        ThemeMode.DARK -> R.string.theme_dark
    }
