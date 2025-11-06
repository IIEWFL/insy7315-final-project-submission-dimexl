package com.senateway.guesthouse.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
//Himanshu Gaur. “Kotlin Multiplatform KMP Dark Mode & Light Mode Colors | Material 3 Theme Setup for Android & IOS.” YouTube, 8 Sept. 2025, www.youtube.com/watch?v=3KfttXaiITw. Accessed 4 Nov. 2025.
object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_THEME_MODE = "theme_mode"
    
    // Theme mode constants
    const val MODE_LIGHT = AppCompatDelegate.MODE_NIGHT_NO
    const val MODE_DARK = AppCompatDelegate.MODE_NIGHT_YES
    const val MODE_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    
    /**
     * Get the current theme mode from preferences
     */
    fun getThemeMode(context: Context): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_THEME_MODE, MODE_SYSTEM)
    }
    
    /**
     * Save the theme mode preference
     */
    fun setThemeMode(context: Context, mode: Int) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_THEME_MODE, mode).apply()
        AppCompatDelegate.setDefaultNightMode(mode)
    }
    
    /**
     * Toggle between light and dark mode
     */
    fun toggleTheme(context: Context) {
        val currentMode = getThemeMode(context)
        val newMode = when (currentMode) {
            MODE_LIGHT -> MODE_DARK
            MODE_DARK -> MODE_LIGHT
            else -> {
                // If system mode, check current system state
                val isCurrentlyDark = AppCompatDelegate.getDefaultNightMode() == MODE_DARK
                if (isCurrentlyDark) MODE_LIGHT else MODE_DARK
            }
        }
        setThemeMode(context, newMode)
    }
    
    /**
     * Check if dark mode is currently active
     */
    fun isDarkMode(context: Context): Boolean {
        val mode = getThemeMode(context)
        return when (mode) {
            MODE_DARK -> true
            MODE_LIGHT -> false
            else -> {
                // Check system default
                val nightModeFlags = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
                nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES
            }
        }
    }
    
    /**
     * Apply the saved theme on app startup
     */
    fun applyTheme(context: Context) {
        val mode = getThemeMode(context)
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}

