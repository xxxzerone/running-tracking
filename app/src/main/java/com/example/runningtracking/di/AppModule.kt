package com.example.runningtracking.di

import com.example.runningtracking.data.location.DefaultLocationTracker
import com.example.runningtracking.domain.location.LocationTracker
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

    viewModel { HomeViewModel(get()) }
}