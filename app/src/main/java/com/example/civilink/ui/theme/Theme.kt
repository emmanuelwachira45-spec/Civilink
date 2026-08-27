package com.example.civilink.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.civilink.data.SettingsManager

// ─────────────────────────────────────────────
// CiviLink – Dark Blue & White Theme
// Dynamic (Material You) color is intentionally disabled so the
// brand palette is always preserved on every device.
// ─────────────────────────────────────────────

private val DarkColorScheme = darkColorScheme(
    primary = SkyAccent,
    onPrimary = DeepNavy,
    primaryContainer = MidnightBlue,
    onPrimaryContainer = Color(0xFFD9ECFF),
    secondary = BrightBlue,
    onSecondary = CivicWhite,
    tertiary = CivicOrange,
    onTertiary = Color(0xFF3B2A00),
    background = DeepNavy,
    onBackground = CivicWhite,
    surface = CivicNavy,
    onSurface = CivicWhite,
    surfaceVariant = MidnightBlue,
    onSurfaceVariant = MistBlue,
    outline = NavyBorder,
    error = Color(0xFFFF7086)
)

private val LightColorScheme = lightColorScheme(
    primary = CivicBlue,
    onPrimary = CivicWhite,
    primaryContainer = Color(0xFFD6E7FA),
    onPrimaryContainer = Color(0xFF0B3358),
    secondary = BrightBlue,
    onSecondary = CivicWhite,
    tertiary = CivicOrange,
    onTertiary = Color(0xFF3B2A00),
    background = CivicWhite,
    onBackground = CivicText,
    surface = IceWhite,
    onSurface = CivicText,
    surfaceVariant = CivicLightBlue,
    onSurfaceVariant = CivicGray,
    outline = Color(0xFFC9DAEA),
    error = Color(0xFFBA1A1A)
)

@Composable
fun CivilinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// ─────────────────────────────────────────────
// Live theme-aware semantic surfaces.
// These react INSTANTLY to the in-app dark-mode
// switch because they read the same preference
// flow that drives CivilinkTheme itself.
// ─────────────────────────────────────────────

/** Page/scaffold background – pale blue by day, deep navy by night. */
@Composable
fun civiPageBg(): Color {
    val isDark by SettingsManager.darkModeFlow.collectAsState()
    return if (isDark) DeepNavy else CivicLightBlue
}

/** Bottom navigation / bars that sit on the page surface. */
@Composable
fun civiNavBar(): Color {
    val isDark by SettingsManager.darkModeFlow.collectAsState()
    return if (isDark) Color(0xFF0E2A44) else CivicWhite
}

/**
 * True when CiviLink is currently rendering its night palette.
 * Handy for scrim strength, icon tints, etc.
 */
@Composable
fun isCiviDark(): Boolean {
    val isDark by SettingsManager.darkModeFlow.collectAsState()
    return isDark
}
