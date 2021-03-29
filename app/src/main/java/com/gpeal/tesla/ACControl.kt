package com.gpeal.tesla

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private const val MinTemperature = 40f
private const val MaxTemperature = 90f
private const val StrokeCapBufferDegrees = 13f
private const val DialEndDegrees = 360f - 2f * StrokeCapBufferDegrees - 5f

private const val DialEndRadians = DialEndDegrees * Math.PI / 180f

@Composable
fun ACControl(
    modifier: Modifier = Modifier
) {
    var temperature by remember { mutableStateOf(74f) }

    Box(
        modifier = modifier
    ) {
        BackgroundTopShadow()
        BackgroundBottomShadow()
        BackgroundCircle()
        DialArc(temperature)
        DialHandle(temperature) { percentage ->
            val temperatureRange = MaxTemperature - MinTemperature
            val newTemperature = MinTemperature + temperatureRange * percentage
            if (newTemperature < MinTemperature + 15f && temperature > MaxTemperature - 15f) return@DialHandle
            temperature = newTemperature
        }
    }
}

@Composable
private fun BackgroundCircle() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF202428), Color(0xFF131314)),
                )
            )
    )
}

@Composable
private fun BackgroundTopShadow() {
    val color = Color(0xFF485057)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .offset((-25).dp, (-25).dp)
            .background(
                Brush.radialGradient(listOf(color, color.copy(alpha = 0f)))
            )
    )
}

@Composable
private fun BackgroundBottomShadow() {
    val color = Color(0xFF141415)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .offset(25.dp, 25.dp)
            .background(
                Brush.radialGradient(listOf(color, color.copy(alpha = 0f)))
            )
    )
}

@Composable
private fun DialArc(
    temperature: Float,
    minTemperature: Float = MinTemperature,
    maxTemperature: Float = MaxTemperature,
) {
    val fillPaint = remember {
        Paint().apply {
            style = PaintingStyle.Stroke
            strokeCap = StrokeCap.Round
        }
    }
    val shadowPaint = remember {
        Paint().apply {
            color = Color(0xFF283038)
            style = PaintingStyle.Stroke
            strokeCap = StrokeCap.Round
        }
    }

    val path = remember { Path() }
    val temperatureSweepAngle = (temperature - minTemperature) / (maxTemperature - minTemperature) * DialEndDegrees

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(84.dp)
            .aspectRatio(1f)
    ) {
        path.reset()
        path.moveTo(size.width / 2f, size.height)
        path.addArc(Rect(Offset.Zero, size), StrokeCapBufferDegrees, temperatureSweepAngle)

        drawIntoCanvas { canvas ->
            fillPaint.strokeWidth = 48.dp.toPx()
            shadowPaint.strokeWidth = 48.dp.toPx()

            fillPaint.shader = SweepGradientShader(
                size.center,
                listOf(Color(0xFF11A8FD), Color(0xFF005696)),
                listOf(0.11f, 0.75f),
            )
            val frameworkShadowPaint = shadowPaint.asFrameworkPaint()
            frameworkShadowPaint.setShadowLayer(6.dp.toPx(), 0f, 0f, shadowPaint.color.toArgb())
            rotate(90f - StrokeCapBufferDegrees) {
                canvas.drawPath(path, shadowPaint)
                canvas.drawPath(path, fillPaint)
            }
        }
    }
}

@Composable
fun DialHandle(
    temperature: Float,
    minTemperature: Float = MinTemperature,
    maxTemperature: Float = MaxTemperature,
    onPercentageChange: (percentage: Float) -> Unit,
) {
    val temperatureSweepAngle = (temperature - minTemperature) / (maxTemperature - minTemperature) * DialEndDegrees + 90f
    val temperatureSweepRadians = temperatureSweepAngle * Math.PI.toFloat() / 180f

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(59.dp)
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { pointerInputChange, _ ->
                        pointerInputChange.consumePositionChange()
                        val x = pointerInputChange.position.x - size.center.x
                        val y = pointerInputChange.position.y - size.center.y
                        // Starting at 6 o'clock clockwise in radians
                        val theta = (atan2(y, x) + 3f * Math.PI / 2f) % (2f * Math.PI)
                        val percentage = (theta / DialEndRadians).coerceAtMost(1.0)
                        onPercentageChange(percentage.toFloat())
                    },
                )
            }
    ) {
        val largeRadius = 21.dp.toPx()
        val smallRadius = 2.dp.toPx()
        val centerRadius = size.center.x - largeRadius - 4.dp.toPx()
        val centerX = size.center.x + centerRadius * cos(temperatureSweepRadians)
        val centerY = size.center.y + centerRadius * sin(temperatureSweepRadians)
        drawCircle(
            Brush.linearGradient(
                listOf(Color(0xFF141515), Color(0xFF2E3236)),
                start = Offset(centerX - largeRadius, centerY - largeRadius),
                end = Offset(centerX + largeRadius, centerY + largeRadius),
            ),
            radius = largeRadius,
            center = Offset(centerX, centerY),
        )
        drawCircle(
            Brush.linearGradient(
                listOf(Color(0xFF0172BE), Color(0xFF0F9BEE)),
                start = Offset(centerX - smallRadius, centerY - smallRadius),
                end = Offset(centerX + smallRadius, centerY + smallRadius),
            ),
            radius = smallRadius,
            center = Offset(centerX, centerY),
        )
        drawCircle(
            Color(0xFF1B1D1E),
            radius = largeRadius,
            center = Offset(centerX, centerY),
            style = Stroke(
                width = 1.dp.toPx(),
            )
        )
    }
}

@Preview
@Composable
fun ACControlPreview() {
    ACControl(
        modifier = Modifier
            .size(400.dp)
    )
}