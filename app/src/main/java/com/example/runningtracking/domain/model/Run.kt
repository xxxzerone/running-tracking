package com.example.runningtracking.domain.model

data class Run(
    val timestamp: Long,
    val durationMillis: Long,
    val distanceMeters: Int
)