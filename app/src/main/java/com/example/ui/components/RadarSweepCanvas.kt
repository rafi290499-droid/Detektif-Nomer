package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.ScamAlertRed
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarSweepCanvas(
    modifier: Modifier = Modifier,
    isTargetDetected: Boolean = true,
    riskColor: Color = ScamAlertRed
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarTransition")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SweepAngle"
    )

    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseRadius"
    )

    Box(
        modifier = modifier.testTag("radar_sweep_canvas"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = (size.minDimension / 2f) * 0.92f

            // Background radar circle glow
            drawCircle(
                color = Color(0xFF0F172A).copy(alpha = 0.85f),
                radius = maxRadius,
                center = center
            )

            // Concentric range circles
            val circleCount = 4
            for (i in 1..circleCount) {
                val r = maxRadius * (i.toFloat() / circleCount)
                drawCircle(
                    color = RadarCyan.copy(alpha = 0.25f),
                    radius = r,
                    center = center,
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }

            // Expanding pulse wave
            drawCircle(
                color = RadarCyan.copy(alpha = (1f - pulseRadius).coerceIn(0f, 0.4f)),
                radius = maxRadius * pulseRadius,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // Crosshair lines
            drawLine(
                color = RadarCyan.copy(alpha = 0.3f),
                start = Offset(center.x - maxRadius, center.y),
                end = Offset(center.x + maxRadius, center.y),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = RadarCyan.copy(alpha = 0.3f),
                start = Offset(center.x, center.y - maxRadius),
                end = Offset(center.x, center.y + maxRadius),
                strokeWidth = 1.dp.toPx()
            )

            // Diagonal reference marks
            val diagR = maxRadius * 0.707f
            drawLine(
                color = RadarCyan.copy(alpha = 0.15f),
                start = Offset(center.x - diagR, center.y - diagR),
                end = Offset(center.x + diagR, center.y + diagR),
                strokeWidth = 0.8.dp.toPx()
            )
            drawLine(
                color = RadarCyan.copy(alpha = 0.15f),
                start = Offset(center.x - diagR, center.y + diagR),
                end = Offset(center.x + diagR, center.y - diagR),
                strokeWidth = 0.8.dp.toPx()
            )

            // Rotating sweep beam
            val rad = Math.toRadians(sweepAngle.toDouble())
            val endX = center.x + maxRadius * cos(rad).toFloat()
            val endY = center.y + maxRadius * sin(rad).toFloat()

            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(RadarCyan.copy(alpha = 0.9f), Color.Transparent),
                    start = center,
                    end = Offset(endX, endY)
                ),
                start = center,
                end = Offset(endX, endY),
                strokeWidth = 3.dp.toPx()
            )

            // Sector light fan
            drawArc(
                brush = Brush.sweepGradient(
                    0.0f to Color.Transparent,
                    0.85f to Color.Transparent,
                    1.0f to RadarCyan.copy(alpha = 0.28f),
                    center = center
                ),
                startAngle = sweepAngle - 45f,
                sweepAngle = 45f,
                useCenter = true,
                topLeft = Offset(center.x - maxRadius, center.y - maxRadius),
                size = androidx.compose.ui.geometry.Size(maxRadius * 2, maxRadius * 2)
            )

            // Central Cell Tower / Base Station Dot
            drawCircle(
                color = RadarCyan,
                radius = 5.dp.toPx(),
                center = center
            )
            drawCircle(
                color = Color.White,
                radius = 2.dp.toPx(),
                center = center
            )

            // Target Blip (Suspect Location Triangulated)
            if (isTargetDetected) {
                val targetOffset = Offset(
                    center.x + maxRadius * 0.52f,
                    center.y - maxRadius * 0.40f
                )

                // Flashing red suspect ping
                drawCircle(
                    color = riskColor.copy(alpha = 0.35f),
                    radius = 14.dp.toPx() * pulseRadius,
                    center = targetOffset,
                    style = Stroke(width = 2.dp.toPx())
                )
                drawCircle(
                    color = riskColor,
                    radius = 6.dp.toPx(),
                    center = targetOffset
                )
                drawCircle(
                    color = Color.White,
                    radius = 2.5.dp.toPx(),
                    center = targetOffset
                )

                // Secondary decoy tower blip
                val towerOffset = Offset(
                    center.x - maxRadius * 0.45f,
                    center.y + maxRadius * 0.35f
                )
                drawCircle(
                    color = RadarCyan.copy(alpha = 0.7f),
                    radius = 3.5.dp.toPx(),
                    center = towerOffset
                )
            }
        }
    }
}
