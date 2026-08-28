package com.example.civilink.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civilink.data.SettingsManager
import com.example.civilink.navigation.ROUT_EDITPROFILE
import com.example.civilink.ui.components.AppLogo
import android.widget.Toast
import com.example.civilink.ui.theme.CivicBlue
import com.example.civilink.ui.theme.civiPageBg
import com.example.civilink.ui.theme.CivicGray
import com.example.civilink.ui.theme.CivicLightBlue
import com.example.civilink.ui.theme.CivicNavy
import com.example.civilink.ui.theme.CivicText
import com.example.civilink.ui.theme.CivicWhite
import com.example.civilink.ui.components.NairobiMap
import com.google.android.gms.maps.model.LatLng

@Composable
fun SettingScreen(navController: NavController) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }

    var locationEnabled by remember {
        mutableStateOf(settingsManager.isLocationEnabled())
    }

    var darkModeEnabled by remember {
        mutableStateOf(settingsManager.isDarkMode())
    }

    var selectedLanguage by remember { mutableStateOf("English") }
    // Captured once in composable context so dialogs can read it
    val systemIsDark = androidx.compose.foundation.isSystemInDarkTheme()
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showAppearanceDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showNairobiMap by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(civiPageBg())
    ) {

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CivicNavy)
                .padding(start = 8.dp, end = 20.dp, top = 18.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = CivicWhite,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Settings",
                color = CivicWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            AppLogo(size = 32.dp)
        }

        // Settings Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp)
        ) {

            // Welcoming personalisation banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = CivicNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppLogo(size = 44.dp)

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Make CiviLink yours",
                            color = CivicWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Dark mode, alerts & language tuned to fit your flow.",
                            color = CivicWhite.copy(alpha = 0.75f),
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(text = "Manage your CiviLink experience", color = CivicGray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(22.dp))

            // ACCOUNT
            SettingsSectionTitle(title = "Account")
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CivicWhite)
            ) {
                Column {
                    SettingItem(Icons.Default.Person, "Edit profile", "Update your personal information") {
                        navController.navigate(ROUT_EDITPROFILE)
                    }
                    SettingItem(Icons.Default.Lock, "Change password", "Update your account password") {
                        showPasswordDialog = true
                    }
                    SettingItem(Icons.Default.Security, "Privacy & security", "Manage your privacy and security") {
                        Toast.makeText(context, "Privacy settings managed by system", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // APP PREFERENCES
            SettingsSectionTitle(title = "App preferences")
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CivicWhite)
            ) {
                SettingSwitchItem(
                    icon = Icons.Default.DarkMode,
                    title = "Dark mode",
                    subtitle = if (darkModeEnabled) "Night palette is active" else "Use a darker appearance",
                    checked = darkModeEnabled,
                    onCheckedChange = { enableDark ->
                        darkModeEnabled = enableDark
                        // Live theme switch – flows through MainActivity instantly
                        settingsManager.setDarkMode(enableDark)
                        Toast.makeText(
                            context,
                            if (enableDark) "Night palette on" else "Back to daylight",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                SettingSwitchItem(
                    icon = Icons.Default.LocationOn,
                    title = "Location services",
                    subtitle = "Use your location when reporting issues",
                    checked = locationEnabled,
                    onCheckedChange = {
                        locationEnabled = it
                        settingsManager.setLocationEnabled(it)
                    }
                )

                SettingItem(Icons.Default.Language, "Language", selectedLanguage) {
                    showLanguageDialog = true
                }
                SettingItem(Icons.Default.Palette, "Appearance", "Customization") {
                    showAppearanceDialog = true
                }
                SettingItem(Icons.Default.LocationOn, "View Service Areas", "Nairobi County Map") {
                    showNairobiMap = true
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // SUPPORT
            SettingsSectionTitle(title = "Support")
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CivicWhite)
            ) {
                SettingItem(Icons.AutoMirrored.Filled.Help, "Help & support", "Get help with CiviLink") {
                    Toast.makeText(context, "Redirecting to help center...", Toast.LENGTH_SHORT).show()
                }
                SettingItem(Icons.Default.Info, "About CiviLink", "Version 1.0.0") {
                    showAboutDialog = true
                }
            }

            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "CiviLink",
                modifier = Modifier.fillMaxWidth(),
                color = CivicBlue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Dialogs (Just for show as requested)
        if (showLanguageDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showLanguageDialog = false },
                title = { Text("Select Language") },
                text = {
                    Column {
                        listOf("English", "Swahili", "French", "Spanish").forEach { lang ->
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable { 
                                    selectedLanguage = lang
                                    showLanguageDialog = false 
                                }.padding(12.dp)
                            ) {
                                Text(lang)
                            }
                        }
                    }
                },
                confirmButton = {}
            )
        }

        if (showPasswordDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showPasswordDialog = false },
                title = { Text("Change Password") },
                text = { Text("A secure link to reset your password will be sent to your registered email address.") },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { 
                        showPasswordDialog = false
                        Toast.makeText(context, "Email sent!", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Send Link")
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { showPasswordDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showAboutDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = { Text("About CiviLink") },
                text = { 
                    Text("CiviLink is a community-driven platform designed to bridge the gap between citizens and local authorities. " +
                         "Our mission is to empower residents to report civic issues like potholes, water leaks, and power outages " +
                         "directly to the right departments for faster resolution. Together, we can build better cities.") 
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { showAboutDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }

        if (showAppearanceDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showAppearanceDialog = false },
                title = { Text("Theme Preference") },
                text = {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth().clickable {
                            settingsManager.setDarkMode(systemIsDark)
                            darkModeEnabled = systemIsDark
                            showAppearanceDialog = false
                        }.padding(12.dp)) {
                            Text("Follow System")
                        }
                        Row(modifier = Modifier.fillMaxWidth().clickable {
                            settingsManager.setDarkMode(false)
                            darkModeEnabled = false
                            showAppearanceDialog = false
                        }.padding(12.dp)) {
                            Text("Light Mode")
                        }
                        Row(modifier = Modifier.fillMaxWidth().clickable {
                            settingsManager.setDarkMode(true)
                            darkModeEnabled = true
                            showAppearanceDialog = false
                        }.padding(12.dp)) {
                            Text("Dark Mode")
                        }
                    }
                },
                confirmButton = {}
            )
        }

        if (showNairobiMap) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showNairobiMap = false },
                title = { Text("CiviLink Service Area") },
                text = {
                    Box(modifier = Modifier.size(350.dp)) {
                        NairobiMap()
                    }
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { showNairobiMap = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(text = title, color = CivicText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun SettingItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp).clip(CircleShape).background(CivicLightBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(21.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = CivicText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = subtitle, color = CivicGray, fontSize = 11.sp)
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Open $title", tint = CivicGray, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun SettingSwitchItem(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp).clip(CircleShape).background(CivicLightBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(21.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = CivicText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = subtitle, color = CivicGray, fontSize = 11.sp)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    SettingScreen(navController = rememberNavController())
}
