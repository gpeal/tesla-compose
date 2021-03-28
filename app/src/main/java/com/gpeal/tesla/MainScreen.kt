package com.gpeal.tesla

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    TeslaScaffold {
        MainScreenContent()
    }
}

@Composable
fun MainScreenContent() {
    Column {
        Toolbar()
    }
}

@Composable
fun Toolbar() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        ToolbarButton(
            painterResource(R.drawable.ic_profile),
            contentDescription = "Profile",
        ) {}
        ToolbarButton(
            painterResource(R.drawable.ic_profile),
            contentDescription = "Profile",
        ) {}
    }
}

@Preview(device = Devices.PIXEL_3)
@Composable
fun MainScreenPreview() {
    MainScreen()
}