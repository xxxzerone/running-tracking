package com.example.runningtracking.data.repository

import com.example.runningtracking.data.local.dao.RunDao
import com.example.runningtracking.data.local.entity.RunEntity
import com.example.runningtracking.domain.model.Run
import com.example.runningtracking.domain.repository.RunRepository

class RoomRunRepository(
    private val runDao: RunDao
) : RunRepository {

    override suspend fun insertRun(run: Run) {
        val entity = RunEntity(
            timestamp = run.timestamp,
            durationMillis = run.durationMillis,
            distanceMeters = run.distanceMeters
        )
        runDao.insertRun(entity)
    }
}