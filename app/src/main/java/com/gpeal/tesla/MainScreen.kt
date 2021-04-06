package com.gpeal.tesla

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
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
            sheetContent = { MainScreenBottomSheetContent(scaffoldState) },
            content = { MainScreenContent() },
            scaffoldState = scaffoldState,
            sheetPeekHeight = 130.dp,
            sheetShape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
        )
    }
}

@Composable
fun MainScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(brush = Brush.verticalGradient(listOf(scaffoldStartColor, scaffoldEndColor)))
            .verticalScroll(rememberScrollState())
    ) {
        Toolbar()
        Image(
            painterResource(R.drawable.cybertruck),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .fillMaxWidth()
        )
        StatusSection()
        Spacer(modifier = Modifier.height(28.dp))
        InformationSection()
        Spacer(modifier = Modifier.height(200.dp))
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
        ToolbarText()
        ToolbarButton(
            painterResource(R.drawable.ic_profile),
            contentDescription = "Profile",
        ) {}
    }

}

@Composable
private fun ToolbarText() {
    val density = LocalDensity.current
    val blurRadius = with(density) { 4.dp.toPx() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Tesla",
            style = TextStyle(
                fontFamily = Lato,
                fontSize = 18.sp,
                color = Color(0xFF857F89),
            ),
            modifier = Modifier
                .padding(top = 24.dp)
        )
        Text(
            "Cybertruck",
            style = TextStyle(
                fontFamily = Lato,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFDFDFD),
                shadow = Shadow(
                    Color.White.copy(alpha = 0.25f),
                    offset = Offset(0f, 0f),
                    blurRadius = blurRadius,
                ),
            ),
            modifier = Modifier
                .padding(top = 4.dp)
        )
    }
}

@Composable
private fun StatusSection() {
    Text(
        "Status",
        style = TextStyle(
            fontFamily = Lato,
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Black,
        ),
        modifier = Modifier
            .padding(start = 24.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .padding(start = 24.dp)
    ) {
        StatusItem(R.drawable.ic_battery, "Battery", "54%")
        Spacer(modifier = Modifier.width(24.dp))
        StatusItem(R.drawable.ic_range, "Range", "279 mi")
        Spacer(modifier = Modifier.width(24.dp))
        StatusItem(R.drawable.ic_temperature, "Temperature", "72° F")
    }
}

@Composable
private fun StatusItem(
    @DrawableRes iconResource: Int,
    label: String,
    value: String,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painterResource(iconResource),
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                label,
                style = TextStyle(
                    fontFamily = Lato,
                    fontSize = 18.sp,
                    color = Color(0xFF857F89),
                ),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            value,
            style = TextStyle(
                fontFamily = Lato,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
            )
        )
    }
}

@Composable
private fun InformationSection() {
    val scrollState = rememberScrollState()
    Text(
        "Information",
        style = TextStyle(
            fontFamily = Lato,
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Black,
        ),
        modifier = Modifier
            .padding(start = 24.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .padding(start = 8.dp)
            .horizontalScroll(scrollState)
    ) {
        InformationItem("Engine", "Sleeping mode")
        InformationItem("Climate", "A/C is ON")
        InformationItem("Tires", "Low pressure")
    }
}

@Composable
private fun InformationItem(
    label: String,
    value: String,
) {
    val path = remember { Path() }
    val shadowPaint = remember { Paint() }
    val padding = 18.dp
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier
            .size(200.dp, 124.dp)
            .fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawIntoCanvas { canvas ->
                path.reset()
                val inset = padding.toPx()
                path.addRoundRect(
                    RoundRect(
                        inset,
                        inset,
                        size.width - inset,
                        size.height - inset,
                        CornerRadius(8.dp.toPx()),
                    )
                )

                shadowPaint.asFrameworkPaint().setShadowLayer(
                    18.dp.toPx(),
                    -2.dp.toPx(),
                    -2.dp.toPx(),
                    Color(0xFF262E32).toArgb()
                )
                canvas.drawPath(path, shadowPaint)
                shadowPaint.asFrameworkPaint().setShadowLayer(
                    18.dp.toPx(),
                    3.dp.toPx(),
                    3.dp.toPx(),
                    Color(0xCF101012).toArgb()
                )
                canvas.drawPath(path, shadowPaint)
                drawPath(path, Brush.linearGradient(listOf(Color(0xFF1F2328), Color(0xFF1A1C1F))))
            }
        }
        Column(
            Modifier.padding(padding + 16.dp)
        ) {
            Text(
                label,
                style = TextStyle(
                    fontFamily = Lato,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    fontSize = 18.sp
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                value,
                style = TextStyle(
                    fontFamily = Lato,
                    color = Color(0xFF857F89),
                    fontSize = 18.sp
                )
            )

        }
    }
}

@Preview(device = Devices.PIXEL_3)
@Composable
fun MainScreenPreview() {
    MainScreen()
}