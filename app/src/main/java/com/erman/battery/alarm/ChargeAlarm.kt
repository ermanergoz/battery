package com.erman.battery.alarm

class ChargeAlarm {

    private var notifiedLimit: Int? = null

    fun shouldNotify(percentage: Int, isCharging: Boolean, limit: Int): Boolean {
        if (!isCharging) {
            notifiedLimit = null
            return false
        }
        // The level can drift around the limit while plugged in, that must not repeat the alarm
        if (limit == notifiedLimit || percentage < limit) return false

        notifiedLimit = limit
        return true
    }

    companion object {
        const val MIN_LIMIT = 1
        const val MAX_LIMIT = 100

        fun parseLimit(text: CharSequence?): Int? =
            text?.toString()?.trim()?.toIntOrNull()?.takeIf { it in MIN_LIMIT..MAX_LIMIT }
    }
}
