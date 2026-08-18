package com.example.civilink.ui.screens.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.civilink.navigation.ROUT_ONBOARDING1
import com.example.civilink.navigation.ROUT_SPLASH
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicTeal
import com.example.civilink.ui.theme.CivicWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // Navigate from Splash → Onboarding 1
    LaunchedEffect(Unit) {

        delay(3000)

        navController.navigate(ROUT_ONBOARDING1) {

            // Remove Splash from the back stack
            popUpTo(ROUT_SPLASH) {
                inclusive = true
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Animated Logo
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .scale(logoScale.value),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(
                            CivicTeal.copy(alpha = 0.20f)
                        )
                )

                AppLogo(
                    size = 105.dp
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // App Name
            Text(
                text = "CiviLink",
                color = CivicWhite,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // Tagline
            Text(
                text = "Connect. Report. Improve.",
                color = CivicWhite.copy(alpha = 0.82f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(45.dp)
            )

            // Loading indicator
            CircularProgressIndicator(
                modifier = Modifier.size(30.dp),
                color = CivicTeal,
                strokeWidth = 3.dp
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Making your community heard",
                color = CivicWhite.copy(alpha = 0.65f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
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

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {

    SplashScreen(
        navController = rememberNavController(),
    )
}
