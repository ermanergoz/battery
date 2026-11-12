package com.erman.battery.alarm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ChargeAlarmTest {

    private val alarm = ChargeAlarm()

    @Test
    fun notifiesWhenChargingReachesTheLimit() {
        assertFalse(alarm.shouldNotify(percentage = 79, isCharging = true, limit = 80))
        assertTrue(alarm.shouldNotify(percentage = 80, isCharging = true, limit = 80))
    }

    @Test
    fun notifiesWhenAlreadyAboveTheLimit() {
        assertTrue(alarm.shouldNotify(percentage = 90, isCharging = true, limit = 80))
    }

    @Test
    fun doesNotNotifyWhileDischarging() {
        assertFalse(alarm.shouldNotify(percentage = 95, isCharging = false, limit = 80))
    }

    @Test
    fun notifiesOnlyOncePerChargingSession() {
        assertTrue(alarm.shouldNotify(percentage = 80, isCharging = true, limit = 80))
        assertFalse(alarm.shouldNotify(percentage = 81, isCharging = true, limit = 80))
        assertFalse(alarm.shouldNotify(percentage = 82, isCharging = true, limit = 80))
    }

    @Test
    fun doesNotRepeatWhenTheLevelDipsBelowTheLimitWhilePlugged() {
        assertTrue(alarm.shouldNotify(percentage = 100, isCharging = true, limit = 100))
        assertFalse(alarm.shouldNotify(percentage = 99, isCharging = true, limit = 100))
        assertFalse(alarm.shouldNotify(percentage = 100, isCharging = true, limit = 100))
    }

    @Test
    fun rearmsAfterUnplugging() {
        assertTrue(alarm.shouldNotify(percentage = 80, isCharging = true, limit = 80))
        assertFalse(alarm.shouldNotify(percentage = 80, isCharging = false, limit = 80))
        assertTrue(alarm.shouldNotify(percentage = 80, isCharging = true, limit = 80))
    }

    @Test
    fun rearmsWhenTheLimitIsRaised() {
        assertTrue(alarm.shouldNotify(percentage = 80, isCharging = true, limit = 80))
        assertFalse(alarm.shouldNotify(percentage = 85, isCharging = true, limit = 90))
        assertTrue(alarm.shouldNotify(percentage = 90, isCharging = true, limit = 90))
    }

    @Test
    fun parsesValidLimits() {
        assertEquals(80, ChargeAlarm.parseLimit("80"))
        assertEquals(1, ChargeAlarm.parseLimit("1"))
        assertEquals(100, ChargeAlarm.parseLimit("100"))
        assertEquals(85, ChargeAlarm.parseLimit(" 85 "))
    }

    @Test
    fun rejectsInvalidLimits() {
        assertNull(ChargeAlarm.parseLimit(null))
        assertNull(ChargeAlarm.parseLimit(""))
        assertNull(ChargeAlarm.parseLimit("abc"))
        assertNull(ChargeAlarm.parseLimit("0"))
        assertNull(ChargeAlarm.parseLimit("101"))
        assertNull(ChargeAlarm.parseLimit("-5"))
    }
}
