package com.senateway.guesthouse.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.senateway.guesthouse.R
import com.senateway.guesthouse.databinding.ActivityAdminLoginBinding

// Handles admin authentication using FirebaseAuth
//Coding With T. “Admin Registration & Login | Role Based Authentication | Flutter Firebase Authentication.” YouTube, 26 Sept. 2024, www.youtube.com/watch?v=fXbJrxaIYn0. Accessed 5 Nov. 2025.
class AdminLoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var auth: FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        auth = FirebaseAuth.getInstance()
        // Setup toolbar with back button and title
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Admin Login"
        // Return to previous screen when back button is clicked
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Trigger login when the button is pressed
        binding.buttonLogin.setOnClickListener {
            login()
        }
    }
    // Authenticates admin using email and password with FirebaseAuth
    //“Authenticate with Firebase Using Password-Based Accounts Using Javascript.” Firebase, firebase.google.com/docs/auth/web/password-auth.
    private fun login() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        // Prevent empty field submissions
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }
        // Sign in to Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                startActivity(Intent(this, AdminDashboardActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                // Show reason for failed authentication, useful for testin g
                Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

