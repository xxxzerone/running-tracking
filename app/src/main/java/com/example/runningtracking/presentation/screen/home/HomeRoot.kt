package com.example.runningtracking.presentation.screen.home

import android.Manifest
import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                HomeEvent.GpsDisabled -> {
                    snackbarHostState.showSnackbar("GPS가 꺼져 있습니다. GPS를 켜주세요.")
                }
                HomeEvent.BatteryLow -> {
                    snackbarHostState.showSnackbar("배터리가 30% 이하입니다. 충전이 필요할 수 있습니다.")
                }
            }
        }
    }

    DisposableEffect(state.value.isRunning) {
        val window = (context as? Activity)?.window
        if (state.value.isRunning) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            viewModel.onAction(HomeAction.OnFetchLocation)
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        HomeScreen(
            state = state.value,
            onAction = viewModel::onAction,
            modifier = Modifier.padding(padding)
        )
    }
}