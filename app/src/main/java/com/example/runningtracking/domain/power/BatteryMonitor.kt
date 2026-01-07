package com.example.runningtracking.domain.power

import kotlinx.coroutines.flow.Flow

interface BatteryMonitor {
    fun getBatteryLevel(): Int
    fun observeBatteryLevel(): Flow<Int>
}
