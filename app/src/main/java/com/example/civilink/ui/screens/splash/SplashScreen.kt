package com.example.civilink.ui.screens.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civilink.navigation.ROUT_ADMINDASHBOARD
import com.example.civilink.navigation.ROUT_HOMESCREEN
import com.example.civilink.navigation.ROUT_ONBOARDING1
import com.example.civilink.navigation.ROUT_SPLASH
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.components.FadeSlideIn
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicTeal
import com.example.civilink.ui.theme.CivicWhite
import com.example.civilink.ui.theme.SkyAccent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // Navigate from Splash → Home or Onboarding
    LaunchedEffect(Unit) {

        delay(2500)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            navController.navigate(ROUT_ONBOARDING1) {
                popUpTo(ROUT_SPLASH) { inclusive = true }
            }
        } else {
            // Check if admin
            FirebaseDatabase.getInstance().getReference("admins")
                .child(currentUser.uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        navController.navigate(ROUT_ADMINDASHBOARD) {
                            popUpTo(ROUT_SPLASH) { inclusive = true }
                        }
                    } else {
                        navController.navigate(ROUT_HOMESCREEN) {
                            popUpTo(ROUT_SPLASH) { inclusive = true }
                        }
                    }
                }
                .addOnFailureListener {
                    // Fallback to home if check fails
                    navController.navigate(ROUT_HOMESCREEN) {
                        popUpTo(ROUT_SPLASH) { inclusive = true }
                    }
                }
        }
    }

    // Logo animation
    val infiniteTransition = rememberInfiniteTransition(
        label = "Splash Animation"
    )

    val logoScale = infiniteTransition.animateFloat(
        initialValue = 0.90f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Logo Scale"
    )

    // Expanding halo pulses radiating out of the logo
    val haloFast = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Halo Fast"
    )

    val haloSlow = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(850)
        ),
        label = "Halo Slow"
    )

    // Gentle drifting ambience for floating civic icons
    val ambientDrift = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Ambient Drift"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        CivicNavy,
                        Color(0xFF123B5D),
                        CivicBlue
                    )
                )
            )
    ) {

        // Decorative background circle
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(
                    x = 150.dp,
                    y = (-80).dp
                )
                .clip(CircleShape)
                .background(
                    CivicTeal.copy(alpha = 0.12f)
                )
        )

        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(
                    x = (-100).dp,
                    y = 520.dp
                )
                .clip(CircleShape)
                .background(
                    CivicBlue.copy(alpha = 0.25f)
                )
        )

        // ── Floating civic ambience ──
        Icon(
            imageVector = Icons.Default.Campaign,
            contentDescription = null,
            tint = CivicWhite.copy(alpha = 0.13f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 110.dp, start = 38.dp)
                .size(34.dp)
                .offset(y = (-10 * ambientDrift.value).dp)
        )

        Icon(
            imageVector = Icons.Default.WaterDrop,
            contentDescription = null,
            tint = CivicWhite.copy(alpha = 0.11f),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 46.dp)
                .size(28.dp)
                .offset(y = (-12 * ambientDrift.value).dp)
        )

        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = SkyAccent.copy(alpha = 0.16f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 210.dp, end = 60.dp)
                .size(30.dp)
                .offset(y = (14 * ambientDrift.value).dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Animated Logo with radiating welcome halo
            FadeSlideIn(delayMillis = 80) {
                Box(
                    modifier = Modifier.size(185.dp),
                    contentAlignment = Alignment.Center
                ) {

                    // Outer pulse ring
                    Box(
                        modifier = Modifier
                            .size((150 + haloFast.value * 44).dp)
                            .clip(CircleShape)
                            .background(SkyAccent.copy(alpha = 0.12f * (1f - haloFast.value)))
                    )

                    // Inner pulse ring (counter-phased)
                    Box(
                        modifier = Modifier
                            .size((150 + haloSlow.value * 30).dp)
                            .clip(CircleShape)
                            .background(CivicWhite.copy(alpha = 0.10f * (1f - haloSlow.value)))
                    )

                    // Static glow base
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .scale(logoScale.value)
                            .clip(CircleShape)
                            .background(
                                CivicTeal.copy(alpha = 0.20f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AppLogo(
                            size = 105.dp
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // App Name with welcoming kicker
            FadeSlideIn(delayMillis = 240) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "WELCOME TO",
                        color = SkyAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 6.sp
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "CiviLink",
                        color = CivicWhite,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // Tagline
            FadeSlideIn(delayMillis = 380) {
                Text(
                    text = "Connect. Report. Improve.",
                    color = CivicWhite.copy(alpha = 0.82f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(
                modifier = Modifier.height(45.dp)
            )

            // Playful bouncing loader dots
            FadeSlideIn(delayMillis = 500) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        repeat(3) { index -> BouncingDot(index = index) }
                    }

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "Warming up your community feed…",
                        color = CivicWhite.copy(alpha = 0.65f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Bottom text
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = CivicTeal
            )

            Spacer(
                modifier = Modifier.size(5.dp)
            )

            Text(
                text = "Built for communities",
                color = CivicWhite.copy(alpha = 0.55f),
                fontSize = 12.sp
            )
        }
    }
}

/**
 * One cheerful loader dot – bounces upward on a staggered rhythm
 * so a [Row] of three reads as a friendly wave.
 */
@Composable
fun BouncingDot(index: Int) {
    val transition = rememberInfiniteTransition(label = "BounceDot")
    val bounce by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 460, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(index * 140)
        ),
        label = "Dot Offset"
    )

    Box(
        modifier = Modifier
            .offset(y = (-7 * bounce).dp)
            .size(9.dp)
            .clip(CircleShape)
            .background(if (index == 1) SkyAccent else CivicWhite.copy(alpha = 0.85f))
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {

    SplashScreen(
        navController = rememberNavController(),
    )
}