package com.example.civilink.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.civilink.R
import com.example.civilink.ui.theme.CivicWhite
import com.example.civilink.ui.theme.DeepNavy

/**
 * Remote photo rendered through Coil with graceful CiviLink-styled states:
 *  • loading  → animated shimmer placeholder
 *  • failure  → navy→blue gradient with a subtle fallback icon (offline-safe)
 *  • optional [scrim] gradient so white overlay text stays legible
 *
 * The caller gives this composable an explicit size via [modifier]
 * (e.g. `.fillMaxWidth().height(180.dp)`); content fills and crops.
 */
@Composable
fun PhotoImage(
    url: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    scrim: Boolean = false,
    fallbackIcon: ImageVector = Icons.Default.LocationCity,
    fallbackIconSize: Dp = 44.dp
) {
    Box(modifier = modifier) {

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(450)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
            loading = {
                // Signature skyline scene shows instantly while the
                // network photo streams in on top of it.
                Box(Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(R.drawable.city_skyline),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(shimmerBrush().let { shimmer ->
                                Brush.verticalGradient(
                                    listOf(CivicWhite.copy(alpha = 0.06f), CivicWhite.copy(alpha = 0.02f))
                                )
                            })
                    )
                }
            },
            error = {
                // Offline / not found → the skyline scene remains, so
                // photos are ALWAYS seen rather than collapsing to grey.
                Image(
                    painter = painterResource(R.drawable.city_skyline),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        )

        if (scrim) {
            Box(
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            0f to DeepNavy.copy(alpha = 0.10f),
                            1f to DeepNavy.copy(alpha = 0.72f)
                        )
                    )
            )
        }
    }
}