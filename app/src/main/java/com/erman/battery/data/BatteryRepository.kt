package com.erman.battery.data

import android.content.Context
import android.os.BatteryManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

interface BatteryDataSource {

    val status: Observable<BatteryStatus>

    /** The current is not part of the battery broadcast, so it is polled every [periodMillis] */
    fun current(periodMillis: Long): Observable<CurrentReading>

    fun currentNow(): Int?
}

class BatteryRepository(context: Context) : BatteryDataSource {

    private val appContext = context.applicationContext
    private val batteryManager = appContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    override val status: Observable<BatteryStatus> =
        batteryStatusChanges(appContext).replay(1).refCount()

    override fun current(periodMillis: Long): Observable<CurrentReading> =
        pollCurrent(::currentNow, periodMillis, Schedulers.computation())

    override fun currentNow(): Int? {
        val current = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        // Int.MIN_VALUE means the property is not supported. The sign convention differs
        // between vendors, the reading is shown negated as it was from the start
        return if (current == Int.MIN_VALUE) null else -current
    }
}
