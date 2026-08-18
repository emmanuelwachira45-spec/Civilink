package com.example.civilink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.civilink.navigation.AppNavHost
import com.example.civilink.ui.theme.CivilinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CivilinkTheme {
                AppNavHost()
            }
        }
    }
}
