package com.erman.battery.data

import android.content.Context
import android.os.BatteryManager
import androidx.lifecycle.LiveData

interface BatteryDataSource {

    val status: LiveData<BatteryStatus>

    fun currentNow(): Int?
}

class BatteryRepository(context: Context) : BatteryDataSource {

    private val appContext = context.applicationContext
    private val batteryManager = appContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    override val status: LiveData<BatteryStatus> = BatteryStatusLiveData(appContext)

    override fun currentNow(): Int? {
        val current = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        // Int.MIN_VALUE means the property is not supported. The sign convention differs
        // between vendors, the reading is shown negated as it was from the start
        return if (current == Int.MIN_VALUE) null else -current
    }
}
