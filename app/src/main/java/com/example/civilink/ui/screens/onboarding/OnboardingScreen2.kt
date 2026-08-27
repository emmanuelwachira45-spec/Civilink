package com.example.civilink.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civilink.navigation.ROUT_LOGINSCREEN
import com.example.civilink.navigation.ROUT_ONBOARDING1
import com.example.civilink.ui.components.AppImages
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.components.FadeSlideIn
import com.example.civilink.ui.components.PageDots
import com.example.civilink.ui.components.PhotoImage
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicWhite
import com.example.civilink.ui.theme.DeepNavy
import com.example.civilink.ui.theme.SkyAccent

@Composable
fun Onboarding2Screen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(CivicNavy, DeepNavy)
                )
            )
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        // ── Cinematic photo hero ──
        FadeSlideIn {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(330.dp)
                    .clip(RoundedCornerShape(32.dp))
            ) {
                PhotoImage(
                    url = AppImages.ONBOARDING_COMMUNITY,
                    modifier = Modifier.matchParentSize(),
                    contentDescription = "Community working together",
                    scrim = true
                )

                AppLogo(
                    size = 58.dp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp)
                )

                // Welcoming trust chip over the photo
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                        .background(
                            CivicWhite.copy(alpha = 0.16f),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = CivicWhite,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Together, problems get solved",
                        color = CivicWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(34.dp))

        FadeSlideIn(delayMillis = 180) {
            Text(
                text = "Make Your Community Better",
                color = CivicWhite,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        FadeSlideIn(delayMillis = 330) {
            Text(
                text = "Report issues around you, track their progress, and help create a better community.",
                color = CivicWhite.copy(alpha = 0.75f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 23.sp
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // ── What happens next ──
        FadeSlideIn(delayMillis = 430) {
            FeatureRow(
                icon = Icons.Default.TrackChanges,
                text = "Follow every update live"
            )
        }

        Spacer(modifier = Modifier.height(13.dp))

        FadeSlideIn(delayMillis = 500) {
            FeatureRow(
                icon = Icons.Default.Groups,
                text = "Rally neighbours around every fix"
            )
        }

        Spacer(modifier = Modifier.height(13.dp))

        FadeSlideIn(delayMillis = 570) {
            FeatureRow(
                icon = Icons.Default.Verified,
                text = "Celebrate what gets repaired"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // ── Bottom controls ──
        FadeSlideIn(delayMillis = 450) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PageDots(pageCount = 2, currentPage = 1)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        navController.navigate(ROUT_LOGINSCREEN) {
                            popUpTo(ROUT_ONBOARDING1) {
                                inclusive = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CivicBlue,
                        contentColor = CivicWhite
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {

                    Text(
                        text = "Get Started",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun Onboarding2ScreenPreview() {

    Onboarding2Screen(
        navController = rememberNavController()
    )
}

/**
 * A welcoming one-line capability highlight used on onboarding pages.
 */
@Composable
private fun FeatureRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(CivicWhite.copy(alpha = 0.08f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SkyAccent,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            color = CivicWhite.copy(alpha = 0.88f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
