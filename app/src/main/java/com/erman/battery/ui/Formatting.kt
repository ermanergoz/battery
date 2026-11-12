package com.erman.battery.ui

import android.content.Context
import com.erman.battery.R

fun Context.formatCurrent(microAmps: Int?): String =
    if (microAmps != null) getString(R.string.current_value, microAmps / 1000)
    else getString(R.string.not_available)

fun Context.formatVoltage(millivolts: Float): String =
    getString(R.string.voltage_value, millivolts / 1000f)

fun Context.formatTemperature(celsius: Float): String =
    getString(R.string.temperature_value, celsius)
