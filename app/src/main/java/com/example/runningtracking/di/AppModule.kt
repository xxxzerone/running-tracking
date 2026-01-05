package com.example.runningtracking.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.runningtracking.data.repository.LocationTrackerRepositoryImpl
import com.example.runningtracking.domain.repository.LocationTrackerRepository
import com.example.runningtracking.presentation.screen.home.HomeViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val appModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }

    singleOf(::LocationTrackerRepositoryImpl) { bind<LocationTrackerRepository>() }

    viewModel { HomeViewModel(androidContext(), get()) }
}