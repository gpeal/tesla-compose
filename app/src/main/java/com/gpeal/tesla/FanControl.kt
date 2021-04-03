package com.gpeal.tesla

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.gpeal.tesla.ui.theme.Lato
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun FanControl(
    onStepChanged: (step: Int) -> Unit = {},
    modifier: Modifier = Modifier,
    steps: Int = 5,
) {
    val density = LocalDensity.current
    var sizePx by remember { mutableStateOf(IntSize.Zero) }

    val availableWidthPx by derivedStateOf { sizePx.width - 2 * with(density) { 4.dp.toPx() } - sizePx.height }
    var isDragging by remember { mutableStateOf(false) }
    var targetProgress by remember { mutableStateOf(0.5f) }
    val progress by animateFloatAsState(
        targetValue = targetProgress,
        if (isDragging) spring(stiffness = Spring.StiffnessHigh) else spring(),
    )
    val draggableState = rememberDraggableState { onDelta ->
        targetProgress = (targetProgress + onDelta / availableWidthPx).coerceIn(0f, 1f)
    }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Fan Speed",
            style = TextStyle(
                fontFamily = Lato,
                fontWeight = FontWeight.Black,
                color = Color.White,
                fontSize = 24.sp,
            ),
            modifier = Modifier
                .padding(bottom = 12.dp)
        )
        Labels(steps)
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .onSizeChanged { sizePx = it }
                .draggable(
                    draggableState,
                    Orientation.Horizontal,
                    interactionSource = interactionSource,
                    onDragStarted = {
                      isDragging = true
                    },
                    onDragStopped = { velocity ->
                        isDragging = false
                        val factor = steps - 1
                        targetProgress = if (velocity > 0) {
                            ceil(targetProgress * factor) / factor.toFloat()
                        } else {
                            floor(targetProgress * factor) / factor.toFloat()
                        }.coerceIn(0f, 1f)
                        Log.d("Gabe", "targetProgress $targetProgress")
                    }
                )
        ) {
            SeekBarBackground()
            SeekBarForeground(progress)
            SeekBarHandle(progress)
        }
    }
}

@Composable
private fun Labels(steps: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 8.dp, end = 16.dp)
    ) {
        repeat(steps) { i ->
            Text(
                "${i + 1}",
                fontFamily = Lato,
                color = Color(0xFF7F8489),
                textAlign = TextAlign.Center,
            )
        }
    }
}
// 11, 165, 319, 473, 627, 782
@Composable
private fun SeekBarHandle(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val startX = 4.dp.toPx()
                val endX = constraints.maxWidth - constraints.maxHeight - 4.dp.toPx()
                val x = (startX + (endX - startX) * progress).toInt()
                Log.d("Gabe", "X: $x")
                layout(constraints.maxHeight, constraints.maxHeight) {
                    placeable.place(x, 0)
                }
            }
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(listOf(Color(0xFF141515), Color(0xFF2E3236)))
            )
            .border(
                BorderStroke(
                    1.dp,
                    Color(0xFF282B2E)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(Color(0xFF0172BE), Color(0xFF0F9BEE)))
                )
        )
    }
}

@Composable
private fun SeekBarForeground(
    progress: Float,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(progress)
            .padding(start = 4.dp, end = 4.dp)
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(
                Brush.linearGradient(
                    0f to Color(0xFF016BB8),
                    0.25f to Color(0xFF0782DB),
                    0.6f to Color(0xFF0F9CEB),
                    1f to Color(0xFF11A8FD),
                )
            )
    )
}

@Composable
private fun SeekBarBackground() {
    val context = LocalContext.current
    val bgDrawable = remember { ContextCompat.getDrawable(context, R.drawable.fan_bg)!! }
    Box(
        modifier = Modifier
            .height(14.dp)
            .fillMaxWidth()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawIntoCanvas { canvas ->
                bgDrawable.setBounds(0, 0, size.width.toInt(), size.height.toInt())
                bgDrawable.draw(canvas.nativeCanvas)
            }
        }
    }
}

@Preview
@Composable
private fun FanControl() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF424750))
    ) {
        FanControl(
            {},
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(horizontal = 38.dp)
        )
    }
}