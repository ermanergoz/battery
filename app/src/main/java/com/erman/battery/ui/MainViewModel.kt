package com.erman.battery.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.erman.battery.alarm.ChargeAlarm
import com.erman.battery.data.BatteryDataSource
import com.erman.battery.data.BatteryRepository
import com.erman.battery.data.BatteryStatus
import com.erman.battery.data.PreferenceSettingsStore
import com.erman.battery.data.SettingsStore
import com.erman.battery.service.BatteryMonitor
import kotlinx.coroutines.delay

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val battery: BatteryDataSource = BatteryRepository(application)
    private val settings: SettingsStore = PreferenceSettingsStore(application)
    private val monitor = BatteryMonitor(application)

    val status: LiveData<BatteryStatus> = battery.status

    /** The current is not part of the battery broadcast, so it is polled while the screen is visible */
    val current: LiveData<Int?> = liveData {
        while (true) {
            emit(battery.currentNow())
            delay(CURRENT_REFRESH_INTERVAL_MS)
        }
    }

    private val _isAlarmEnabled = MutableLiveData(settings.isChargeAlarmEnabled)
    val isAlarmEnabled: LiveData<Boolean> = _isAlarmEnabled

    private val _isLimitInvalid = MutableLiveData(false)
    val isLimitInvalid: LiveData<Boolean> = _isLimitInvalid

    private val _limitText = MutableLiveData(settings.chargeAlarmLimit?.toString().orEmpty())
    val limitText: LiveData<String> = _limitText

    init {
        monitor.sync()
    }

    fun onAlarmToggled(enabled: Boolean) {
        // Also ignores the switch echoing a state that came from here
        if (enabled == _isAlarmEnabled.value) return

        if (enabled) {
            val limit = ChargeAlarm.parseLimit(_limitText.value)
            if (limit == null) {
                _isLimitInvalid.value = true
                _isAlarmEnabled.value = false
                return
            }
            settings.chargeAlarmLimit = limit
        }

        settings.isChargeAlarmEnabled = enabled
        _isLimitInvalid.value = false
        _isAlarmEnabled.value = enabled
        monitor.sync()
    }

    fun onLimitChanged(text: String) {
        // The limit is locked while the alarm is on, otherwise a half typed value
        // (8 on the way to 85) would be picked up by the service right away
        if (_isAlarmEnabled.value == true) return

        val digits = text.filter { it.isDigit() }.take(MAX_LIMIT_DIGITS)
        _limitText.value = digits
        ChargeAlarm.parseLimit(digits)?.let { settings.chargeAlarmLimit = it }
        _isLimitInvalid.value = false
    }

    private companion object {
        const val CURRENT_REFRESH_INTERVAL_MS = 1_000L
        const val MAX_LIMIT_DIGITS = 3
    }
}
