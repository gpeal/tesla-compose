package com.gpeal.tesla

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gpeal.tesla.ui.theme.Lato
import com.gpeal.tesla.ui.theme.scaffoldEndColor
import com.gpeal.tesla.ui.theme.scaffoldStartColor

@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val scaffoldState = rememberBottomSheetScaffoldState()

    TeslaScaffold {
        BottomSheetScaffold(
            sheetContent = { BottomSheetContent() },
            content = { MainScreenContent() },
            scaffoldState = scaffoldState,
            sheetPeekHeight = 144.dp,
            sheetShape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
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

@Composable
private fun BottomSheetContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(listOf(Color(0xFF353A40), Color(0xFF16171B)))
            )
            .border(
                2.dp,
                Brush.linearGradient(listOf(Color(0xFF424750), Color(0xFF202326))),
                RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            )
    ) {
        BottomSheetDragHandle()
        BottomSheetInfo()
    }
}

@Composable
fun BottomSheetDragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp, 4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFF17181C))
                .align(Alignment.Center)
        )
    }
}

@Composable
fun BottomSheetInfo() {
    val shadowOffset = with(LocalDensity.current) { 4.dp.toPx() }

    Column(
        modifier = Modifier
            .padding(start = 40.dp)
            .width(214.dp)
    ) {
        Text(
            "A/C is ON",
            style = TextStyle(
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = Lato,
                fontWeight = FontWeight.Black
            )
        )
        Text(
            "Tap to turn off or swipe up for a fast setup",
            style = TextStyle(
                color = Color(0xFF857F89),
                fontFamily = Lato,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                shadow = Shadow(
                    Color.Black.copy(alpha = 0.25f),
                    offset = Offset(0f, shadowOffset),
                    blurRadius = shadowOffset,
                )
            ),
            modifier = Modifier
                .padding(top = 8.dp)
        )
    }
}

@Preview(device = Devices.PIXEL_3)
@Composable
fun MainScreenPreview() {
    MainScreen()
}