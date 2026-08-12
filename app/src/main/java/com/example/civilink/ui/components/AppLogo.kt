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
import com.example.civilink.ui.theme.CivicTeal
import com.example.civilink.ui.theme.CivicWhite

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
            modifier = Modifier.size(size * 0.65f)
        ) {
            val strokeWidth = size.toPx() * 0.15f
            val arcSize = Size(size.toPx() * 0.65f - strokeWidth, size.toPx() * 0.65f - strokeWidth)
            val offset = strokeWidth / 2f

            // Drawing a stylized "C" logo based on the provided geometric design
            // Upper Arc
            drawArc(
                brush = Brush.linearGradient(
                    colors = listOf(CivicBlue, CivicTeal),
                    start = Offset(0f, 0f),
                    end = Offset(size.toPx(), size.toPx())
                ),
                startAngle = 160f,
                sweepAngle = 220f,
                useCenter = false,
                topLeft = Offset(offset, offset),
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Inner Accent Link (The "Link" part of CiviLink)
            drawArc(
                color = CivicTeal,
                startAngle = 0f,
                sweepAngle = 40f,
                useCenter = false,
                topLeft = Offset(offset + strokeWidth * 0.8f, offset + strokeWidth * 0.8f),
                size = Size(arcSize.width - strokeWidth * 1.6f, arcSize.height - strokeWidth * 1.6f),
                style = Stroke(width = strokeWidth * 0.6f, cap = StrokeCap.Round)
            )
            
            // Connecting dot (Central focal point)
            drawCircle(
                color = CivicBlue,
                radius = strokeWidth * 0.4f,
                center = Offset(size.toPx() * 0.325f, size.toPx() * 0.325f)
            )
        }
    }
}
