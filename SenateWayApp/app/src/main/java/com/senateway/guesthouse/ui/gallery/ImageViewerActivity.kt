package com.senateway.guesthouse.ui.gallery

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.senateway.guesthouse.databinding.ActivityImageViewerBinding

class ImageViewerActivity : Activity() {
    
    private lateinit var binding: ActivityImageViewerBinding
    private lateinit var images: List<GalleryImage>
    private var currentPosition: Int = 0
    private var isUIVisible = true
    
    // Auto slideshow
    //Divya. “How to Create Android Auto Image Slider with Round Indicator Using Kotlin?” Stack Overflow, 26 May 2021, stackoverflow.com/questions/67699212/how-to-create-android-auto-image-slider-with-round-indicator-using-kotlin.
    private val autoSlideHandler = Handler(Looper.getMainLooper())
    private var autoSlideRunnable: Runnable? = null
    private val autoSlideDelay = 4000L // 4 seconds per image
    private var isAutoSlideEnabled = true
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Hide system bars
        hideSystemUI()
        
        // Get data from intent
        val imagesList = intent.getParcelableArrayListExtra<GalleryImage>("images")
        images = imagesList?.toList() ?: emptyList()
        currentPosition = intent.getIntExtra("position", 0)
        
        setupCloseButton()
        setupViewPager()
        updateImageInfo()
        setupTapToToggle()
        startAutoSlide() // Begin slideshow automatically
    }
    // Handle close button
    private fun setupCloseButton() {
        binding.buttonClose.setOnClickListener {
            finish()
        }
    }
    // Initializes the ViewPager for swiping through images
   // Divya. “How to Create Android Auto Image Slider with Round Indicator Using Kotlin?” Stack Overflow, 26 May 2021, stackoverflow.com/questions/67699212/how-to-create-android-auto-image-slider-with-round-indicator-using-kotlin.
    private fun setupViewPager() {
        val adapter = ImageViewerAdapter(images)
        binding.viewPagerImages.adapter = adapter
        binding.viewPagerImages.setCurrentItem(currentPosition, false)
        
        // Update info when page changes
        binding.viewPagerImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position
                updateImageInfo()
                // Restart auto slide when user manually swipes
                restartAutoSlide()
            }
            
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                // Pause auto slide while user is scrolling
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    stopAutoSlide()
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    restartAutoSlide()
                }
            }
        })
    }
    // Updates image title, category, and mini description
    private fun updateImageInfo() {
        if (images.isNotEmpty() && currentPosition < images.size) {
            val image = images[currentPosition]
            binding.textImageTitle.text = image.title
            binding.textImageCategory.text = image.category
            binding.textImageCounter.text = "${currentPosition + 1} / ${images.size}"
        }
    }
    // Makes app fullscreen by hiding system UI elements
    //“Android: How to Hide the System UI Properly.” Stack Overflow, stackoverflow.com/questions/37380587/android-how-to-hide-the-system-ui-properly.
    private fun hideSystemUI() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.insetsController?.hide(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())
            window.insetsController?.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            )
        }
    }
    // Enables tapping the screen to show/hide UI elements
    //“Android: How to Hide the System UI Properly.” Stack Overflow, stackoverflow.com/questions/37380587/android-how-to-hide-the-system-ui-properly.
    private fun setupTapToToggle() {
        // Add click listener to the root layout to toggle UI
        binding.root.setOnClickListener {
            toggleUI()
            // Toggle auto slide when UI is toggled
            if (isUIVisible) {
                restartAutoSlide()
            } else {
                stopAutoSlide()
            }
        }
        
        // Prevent close button and bottom panel clicks from toggling UI
        binding.buttonClose.setOnClickListener { finish() }
        val bottomPanel = binding.root.findViewById<View>(com.senateway.guesthouse.R.id.layoutBottomInfo)
        bottomPanel?.setOnClickListener(null)
        
        // Prevent the top toolbar area from toggling UI
        val topToolbar = binding.root.findViewById<View>(com.senateway.guesthouse.R.id.layoutTopToolbar)
        topToolbar?.setOnClickListener(null)
    }
    // Starts automatic image switching at a fixed delay
    //Schnicke. “Switching Images with a Delay.” Stack Overflow, 11 May 2014, stackoverflow.com/questions/23597613/switching-images-with-a-delay.
    private fun startAutoSlide() {
        if (!isAutoSlideEnabled || images.isEmpty()) return
        
        autoSlideRunnable = Runnable {
            if (currentPosition < images.size - 1) {
                currentPosition++
            } else {
                // Loop back to first image
                currentPosition = 0
            }
            binding.viewPagerImages.setCurrentItem(currentPosition, true)
            startAutoSlide() // Schedule next slide
        }
        
        autoSlideHandler.postDelayed(autoSlideRunnable!!, autoSlideDelay)
    }
    // Stops slide show timer
    private fun stopAutoSlide() {
        autoSlideRunnable?.let {
            autoSlideHandler.removeCallbacks(it)
        }
        autoSlideRunnable = null
    }
    // Restarts slideshow when user stops interacting
    private fun restartAutoSlide() {
        stopAutoSlide()
        if (isAutoSlideEnabled && isUIVisible) {
            startAutoSlide()
        }
    }
    
    private fun toggleUI() {
        isUIVisible = !isUIVisible
        val topToolbar = binding.root.findViewById<View>(com.senateway.guesthouse.R.id.layoutTopToolbar)
        val bottomPanel = binding.root.findViewById<View>(com.senateway.guesthouse.R.id.layoutBottomInfo)
        
        if (isUIVisible) {
            topToolbar?.visibility = View.VISIBLE
            bottomPanel?.visibility = View.VISIBLE
        } else {
            topToolbar?.visibility = View.GONE
            bottomPanel?.visibility = View.GONE
        }
    }
    // Shows or hides top and bottom UI panels
    //“Android: How to Hide the System UI Properly.” Stack Overflow, stackoverflow.com/questions/37380587/android-how-to-hide-the-system-ui-properly.
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
            if (isAutoSlideEnabled) {
                restartAutoSlide()
            }
        } else {
            stopAutoSlide()
        }
    }
    // Stop slideshow when app goes to background
    override fun onPause() {
        super.onPause()
        stopAutoSlide()
    }
    
    override fun onResume() {
        super.onResume()
        if (isAutoSlideEnabled && isUIVisible) {
            startAutoSlide()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopAutoSlide()
    }
}

