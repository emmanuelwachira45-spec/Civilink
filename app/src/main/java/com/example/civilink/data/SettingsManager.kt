package com.example.civilink.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("civilink_prefs", Context.MODE_PRIVATE)

    companion object {
        private val _darkModeFlow = MutableStateFlow(false)
        val darkModeFlow: StateFlow<Boolean> = _darkModeFlow
        private var isInitialized = false
    }

    init {
        if (!isInitialized) {
            _darkModeFlow.value = prefs.getBoolean("dark_mode", false)
            isInitialized = true
        }
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("dark_mode", enabled).apply()
        _darkModeFlow.value = enabled
    }

    fun isDarkMode(): Boolean {
        return prefs.getBoolean("dark_mode", false)
    }

    fun setNotifications(enabled: Boolean) {
        prefs.edit().putBoolean("notifications", enabled).apply()
    }

    fun isNotificationsEnabled(): Boolean {
        return prefs.getBoolean("notifications", true)
    }
    
    fun setLocationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("location", enabled).apply()
    }
    
    fun isLocationEnabled(): Boolean {
        return prefs.getBoolean("location", true)
    }
}
