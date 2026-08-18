package com.example.civilink.ui.screens.setting

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civilink.ui.components.AppLogo
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite

@Composable
fun SettingScreen(navController: NavController) {

    var notificationsEnabled by remember {
        mutableStateOf(value = true)
    }

    var locationEnabled by remember {
        mutableStateOf(value = true)
    }

    var darkModeEnabled by remember {
        mutableStateOf(value = false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CivicLightBlue)
    ) {

        // ─────────────────────────────
        // Top Bar
        // ─────────────────────────────

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CivicNavy)
                .padding(
                    start = 8.dp,
                    end = 20.dp,
                    top = 18.dp,
                    bottom = 18.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    navController.popBackStack()
                },
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = CivicWhite,
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "Settings",
                color = CivicWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            AppLogo(size = 32.dp)
        }

        // ─────────────────────────────
        // Settings Content
        // ─────────────────────────────

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp)
        ) {

            Text(
                text = "Manage your CiviLink experience",
                color = CivicGray,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // ACCOUNT
            SettingsSectionTitle(
                title = "Account"
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                )
            ) {

                Column {

                    SettingItem(
                        icon = Icons.Default.Lock,
                        title = "Change password",
                        subtitle = "Update your account password",
                    ) {
                        // Change password
                    }

                    SettingItem(
                        icon = Icons.Default.Security,
                        title = "Privacy & security",
                        subtitle = "Manage your privacy and security",
                    ) {
                        // Privacy settings
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // NOTIFICATIONS
            SettingsSectionTitle(
                title = "Notifications"
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite,
                ),
            ) {

                SettingSwitchItem(
                    icon = Icons.Default.Notifications,
                    title = "Report updates",
                    subtitle = "Get updates when your reports change",
                    checked = notificationsEnabled,
                ) {
                    notificationsEnabled = it
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // APP PREFERENCES
            SettingsSectionTitle(
                title = "App preferences"
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                )
            ) {

                SettingSwitchItem(
                    icon = Icons.Default.DarkMode,
                    title = "Dark mode",
                    subtitle = "Use a darker appearance",
                    checked = darkModeEnabled,
                    onCheckedChange = {
                        darkModeEnabled = it
                    }
                )

                SettingSwitchItem(
                    icon = Icons.Default.LocationOn,
                    title = "Location services",
                    subtitle = "Use your location when reporting issues",
                    checked = locationEnabled,
                    onCheckedChange = {
                        locationEnabled = it
                    }
                )

                SettingItem(
                    icon = Icons.Default.Language,
                    title = "Language",
                    subtitle = "English",
                ) {
                    // Language settings
                }

                SettingItem(
                    icon = Icons.Default.Palette,
                    title = "Appearance",
                    subtitle = "CiviLink default theme",
                ) {
                    // Appearance settings
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // SUPPORT
            SettingsSectionTitle(
                title = "Support"
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicWhite
                )
            ) {

                SettingItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    title = "Help & support",
                    subtitle = "Get help with CiviLink",
                ) {
                    // Help
                }

                SettingItem(
                    icon = Icons.Default.Info,
                    title = "About CiviLink",
                    subtitle = "Version 1.0.0",
                ) {
                    // About
                }
            }

            Spacer(
                modifier = Modifier.height(25.dp)
            )

            Text(
                text = "CiviLink",
                modifier = Modifier.fillMaxWidth(),
                color = CivicBlue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Connect. Report. Improve.",
                modifier = Modifier.fillMaxWidth(),
                color = CivicGray,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}


// ─────────────────────────────────────────────
// Section Title
// ─────────────────────────────────────────────

@Composable
fun SettingsSectionTitle(
    title: String,
) {

    Text(
        text = title,
        color = CivicText,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
    )
}


// ─────────────────────────────────────────────
// Normal Setting Item
// ─────────────────────────────────────────────

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 15.dp,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(CivicLightBlue),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CivicBlue,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = CivicText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = subtitle,
                color = CivicGray,
                fontSize = 11.sp
            )
        }

        IconButton(
            onClick = onClick
        ) {

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open $title",
                tint = CivicGray
            )
        }
    }
}


// ─────────────────────────────────────────────
// Switch Setting Item
// ─────────────────────────────────────────────

@Composable
fun SettingSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 15.dp,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(CivicLightBlue),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CivicBlue,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = CivicText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = subtitle,
                color = CivicGray,
                fontSize = 11.sp
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {

    SettingScreen(
        navController = rememberNavController(),
    )
}
