package com.erman.battery.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.erman.battery.R
import com.erman.battery.data.BatteryHealth
import com.erman.battery.data.ChargeState
import com.erman.battery.data.ChargingMode

@get:StringRes
val ChargingMode.labelRes: Int
    get() = when (this) {
        ChargingMode.DISCHARGING -> R.string.discharging
        ChargingMode.AC -> R.string.charging_ac
        ChargingMode.USB -> R.string.charging_usb
        ChargingMode.WIRELESS -> R.string.charging_wireless
    }

@get:DrawableRes
val ChargingMode.iconRes: Int
    get() = when (this) {
        ChargingMode.DISCHARGING -> R.drawable.ic_decharge
        ChargingMode.AC -> R.drawable.ic_ac_charger
        ChargingMode.USB -> R.drawable.ic_usb_charger
        ChargingMode.WIRELESS -> R.drawable.ic_wireless_charger
    }

@get:StringRes
val BatteryHealth.labelRes: Int
    get() = when (this) {
        BatteryHealth.GOOD -> R.string.health_good
        BatteryHealth.OVERHEAT -> R.string.health_overheat
        BatteryHealth.DEAD -> R.string.health_dead
        BatteryHealth.OVER_VOLTAGE -> R.string.health_over_voltage
        BatteryHealth.FAILURE -> R.string.health_failure
        BatteryHealth.COLD -> R.string.health_cold
        BatteryHealth.UNKNOWN -> R.string.unknown
    }

@get:StringRes
val ChargeState.labelRes: Int
    get() = when (this) {
        ChargeState.CHARGING -> R.string.state_charging
        ChargeState.DISCHARGING -> R.string.discharging
        ChargeState.NOT_CHARGING -> R.string.state_not_charging
        ChargeState.FULL -> R.string.state_full
        ChargeState.UNKNOWN -> R.string.unknown
    }
