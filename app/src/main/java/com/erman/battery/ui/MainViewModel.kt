package com.erman.battery.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erman.battery.alarm.ChargeAlarm
import com.erman.battery.data.BatteryDataSource
import com.erman.battery.data.BatteryRepository
import com.erman.battery.data.BatteryStatus
import com.erman.battery.data.CurrentReading
import com.erman.battery.data.PreferenceSettingsStore
import com.erman.battery.data.SettingsStore
import com.erman.battery.service.BatteryMonitor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val battery: BatteryDataSource = BatteryRepository(application)
    private val settings: SettingsStore = PreferenceSettingsStore(application)
    private val monitor = BatteryMonitor(application)

    val status: Observable<BatteryStatus> = battery.status

    val current: Observable<CurrentReading> =
        battery.current(CURRENT_REFRESH_INTERVAL_MS).observeOn(AndroidSchedulers.mainThread())

    private val alarmEnabled = BehaviorSubject.createDefault(settings.isChargeAlarmEnabled)
    val isAlarmEnabled: Observable<Boolean> = alarmEnabled.hide()

    private val limitInvalid = BehaviorSubject.createDefault(false)
    val isLimitInvalid: Observable<Boolean> = limitInvalid.hide()

    val savedAlarmLimit: Int?
        get() = settings.chargeAlarmLimit

    init {
        monitor.sync()
    }

    fun onAlarmToggled(enabled: Boolean, limitText: CharSequence?) {
        // Also ignores the switch echoing a state that came from here
        if (enabled == alarmEnabled.value) return

        if (enabled) {
            val limit = ChargeAlarm.parseLimit(limitText)
            if (limit == null) {
                limitInvalid.onNext(true)
                // Emitted again on purpose, it moves the switch back off
                alarmEnabled.onNext(false)
                return
            }
            settings.chargeAlarmLimit = limit
        }

        settings.isChargeAlarmEnabled = enabled
        limitInvalid.onNext(false)
        alarmEnabled.onNext(enabled)
        monitor.sync()
    }

    fun onLimitChanged(limitText: CharSequence?) {
        // The limit is locked while the alarm is on, otherwise a half typed value
        // (8 on the way to 85) would be picked up by the service right away
        if (alarmEnabled.value == true) return

        ChargeAlarm.parseLimit(limitText)?.let { settings.chargeAlarmLimit = it }
        limitInvalid.onNext(false)
    }

    private companion object {
        const val CURRENT_REFRESH_INTERVAL_MS = 1_000L
    }
}
