package com.senateway.guesthouse

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.senateway.guesthouse.databinding.ActivitySplashBinding
import com.senateway.guesthouse.utils.ThemeManager
//Philipp Lackner. “How to Build an Animated Splash Screen on Android - the Full Guide.” YouTube, 15 Nov. 2023, www.youtube.com/watch?v=eFZmMSm1G1c. Accessed 4 Nov. 2025.
//Android Knowledge. “Splash Screen in Android Studio Using Kotlin | Animated Splash Screen.” YouTube, 28 Oct. 2022, www.youtube.com/watch?v=MekKr4IhtRg. Accessed 4 Nov. 2025.
class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply theme before setting content view
        ThemeManager.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Hide system bars for full-screen
        hideSystemBars()
        
        // Setup and play video
        setupVideoPlayer()
    }
    
    private fun setupVideoPlayer() {

        var videoResource = resources.getIdentifier("splashvideo", "raw", packageName)
        if (videoResource == 0) {
            videoResource = resources.getIdentifier("splash_video", "raw", packageName)
        }
        
        android.util.Log.d("SplashActivity", "Video resource ID: $videoResource")
        
        if (videoResource != 0) {
            // Video found in raw folder
            val videoUri = Uri.parse("android.resource://$packageName/$videoResource")
            android.util.Log.d("SplashActivity", "Video URI: $videoUri")
            
            // Set up video completion listener
            binding.videoView.setOnCompletionListener {
                android.util.Log.d("SplashActivity", "Video completed, navigating to MainActivity")
                navigateToMain()
            }
            
            // Set up error listener with detailed logging
            binding.videoView.setOnErrorListener { _, what, extra ->
                android.util.Log.e("SplashActivity", "Video error - what: $what, extra: $extra")
                // If video fails to load, show fallback and navigate after a short delay
                binding.imageFallback.visibility = View.VISIBLE
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    navigateToMain()
                }, 2000)
                true
            }
            
            // Set up prepared listener to ensure video is ready
            binding.videoView.setOnPreparedListener { mediaPlayer ->
                android.util.Log.d("SplashActivity", "Video prepared, starting playback")
                mediaPlayer.isLooping = false
                binding.videoView.start()
                
                // Start fade-in animation for text after a short delay
                startTextAnimation()
            }
            
            // Set the video URI
            binding.videoView.setVideoURI(videoUri)
        } else {
            // Video not found, show fallback logo and navigate after delay
            android.util.Log.w("SplashActivity", "Video not found in raw folder, showing fallback")
            binding.imageFallback.visibility = View.VISIBLE
            // Start text animation even if video is not found
            startTextAnimation()
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                navigateToMain()
            }, 2000)
        }
    }
    
    private fun startTextAnimation() {
        // Delay the animation start by 500ms to let video start playing
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            // Fade in animation
            val fadeIn = ObjectAnimator.ofFloat(binding.textAppName, "alpha", 0f, 1f)
            fadeIn.duration = 1500 // 1.5 seconds fade-in
            fadeIn.interpolator = AccelerateDecelerateInterpolator()
            fadeIn.start()
        }, 500)
    }
    
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun hideSystemBars() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }
    
    override fun onPause() {
        super.onPause()
        binding.videoView.pause()
    }
    
    override fun onResume() {
        super.onResume()
        binding.videoView.resume()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.stopPlayback()
    }
}

