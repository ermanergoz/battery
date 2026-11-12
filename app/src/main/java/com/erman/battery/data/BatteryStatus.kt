package com.erman.battery.data

import android.os.BatteryManager

data class BatteryStatus(
    val percentage: Int,
    val voltage: Float,
    val temperature: Float,
    val chargingMode: ChargingMode,
    val health: BatteryHealth,
    val chargeState: ChargeState,
    val technology: String?
) {
    val isCharging: Boolean
        get() = chargingMode != ChargingMode.DISCHARGING

    companion object {
        fun from(
            level: Int,
            scale: Int,
            voltage: Int,
            temperatureTenths: Int,
            plugged: Int,
            health: Int,
            status: Int,
            technology: String?
        ) = BatteryStatus(
            percentage = if (scale > 0) level * 100 / scale else level,
            voltage = voltage.toFloat(),
            temperature = temperatureTenths / 10f,
            chargingMode = ChargingMode.fromPlugged(plugged),
            health = BatteryHealth.fromExtra(health),
            chargeState = ChargeState.fromExtra(status),
            technology = technology?.takeIf { it.isNotBlank() }
        )
    }
}

enum class ChargingMode {
    DISCHARGING, AC, USB, WIRELESS;

    companion object {
        fun fromPlugged(plugged: Int): ChargingMode = when (plugged) {
            BatteryManager.BATTERY_PLUGGED_AC -> AC
            BatteryManager.BATTERY_PLUGGED_USB -> USB
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> WIRELESS
            else -> DISCHARGING
        }
    }
}

enum class BatteryHealth {
    GOOD, OVERHEAT, DEAD, OVER_VOLTAGE, FAILURE, COLD, UNKNOWN;

    companion object {
        fun fromExtra(health: Int): BatteryHealth = when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> GOOD
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> OVERHEAT
            BatteryManager.BATTERY_HEALTH_DEAD -> DEAD
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> OVER_VOLTAGE
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> FAILURE
            BatteryManager.BATTERY_HEALTH_COLD -> COLD
            else -> UNKNOWN
        }
    }
}

enum class ChargeState {
    CHARGING, DISCHARGING, NOT_CHARGING, FULL, UNKNOWN;

    companion object {
        fun fromExtra(status: Int): ChargeState = when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> CHARGING
            BatteryManager.BATTERY_STATUS_DISCHARGING -> DISCHARGING
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> NOT_CHARGING
            BatteryManager.BATTERY_STATUS_FULL -> FULL
            else -> UNKNOWN
        }
    }
}
