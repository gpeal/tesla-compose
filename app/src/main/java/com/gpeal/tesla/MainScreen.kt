package com.gpeal.tesla

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen() {
    TeslaScaffold {
        MainScreenContent()
    }
}

@Composable
fun MainScreenContent() {
    Text("Hello World", color = Color.White)
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}