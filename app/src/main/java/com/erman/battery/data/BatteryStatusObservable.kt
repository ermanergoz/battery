package com.erman.battery.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import io.reactivex.rxjava3.core.Observable

fun batteryStatusChanges(context: Context): Observable<BatteryStatus> =
    Observable.create { emitter ->
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                emitter.onNext(intent.toBatteryStatus())
            }
        }
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        emitter.setCancellable { context.unregisterReceiver(receiver) }
    }

private fun Intent.toBatteryStatus() = BatteryStatus.from(
    level = getIntExtra(BatteryManager.EXTRA_LEVEL, 0),
    scale = getIntExtra(BatteryManager.EXTRA_SCALE, 100),
    voltage = getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0),
    temperatureTenths = getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0),
    plugged = getIntExtra(BatteryManager.EXTRA_PLUGGED, -1),
    health = getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN),
    status = getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN),
    technology = getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
)
