package com.example.civilink.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.MidnightBlue
import com.example.civilink.ui.theme.SkyAccent
import com.example.civilink.ui.theme.CivicWhite

/**
 * CiviLink mark – a stylised city skyline forming the heart of the brand.
 *
 * • Three towers rise from a ground line (civic infrastructure)
 * • Lit windows sparkle on the hero tower (community life)
 * • A sky-blue sun punctuates the composition
 * • An unbroken orbital ring sweeps around everything,
 *   echoing the "Link" in CiviLink – connecting citizens & city.
 *
 * Rendered fully in Canvas so it stays crisp at every size and
 * colours stay tied to the brand palette. One definition, used
 * across the entire project (splash, toolbars, dialogs, loaders).
 */
@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    containerColor: Color = CivicWhite,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.28f))
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(size * 0.72f)
        ) {
            val s = this.size.width
            val p = s / 100f   // proportion unit

            // ── Orbital "link" ring ──
            val ringStroke = p * 6f
            drawArc(
                brush = Brush.linearGradient(
                    colors = listOf(CivicBlue, SkyAccent),
                    start = Offset.Zero,
                    end = Offset(s, s)
                ),
                startAngle = 200f,
                sweepAngle = 305f,
                useCenter = false,
                topLeft = Offset(p * 2f, p * 2f),
                size = Size(s - p * 4f, s - p * 4f),
                style = Stroke(width = ringStroke, cap = StrokeCap.Round)
            )

            // ── Ground line ──
            drawLine(
                brush = Brush.linearGradient(listOf(CivicBlue, SkyAccent)),
                start = Offset(p * 14f, p * 82f),
                end = Offset(p * 86f, p * 82f),
                strokeWidth = p * 5f,
                cap = StrokeCap.Round
            )

            val skyBrush = Brush.verticalGradient(
                colors = listOf(CivicBlue, MidnightBlue),
                startY = p * 30f,
                endY = p * 82f
            )

            // ── Left block (low-rise) ──
            drawRect(
                brush = skyBrush,
                topLeft = Offset(p * 18f, p * 52f),
                size = Size(p * 16f, p * 30f)
            )

            // ── Hero tower (centre) with lit windows ──
            drawRect(
                brush = skyBrush,
                topLeft = Offset(p * 40f, p * 26f),
                size = Size(p * 22f, p * 56f)
            )
            // Antenna spire
            drawLine(
                color = CivicBlue,
                start = Offset(p * 51f, p * 12f),
                end = Offset(p * 51f, p * 26f),
                strokeWidth = p * 3.5f,
                cap = StrokeCap.Round
            )
            // Beacon on top of the spire
            drawCircle(
                color = SkyAccent,
                radius = p * 3f,
                center = Offset(p * 51f, p * 11f)
            )
            // Window grid (2 columns x 5 rows) – lives inside the tower
            var row = 0
            while (row < 5) {
                var col = 0
                while (col < 2) {
                    drawRect(
                        color = CivicWhite.copy(alpha = 0.95f),
                        topLeft = Offset(
                            p * (43.5f + col * 8.5f),
                            p * (32f + row * 9.5f)
                        ),
                        size = Size(p * 5f, p * 5.5f)
                    )
                    col++
                }
                row++
            }

            // ── Right block (high-rise companion) ──
            drawRect(
                brush = skyBrush,
                topLeft = Offset(p * 68f, p * 40f),
                size = Size(p * 15f, p * 42f)
            )
            // Single window slit
            drawRect(
                color = CivicWhite.copy(alpha = 0.85f),
                topLeft = Offset(p * 72.5f, p * 48f),
                size = Size(p * 6f, p * 4.5f)
            )

            // ── Sun over the horizon ──
            drawCircle(
                color = SkyAccent,
                radius = p * 7f,
                center = Offset(p * 24f, p * 22f)
            )
        }
    }
}
