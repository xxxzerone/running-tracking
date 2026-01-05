package com.example.runningtracking.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.runningtracking.data.local.dao.RunDao
import com.example.runningtracking.data.local.entity.RunEntity

@Database(entities = [RunEntity::class], version = 1, exportSchema = false)
abstract class RunningDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao
}