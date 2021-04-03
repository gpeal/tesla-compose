package com.gpeal.tesla

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gpeal.tesla.ui.theme.Lato
import kotlinx.coroutines.launch

@Composable
fun MainScreenBottomSheetContent(scaffoldState: BottomSheetScaffoldState) {
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
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
        BottomSheetDragHandle(
            onClick = {
                scope.launch {
                    if (scaffoldState.bottomSheetState.isExpanded) {
                        scaffoldState.bottomSheetState.collapse()
                    } else {
                        scaffoldState.bottomSheetState.expand()
                    }
                }
            }
        )
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BottomSheetInfo()
            ACPowerButton()
        }
        Spacer(modifier = Modifier.weight(1f))
        ACControl(
            modifier = Modifier
                .size(350.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        FanControl(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 38.dp)
                .offset(y = (-18).dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        FeatureButtons(
            modifier = Modifier
                .offset(y = (-8).dp)
        )
    }
}

@Composable
private fun BottomSheetDragHandle(
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .clickable(interactionSource, indication = null, onClick = onClick)
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
private fun BottomSheetInfo() {
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
                fontWeight = FontWeight.Black,
                shadow = Shadow(
                    Color.Black.copy(alpha = 0.25f),
                    offset = Offset(0f, shadowOffset),
                    blurRadius = shadowOffset,
                ),
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
                ),
            ),
            modifier = Modifier
                .padding(top = 8.dp)
        )
    }
}

@Composable
private fun ACPowerButton() {
    var isEnabled by remember { mutableStateOf(true) }
    ToggleButton(
        iconPainter = painterResource(R.drawable.ic_power),
        contentDescription = "Turn AC On",
        isEnabled = isEnabled,
        modifier = Modifier
            .offset(y = (-18).dp)
            .padding(end = 22.dp)
    ) {
        isEnabled = !isEnabled
    }
}

@Composable
private fun FeatureButtons(
    modifier: Modifier = Modifier,
) {
    var isAutoEnabled by remember { mutableStateOf(true) }
    var isDryEnabled by remember { mutableStateOf(true) }
    var isCoolEnabled by remember { mutableStateOf(true) }
    var isProgramEnabled by remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            "Mode",
            style = TextStyle(
                fontFamily = Lato,
                fontWeight = FontWeight.Black,
                color = Color.White,
                fontSize = 24.sp,
            ),
            modifier = Modifier
                .padding(bottom = 12.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            FeatureButton(
                "Auto",
                painterResource(R.drawable.ic_a),
                isAutoEnabled
            ) {
                isAutoEnabled = !isAutoEnabled
            }
            FeatureButton(
                "Dry",
                painterResource(R.drawable.ic_dry),
                isDryEnabled
            ) {
                isDryEnabled = !isDryEnabled
            }
            FeatureButton(
                "Cool",
                painterResource(R.drawable.ic_cool),
                isCoolEnabled
            ) {
                isCoolEnabled = !isCoolEnabled
            }
            FeatureButton(
                "Program",
                painterResource(R.drawable.ic_program),
                isProgramEnabled
            ) {
                isProgramEnabled = !isProgramEnabled
            }
        }
    }
}

@Composable
private fun FeatureButton(
    text: String,
    icon: Painter,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text,
            style = TextStyle(
                fontFamily = Lato,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color(0xFF7F8489)
            ),
            modifier = Modifier
                .offset(y = 4.dp)
        )
        ToggleButton(
            icon,
            text,
            isEnabled,
            onClick = onClick,
        )
    }
}

@Preview(device = Devices.PIXEL_3)
@Composable
private fun MainScreenBottomSheetContentPreview() {
    val scaffoldState = rememberBottomSheetScaffoldState()

    MainScreenBottomSheetContent(scaffoldState)
}

@Preview
@Composable
private fun FeatureButtonPreview() {
    var isEnabled by remember { mutableStateOf(true) }
    FeatureButton(
        "Auto",
        painterResource(R.drawable.ic_a),
        isEnabled,
    ) {
        isEnabled = !isEnabled
    }
}