package com.example.runningtracking.di

import androidx.room.Room
import com.example.runningtracking.data.local.RunningDatabase
import com.example.runningtracking.data.location.DefaultGpsStatusMonitor
import com.example.runningtracking.data.location.DefaultLocationTracker
import com.example.runningtracking.data.repository.RoomRunRepository
import com.example.runningtracking.domain.location.GpsStatusMonitor
import com.example.runningtracking.domain.location.LocationTracker
import com.example.runningtracking.domain.repository.RunRepository
import com.example.runningtracking.presentation.screen.home.HomeViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }

    singleOf(::DefaultLocationTracker) { bind<LocationTracker>() }
    singleOf(::DefaultGpsStatusMonitor) { bind<GpsStatusMonitor>() }

    single {
        Room.databaseBuilder(
            androidContext(),
            RunningDatabase::class.java,
            "running_db"
        ).build()
    }

    single { get<RunningDatabase>().runDao() }

    singleOf(::RoomRunRepository) { bind<RunRepository>() }

    viewModel { 
        HomeViewModel(
            context = androidContext(), 
            locationTracker = get<LocationTracker>(), 
            runRepository = get<RunRepository>(), 
            gpsStatusMonitor = get<GpsStatusMonitor>()
        ) 
    }
}