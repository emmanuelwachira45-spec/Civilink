package com.example.civilink.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.civilink.ui.theme.CivicWhite
import com.example.civilink.ui.theme.SkyAccent

/**
 * Slides content up while fading it in on first composition.
 * Stagger blocks on a screen with different [delayMillis] values
 * for a polished, choreographed entrance.
 */
@Composable
fun FadeSlideIn(
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    dropDistance: Dp = 46.dp,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delayMillis.toLong())
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 640, easing = FastOutSlowInEasing),
        label = "fadeSlideAlpha"
    )
    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else dropDistance,
        animationSpec = tween(durationMillis = 640, easing = FastOutSlowInEasing),
        label = "fadeSlideY"
    )

    Box(
        modifier
            .offset(y = offsetY)
            .graphicsLayer { this.alpha = alpha }
    ) {
        content()
    }
}

/**
 * Horizontal page indicator with a stretching pill for the active page.
 */
@Composable
fun PageDots(
    pageCount: Int,
    currentPage: Int,
    activeColor: Color = SkyAccent,
    inactiveColor: Color = CivicWhite.copy(alpha = 0.28f),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val active = index == currentPage
            val width by animateDpAsState(
                targetValue = if (active) 28.dp else 8.dp,
                animationSpec = spring(dampingRatio = 0.65f),
                label = "pageDotWidth"
            )
            Box(
                Modifier
                    .height(8.dp)
                    .width(width)
                    .background(
                        color = if (active) activeColor else inactiveColor,
                        shape = CircleShape
                    )
            )
        }
    }
}