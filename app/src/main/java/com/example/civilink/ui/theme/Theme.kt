package com.example.civilink.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// CiviLink Colors
val CivicNavy = Color(0xFF0B1F33)
val CivicBlue = Color(0xFF1769AA)
val CivicTeal = Color(0xFF00A896)
val CivicLightBlue = Color(0xFFE8F3FA)
val CivicWhite = Color(0xFFFFFFFF)
val CivicText = Color(0xFF172B3A)
val CivicGray = Color(0xFF6B7C8F)
val CivicOrange = Color(0xFFFF9800)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

private val DarkColorScheme = darkColorScheme(
    primary = CivicBlue,
    onPrimary = CivicWhite,
    secondary = CivicTeal,
    onSecondary = CivicWhite,
    tertiary = CivicOrange,
    onSurface = CivicWhite,
    onSurfaceVariant = CivicGray,
    background = CivicNavy,
    onBackground = CivicWhite
)

private val LightColorScheme = lightColorScheme(
    primary = CivicBlue,
    onPrimary = CivicWhite,
    secondary = CivicTeal,
    onSecondary = CivicWhite,
    tertiary = CivicOrange,
    onSurface = CivicText,
    onSurfaceVariant = CivicGray,
    background = CivicLightBlue,
    onBackground = CivicText
)

@Composable
fun CivilinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
