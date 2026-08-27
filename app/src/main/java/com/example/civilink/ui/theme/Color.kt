package com.example.civilink.ui.theme

import androidx.compose.ui.graphics.Color

// CiviLink - Dark Blue & White Brand Palette

// Core blues
val DeepNavy get() = Color(0xFF06192B)      // deepest blue - dark backgrounds
val CivicNavy get() = Color(0xFF0A2540)     // primary dark-blue surface / headers
val MidnightBlue get() = Color(0xFF10375C)  // elevated dark-blue cards
val CivicBlue get() = Color(0xFF1565C0)     // royal blue - primary actions
val BrightBlue get() = Color(0xFF2D7DD2)    // brighter royal for gradients
val SkyAccent get() = Color(0xFF4FC3F7)     // vivid sky highlight

// Semantic statuses (small accents only)
val CivicOrange get() = Color(0xFFFFB84D)   // pending - warm amber
val StatusGreen get() = Color(0xFF34D399)   // resolved - success

// Whites & neutrals
val CivicWhite get() = Color(0xFFFFFFFF)
val IceWhite get() = Color(0xFFF4F9FE)      // soft off-white surfaces
val CivicLightBlue get() = Color(0xFFE7F1FB) // pale-blue chip/tint background
val CivicText get() = Color(0xFF10293F)     // headings on light surfaces
val CivicGray get() = Color(0xFF5C7086)     // secondary text
val NavyBorder get() = Color(0xFF1E4266)    // hairlines on dark blue
val MistBlue get() = Color(0xFFB9CCDF)      // secondary text on dark blue

// Compatibility aliases
val CivicTeal get() = SkyAccent
