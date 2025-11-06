package com.senateway.guesthouse

import android.app.Application
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.senateway.guesthouse.utils.ThemeManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Apply theme on app startup
        ThemeManager.applyTheme(this)
        
        // Initialize Coil with SVG support for weather icons
        val imageLoader = ImageLoader.Builder(this)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
        coil.Coil.setImageLoader(imageLoader)
    }
}

