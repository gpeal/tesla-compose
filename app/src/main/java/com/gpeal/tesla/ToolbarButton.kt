package com.gpeal.tesla

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val pressAnimationSpec = spring<Float>(dampingRatio = 0.4f, stiffness = 400f)

@Composable
fun ToolbarButton(
    iconPainter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, animationSpec = pressAnimationSpec)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp)
            .scale(scale)
    ) {
        ButtonLayer1(isPressed)
        ButtonLayer2(isPressed)
        ButtonLayer3(interactionSource, onClick)
        Icon(
            iconPainter,
            contentDescription,
            tint = Color(0xFF857F89),
            modifier = Modifier
                .size(16.dp)
        )
    }
}

@Composable
private fun ButtonLayer3(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(57.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource,
                indication = null,
                onClick = onClick,
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF2F353A), Color(0xFF1C1F22)),
                )
            )
    )
}

@Composable
private fun ButtonLayer2(isPressed: Boolean) {
    val stopLocation by animateFloatAsState(if (isPressed) 0.8f else 0.95f, animationSpec = pressAnimationSpec)
    Box(
        modifier = Modifier
            .size(61.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF2C3036),
                        stopLocation to Color(0xFF2C3036),
                        1f to Color(0x0031343C),
                    ),
                )
            )
    )
}

@Composable
private fun ButtonLayer1(isPressed: Boolean) {
    val density = LocalDensity.current
    val radius by animateFloatAsState(
        with(density) { (if (isPressed) 35 else 40).dp.toPx() },
        animationSpec = pressAnimationSpec
    )
    val center = with(density) { 50.dp.toPx() }
    val centerOffset by animateFloatAsState(
        with(density) { (if (isPressed) 4 else 7).dp.toPx() },
        animationSpec = pressAnimationSpec
    )

    val topLeftShadowColor by animateColorAsState(if (isPressed) Color(0xFF424a52) else Color(0xFF485057))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    listOf(topLeftShadowColor, Color.Transparent),
                    center = Offset(center - centerOffset, center - centerOffset),
                    radius = radius,
                )
            )
            .background(
                Brush.radialGradient(
                    listOf(Color(0xFF1F2427), Color.Transparent),
                    center = Offset(center + centerOffset, center + centerOffset),
                    radius = radius,
                )
            )
    )
}

@Preview(name = "Profile Button")
@Composable
fun ProfileButton() {
    Box(
        modifier = Modifier.background(Color(0xFF353A40))
    ) {
        ToolbarButton(
            painterResource(R.drawable.ic_profile),
            contentDescription = "Profile",
        ) {}

    }
}