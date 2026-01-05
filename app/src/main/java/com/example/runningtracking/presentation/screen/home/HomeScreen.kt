package com.example.runningtracking.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.runningtracking.presentation.component.GoogleMapView

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        GoogleMapView(
            location = state.location,
            pathPoints = state.pathPoints
        )

        ExtendedFloatingActionButton(
            onClick = { onAction(HomeAction.OnToggleRunning) },
            icon = {
                Icon(
                    imageVector = if (state.isRunning) Icons.Filled.Close else Icons.Filled.PlayArrow,
                    contentDescription = null
                )
            },
            text = {
                Text(text = if (state.isRunning) "Running Stop" else "Running Start")
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        state = HomeState(),
        onAction = {}
    )
}