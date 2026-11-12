package com.erman.battery.data

enum class ThemeMode(val value: String) {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark");

    companion object {
        fun fromValue(value: String?): ThemeMode = values().firstOrNull { it.value == value } ?: SYSTEM
    }
}
