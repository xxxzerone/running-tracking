package com.example.runningtracking.domain.repository

import com.example.runningtracking.domain.model.Run
import kotlinx.coroutines.flow.Flow

interface RunRepository {
    suspend fun insertRun(run: Run)
    fun getAllRuns(): Flow<List<Run>>
}