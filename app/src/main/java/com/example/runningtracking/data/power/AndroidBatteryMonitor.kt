package com.example.runningtracking.data.power

import android.content.Context
import android.os.BatteryManager
import com.example.runningtracking.domain.power.BatteryMonitor

class AndroidBatteryMonitor(
    private val context: Context
) : BatteryMonitor {
    override fun getBatteryLevel(): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}
