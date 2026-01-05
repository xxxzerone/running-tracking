package com.example.runningtracking.domain.repository

import com.example.runningtracking.domain.model.Run

interface RunRepository {
    suspend fun insertRun(run: Run)
}