package com.gpeal.tesla

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gpeal.tesla.ui.theme.scaffoldEndColor
import com.gpeal.tesla.ui.theme.scaffoldStartColor

@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val scaffoldState = rememberBottomSheetScaffoldState()

    TeslaScaffold {
        BottomSheetScaffold(
            sheetContent = { MainScreenBottomSheetContent(scaffoldState) },
            content = { MainScreenContent() },
            scaffoldState = scaffoldState,
            sheetPeekHeight = 144.dp,
            sheetShape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
        )
    }
}

@Composable
fun MainScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(scaffoldStartColor, scaffoldEndColor)))
    ) {
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