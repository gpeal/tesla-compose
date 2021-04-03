package com.gpeal.tesla

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gpeal.tesla.ui.theme.Lato
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private const val MinTemperature = 40f
private const val MaxTemperature = 90f
private const val StrokeCapBufferDegrees = 14f
private const val DialEndDegrees = 360f - 2f * StrokeCapBufferDegrees - 5f

private const val DialEndRadians = (DialEndDegrees * Math.PI / 180f).toFloat()

@Composable
fun ACControl(
    initialTemperature: Float = 74f,
    modifier: Modifier = Modifier
) {
    var temperature by remember(initialTemperature) { mutableStateOf(initialTemperature) }

    Box(
        modifier = modifier
    ) {
        BackgroundTopShadow()
        BackgroundBottomShadow()
        BackgroundCircle()
        DialUnfilledArc()
        DialArc(temperature)
        DialHandle(temperature) { percentage ->
            val temperatureRange = MaxTemperature - MinTemperature
            val newTemperature = MinTemperature + temperatureRange * percentage
            if (newTemperature < MinTemperature + 15f && temperature > MaxTemperature - 15f) return@DialHandle
            if (newTemperature > MaxTemperature - 15f && temperature < MinTemperature + 15f) return@DialHandle
            temperature = newTemperature
        }
        TemperatureText(temperature)
        repeat(7) { i ->
            TemperatureTick(temperature, 46.75f + i * 6.8649f)
        }
    }
}

@Composable
private fun BackgroundCircle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
            .fillMaxWidth()
            .padding(20.dp)
            .aspectRatio(1f)
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
            .fillMaxWidth()
            .padding(20.dp)
            .aspectRatio(1f)
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
            .fillMaxWidth()
            .padding(84.dp)
            .aspectRatio(1f)
            .aspectRatio(1f)
    ) {
        path.reset()
        path.moveTo(size.width / 2f, size.height)
        path.addArc(Rect(Offset.Zero, size), StrokeCapBufferDegrees + 5f, temperatureSweepAngle)

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
            rotate(90f - StrokeCapBufferDegrees - 5f) {
                canvas.drawPath(path, shadowPaint)
                canvas.drawPath(path, fillPaint)
            }
        }
    }
}

@Composable
fun DialUnfilledArc() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .padding(60.dp)
            .aspectRatio(1f)
            .aspectRatio(1f)
            .alpha(0.17f)
    ) {
        val innerRadius = size.width / 2f - 48.dp.toPx()
        val innerRadiusPercent = innerRadius / (size.width / 2f)
        val shadowWidth = 3.dp.toPx() / (size.width / 2f)
        drawCircle(
            Brush.radialGradient(
                0f to Color.Transparent,
                innerRadiusPercent to Color.Transparent,
                (innerRadiusPercent + 0.01f) to Color(0x4FAABBDE),
                (innerRadiusPercent + shadowWidth) to Color(0xFF1F2124),
                (1f - shadowWidth) to Color(0xFF1F2124),
                1f to Color(0x4FAABBDE),
            ),
            radius = size.width / 2f,
            center = size.center,
        )
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
            .fillMaxWidth()
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

@Composable
fun BoxScope.TemperatureText(
    temperature: Float,
) {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
    ) {
        Text(
            "${"%.0f".format(temperature)}°F",
            style = TextStyle(
                fontFamily = Lato,
                fontWeight = FontWeight.Black,
                fontSize = 38.sp,
                color = Color.White,
            )
        )
        Text(
            "Cooling…",
            style = TextStyle(
                fontFamily = Lato,
                fontSize = 20.sp,
                color = Color.White,
            )
        )
    }
}

@Composable
fun TemperatureTick(
    temperature: Float,
    tickTemperature: Float,
) {
    val onColor = Color(0xFF0A8ADA)
    val offColor = Color(0xFF15171C)
    val color by animateColorAsState(
        if (temperature >= tickTemperature) onColor else offColor,
        animationSpec = spring(),
    )
    val paint = remember { Paint() }
    val path = remember { Path() }

    val tickPercentage = (tickTemperature - MinTemperature) / (MaxTemperature - MinTemperature)
    val tickDegrees = tickPercentage * DialEndDegrees

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .aspectRatio(1f)
    ) {
        rotate(
            tickDegrees + 90f,
            pivot = size.center,
        ) {
            val topLeft = Offset(size.width - 15.dp.toPx(), size.center.y - 1.5.dp.toPx())
            val size = Size(15.dp.toPx(), 3.dp.toPx())

            path.reset()
            path.addRoundRect(RoundRect(Rect(topLeft, size),CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx())))


            drawIntoCanvas { canvas ->
                val frameworkPaint = paint.asFrameworkPaint()
                paint.color = color
                frameworkPaint.setShadowLayer(10.dp.toPx(), 0f, 0f, color.toArgb())
                canvas.drawPath(path, paint)
            }
        }
    }
}

@Preview(name = "74 Degrees")
@Composable
fun ACControlPreview74() {
    ACControl(
        74f,
        modifier = Modifier
            .width(400.dp)
            .wrapContentHeight()
    )
}

@Preview(name = "85 Degrees")
@Composable
fun ACControlPreview85() {
    ACControl(
        85f,
        modifier = Modifier
            .size(400.dp)
    )
}