package com.erman.battery.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.erman.battery.alarm.ChargeAlarm
import com.erman.battery.data.BatteryDataSource
import com.erman.battery.data.BatteryRepository
import com.erman.battery.data.BatteryStatus
import com.erman.battery.data.PreferenceSettingsStore
import com.erman.battery.data.SettingsStore
import io.reactivex.rxjava3.disposables.Disposable

class BatteryService : Service() {

    private val battery: BatteryDataSource by lazy { BatteryRepository(this) }
    private val settings: SettingsStore by lazy { PreferenceSettingsStore(this) }
    private val notifications by lazy { BatteryNotifications(this) }
    private val chargeAlarm = ChargeAlarm()

    private var latestStatus: BatteryStatus? = null
    private var statusSubscription: Disposable? = null

    override fun onCreate() {
        super.onCreate()
        promoteToForeground()
        statusSubscription = battery.status.subscribe(::onStatusChanged)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Every startForegroundService() call has to be answered with startForeground()
        promoteToForeground()
        return START_STICKY
    }

    override fun onDestroy() {
        statusSubscription?.dispose()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun promoteToForeground() {
        startForeground(
            BatteryNotifications.ONGOING_NOTIFICATION_ID,
            notifications.ongoing(latestStatus, battery.currentNow(), activeAlarmLimit())
        )
    }

    private fun onStatusChanged(status: BatteryStatus) {
        latestStatus = status
        notifications.showOngoing(status, battery.currentNow(), activeAlarmLimit())
        checkChargeAlarm(status)
    }

    private fun activeAlarmLimit(): Int? =
        settings.chargeAlarmLimit?.takeIf { settings.isChargeAlarmEnabled }

    private fun checkChargeAlarm(status: BatteryStatus) {
        val limit = activeAlarmLimit() ?: return
        if (chargeAlarm.shouldNotify(status.percentage, status.isCharging, limit)) {
            notifications.showChargeAlarm(status.percentage)
        }
    }
}
