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
import androidx.compose.runtime.*
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
fun ToggleButton(
    iconPainter: Painter,
    contentDescription: String,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, animationSpec = pressAnimationSpec)
    val iconColor by animateColorAsState(if (isEnabled) Color.White else Color(0xFF7F8489))

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(88.dp)
            .scale(scale)
    ) {
        ButtonLayer1(isPressed, isEnabled)
        ButtonLayer2(isPressed, isEnabled)
        ButtonLayer3(interactionSource, isEnabled, onClick)
        Icon(
            iconPainter,
            contentDescription,
            tint = iconColor,
            modifier = Modifier
                .size(16.dp)
        )
    }
}

@Composable
private fun ButtonLayer3(
    interactionSource: MutableInteractionSource,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    val startColor by animateColorAsState(if (isEnabled) Color(0xFF016BB8) else Color(0xFF2F353A))
    val endColor by animateColorAsState(if (isEnabled) Color(0xFF11A8FD) else Color(0xFF1C1F22))

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
                    colors = listOf(startColor, endColor),
                )
            )
    )
}

@Composable
private fun ButtonLayer2(
    isPressed: Boolean,
    isEnabled: Boolean,
) {
    val stopLocation by animateFloatAsState(if (isPressed) 0.8f else 0.95f, animationSpec = pressAnimationSpec)
    val color by animateColorAsState(if (isEnabled) Color(0xFF11A8FD) else Color(0xFF2F353A))
    Box(
        modifier = Modifier
            .size(61.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to color,
                        stopLocation to color,
                        1f to color.copy(alpha = 0f),
                    ),
                )
            )
    )
}

@Composable
private fun ButtonLayer1(
    isPressed: Boolean,
    isEnabled: Boolean,
) {
    val density = LocalDensity.current
    val radius by animateFloatAsState(
        with(density) { (if (isPressed) 32 else 32).dp.toPx() },
        animationSpec = pressAnimationSpec
    )
    val center = with(density) { 50.dp.toPx() }
    val centerOffset by animateFloatAsState(
        with(density) { (if (isPressed) 0 else 5).dp.toPx() },
        animationSpec = pressAnimationSpec
    )

    val topShadowColor by animateColorAsState(if (isEnabled) Color(0xFF2F393D) else Color(0xFF262E32))
    val bottomShadowColor by animateColorAsState(if (isEnabled) Color.Black else Color(0xBF101012))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    listOf(topShadowColor, Color.Transparent),
                    center = Offset(center - centerOffset, center - centerOffset),
                    radius = radius,
                )
            )
            .background(
                Brush.radialGradient(
                    listOf(bottomShadowColor, Color.Transparent),
                    center = Offset(center + centerOffset, center + centerOffset),
                    radius = radius,
                )
            )
    )
}

@Preview(name = "Profile Button")
@Composable
private fun PowerButton() {
    var isEnabled by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier.background(Color(0xFF353A40))
    ) {
        ToggleButton(
            painterResource(R.drawable.ic_profile),
            contentDescription = "Profile",
            isEnabled = isEnabled,
        ) {
            isEnabled = !isEnabled
        }
    }
}