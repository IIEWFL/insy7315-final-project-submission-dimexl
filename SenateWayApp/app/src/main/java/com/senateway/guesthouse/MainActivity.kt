package com.senateway.guesthouse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import com.senateway.guesthouse.databinding.ActivityMainBinding
import com.senateway.guesthouse.ui.admin.AdminLoginActivity
import com.senateway.guesthouse.utils.ThemeManager

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply saved theme before setting content view
        //Himanshu Gaur. “Kotlin Multiplatform KMP Dark Mode & Light Mode Colors | Material 3 Theme Setup for Android & IOS.” YouTube, 8 Sept. 2025, www.youtube.com/watch?v=3KfttXaiITw. Accessed 4 Nov. 2025.
        ThemeManager.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Check if admin route
        if (intent.data?.scheme == "admin" || intent.getBooleanExtra("admin", false)) {
            startActivity(Intent(this, AdminLoginActivity::class.java))
            finish()
            return
        }
        
        setupToolbar()
        setupNavigation()
        setupDrawer()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
    
    private fun setupDrawer() {
        drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.homeFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_rooms -> {
                    startActivity(Intent(this, com.senateway.guesthouse.ui.rooms.RoomsActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_gallery -> {
                    startActivity(Intent(this, com.senateway.guesthouse.ui.gallery.GalleryActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_reviews -> {
                    startActivity(Intent(this, com.senateway.guesthouse.ui.reviews.ReviewsActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_location -> {
                    startActivity(Intent(this, com.senateway.guesthouse.ui.map.MapActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_contact -> {
                    startActivity(Intent(this, com.senateway.guesthouse.ui.contact.ContactActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_chatbot -> {
                    startActivity(Intent(this, com.senateway.guesthouse.ui.chatbot.ChatbotActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_theme_toggle -> {
                    ThemeManager.toggleTheme(this)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_admin -> {
                    startActivity(Intent(this, AdminLoginActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
    }
    
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

