package com.example.civilink.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * Springy press effect: scales the element down while pressed and
 * snaps it back with a bounce on release. Replaces plain [clickable]
 * across the app to make interactions feel tactile and alive.
 */
fun Modifier.bounceClick(
    pressedScale: Float = 0.94f,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = 0.45f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "bounceClickScale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
}

/**
 * Animated shimmer brush used as a photo/loading placeholder.
 */
@Composable
fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue = -400f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing)
        ),
        label = "shimmerX"
    )

    return Brush.linearGradient(
        colors = listOf(
            Color(0xFFE3EDF7),
            Color(0xFFF8FBFF),
            Color(0xFFE3EDF7)
        ),
        start = Offset(progress, 0f),
        end = Offset(progress + 400f, 260f)
    )
}

/**
 * Number that animates from its previous value up to [value]
 * whenever the target changes – great for report statistics.
 */
@Composable
fun AnimatedCounter(
    value: Int,
    modifier: Modifier = Modifier,
    style: TextStyle,
    color: Color
) {
    var hasAnimatedIn by remember(value == 0) { mutableStateOf(false) }

    val animated by animateIntAsState(
        targetValue = if (hasAnimatedIn) value else 0,
        animationSpec = tween(
            durationMillis = if (value > 25) 1100 else 650,
            easing = FastOutSlowInEasing
        ),
        label = "animatedCounter"
    )

    androidx.compose.runtime.LaunchedEffect(Unit) { hasAnimatedIn = true }

    Text(
        text = animated.toString(),
        modifier = modifier,
        style = style.copy(color = color)
    )
}

/**
 * Placeholder card shown with shimmer while community reports sync.
 */
@Composable
fun ShimmerReportCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .background(shimmerBrush(), CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(13.dp)
                    .background(shimmerBrush(), RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(10.dp)
                    .background(shimmerBrush(), RoundedCornerShape(5.dp))
            )
        }
    }
}