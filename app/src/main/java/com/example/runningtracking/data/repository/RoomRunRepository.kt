package com.example.runningtracking.data.repository

import com.example.runningtracking.data.local.dao.RunDao
import com.example.runningtracking.data.local.entity.RunEntity
import com.example.runningtracking.domain.model.Run
import com.example.runningtracking.domain.repository.RunRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

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

    override fun getAllRuns(): Flow<List<Run>> {
        return runDao.getAllRuns().map { entities ->
            entities.map { entity ->
                Run(
                    timestamp = entity.timestamp,
                    durationMillis = entity.durationMillis,
                    distanceMeters = entity.distanceMeters
                )
            }
        }.flowOn(Dispatchers.IO)
    }
}