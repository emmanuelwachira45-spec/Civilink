package com.example.civilink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.civilink.data.SettingsManager
import com.example.civilink.navigation.AppNavHost
import com.example.civilink.ui.theme.CivilinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize settings to load the theme preference
        val settingsManager = SettingsManager(this)
        
        enableEdgeToEdge()
        setContent {
            val isDarkMode by SettingsManager.darkModeFlow.collectAsState()
            
            CivilinkTheme(darkTheme = isDarkMode) {
                AppNavHost()
            }
        }
    }
}
