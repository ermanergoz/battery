package com.erman.battery.data

import android.os.BatteryManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BatteryStatusTest {

    @Test
    fun percentageIsScaled() {
        assertEquals(61, status(level = 61, scale = 100).percentage)
        assertEquals(50, status(level = 128, scale = 256).percentage)
    }

    @Test
    fun percentageFallsBackToLevelWhenScaleIsMissing() {
        assertEquals(42, status(level = 42, scale = 0).percentage)
    }

    @Test
    fun temperatureIsConvertedFromTenthsOfDegrees() {
        assertEquals(26.0f, status(temperatureTenths = 260).temperature, 0.001f)
        assertEquals(31.5f, status(temperatureTenths = 315).temperature, 0.001f)
    }

    @Test
    fun voltageIsKeptInMillivolts() {
        assertEquals(3989f, status(voltage = 3989).voltage, 0.001f)
    }

    @Test
    fun pluggedValueMapsToChargingMode() {
        assertEquals(ChargingMode.AC, ChargingMode.fromPlugged(BatteryManager.BATTERY_PLUGGED_AC))
        assertEquals(ChargingMode.USB, ChargingMode.fromPlugged(BatteryManager.BATTERY_PLUGGED_USB))
        assertEquals(ChargingMode.WIRELESS, ChargingMode.fromPlugged(BatteryManager.BATTERY_PLUGGED_WIRELESS))
        assertEquals(ChargingMode.DISCHARGING, ChargingMode.fromPlugged(0))
        assertEquals(ChargingMode.DISCHARGING, ChargingMode.fromPlugged(-1))
    }

    @Test
    fun isChargingOnlyWhenPlugged() {
        assertTrue(status(plugged = BatteryManager.BATTERY_PLUGGED_USB).isCharging)
        assertFalse(status(plugged = 0).isCharging)
    }

    @Test
    fun healthValueMapsToBatteryHealth() {
        assertEquals(BatteryHealth.GOOD, BatteryHealth.fromExtra(BatteryManager.BATTERY_HEALTH_GOOD))
        assertEquals(BatteryHealth.OVERHEAT, BatteryHealth.fromExtra(BatteryManager.BATTERY_HEALTH_OVERHEAT))
        assertEquals(BatteryHealth.DEAD, BatteryHealth.fromExtra(BatteryManager.BATTERY_HEALTH_DEAD))
        assertEquals(BatteryHealth.OVER_VOLTAGE, BatteryHealth.fromExtra(BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE))
        assertEquals(BatteryHealth.FAILURE, BatteryHealth.fromExtra(BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE))
        assertEquals(BatteryHealth.COLD, BatteryHealth.fromExtra(BatteryManager.BATTERY_HEALTH_COLD))
        assertEquals(BatteryHealth.UNKNOWN, BatteryHealth.fromExtra(BatteryManager.BATTERY_HEALTH_UNKNOWN))
        assertEquals(BatteryHealth.UNKNOWN, BatteryHealth.fromExtra(-1))
    }

    @Test
    fun statusValueMapsToChargeState() {
        assertEquals(ChargeState.CHARGING, ChargeState.fromExtra(BatteryManager.BATTERY_STATUS_CHARGING))
        assertEquals(ChargeState.DISCHARGING, ChargeState.fromExtra(BatteryManager.BATTERY_STATUS_DISCHARGING))
        assertEquals(ChargeState.NOT_CHARGING, ChargeState.fromExtra(BatteryManager.BATTERY_STATUS_NOT_CHARGING))
        assertEquals(ChargeState.FULL, ChargeState.fromExtra(BatteryManager.BATTERY_STATUS_FULL))
        assertEquals(ChargeState.UNKNOWN, ChargeState.fromExtra(BatteryManager.BATTERY_STATUS_UNKNOWN))
        assertEquals(ChargeState.UNKNOWN, ChargeState.fromExtra(-1))
    }

    @Test
    fun blankTechnologyIsTreatedAsUnknown() {
        assertEquals("Li-ion", status(technology = "Li-ion").technology)
        assertNull(status(technology = "").technology)
        assertNull(status(technology = null).technology)
    }

    private fun status(
        level: Int = 50,
        scale: Int = 100,
        voltage: Int = 4000,
        temperatureTenths: Int = 250,
        plugged: Int = 0,
        health: Int = BatteryManager.BATTERY_HEALTH_GOOD,
        state: Int = BatteryManager.BATTERY_STATUS_DISCHARGING,
        technology: String? = "Li-ion"
    ) = BatteryStatus.from(level, scale, voltage, temperatureTenths, plugged, health, state, technology)
}
