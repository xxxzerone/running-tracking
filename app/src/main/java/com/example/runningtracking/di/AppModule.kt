package com.example.runningtracking.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.runningtracking.data.location.DefaultLocationTracker
import com.example.runningtracking.domain.location.LocationTracker
import com.example.runningtracking.presentation.screen.home.HomeViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val appModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }

    single<LocationTracker> { DefaultLocationTracker(get()) }

    viewModel { HomeViewModel(androidContext(), get()) }
}