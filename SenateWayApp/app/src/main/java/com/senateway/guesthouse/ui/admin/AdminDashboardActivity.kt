package com.senateway.guesthouse.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.senateway.guesthouse.MainActivity
import com.senateway.guesthouse.config.FirebaseConfig
import com.senateway.guesthouse.databinding.ActivityAdminDashboardBinding
import com.senateway.guesthouse.utils.EmailService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Booking(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val checkIn: String,
    val checkOut: String,
    val guests: Long,
    val status: String,
    val created: Long,
    val message: String = ""
)
//data model representing a single booking record from Firebase
//Firebase. “Cloud Firestore Data Model.” Firebase, firebase.google.com/docs/firestore/data-model.
class AdminDashboardActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var bookingsAdapter: BookingsAdapter
    private val bookings = mutableListOf<Booking>() //List of all bookings loaded from Firebase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        auth = FirebaseAuth.getInstance()

        //Redirect to login if the admin is not authenticated
        //Laracasts, 2025, laracasts.com/discuss/channels/filament/filament-redirect-unauthenticated-and-non-admin-users-from-admin-to-a-certain-other-route. Accessed 5 Nov. 2025.
        if (auth.currentUser == null) {
            startActivity(Intent(this, AdminLoginActivity::class.java))
            finish()
            return
        }
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Admin Dashboard"
        
        setupViews()
        loadBookings()
        loadAnalytics()
    }
    
    private fun setupViews() {
        //Handles sign-out and redirects back to main screen
        binding.buttonSignOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        //Switch to Analytics tab
        binding.buttonAnalytics.setOnClickListener {
            binding.tabAnalytics.visibility = android.view.View.VISIBLE
            binding.tabBookings.visibility = android.view.View.GONE
            binding.buttonAnalytics.isSelected = true
            binding.buttonBookings.isSelected = false
        }
        // Switch to Bookings tab
        binding.buttonBookings.setOnClickListener {
            binding.tabAnalytics.visibility = android.view.View.GONE
            binding.tabBookings.visibility = android.view.View.VISIBLE
            binding.buttonAnalytics.isSelected = false
            binding.buttonBookings.isSelected = true
        }
        
        // Setup bookings RecyclerView
        bookingsAdapter = BookingsAdapter(
            bookings = bookings,
            onConfirm = { booking -> confirmBooking(booking) },
            onDecline = { booking -> declineBooking(booking) },
            onDelete = { booking -> deleteBooking(booking) }
        )
        binding.recyclerViewBookings.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewBookings.adapter = bookingsAdapter
        
        // Default to Analytics tab
        binding.buttonAnalytics.isSelected = true
        binding.tabBookings.visibility = android.view.View.GONE
    }
    
    private fun loadBookings() {
        val bookingsRef = FirebaseConfig.database.reference.child("bookings")
        // Listen for real-time booking data updates
        //Google. “Get Realtime Updates with Cloud Firestore.” Firebase, firebase.google.com/docs/firestore/query-data/listen.
        bookingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookings.clear()
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        // Handle guests field - can be String, Long, or Int
                        val guestsValue = child.child("guests").value
                        val guests = when (guestsValue) {
                            is Long -> guestsValue
                            is Number -> guestsValue.toLong()
                            is String -> guestsValue.toLongOrNull() ?: 1L
                            else -> 1L
                        }
                        
                        // Handle created field - can be Long or String
                        val createdValue = child.child("created").value
                        val created = when (createdValue) {
                            is Long -> createdValue
                            is Number -> createdValue.toLong()
                            is String -> createdValue.toLongOrNull() ?: System.currentTimeMillis()
                            else -> System.currentTimeMillis()
                        }
                        // Create a Booking object from Firebase dataa
                        val booking = Booking(
                            child.key ?: "",
                            child.child("name").getValue(String::class.java) ?: "",
                            child.child("email").getValue(String::class.java) ?: "",
                            child.child("phone").getValue(String::class.java) ?: "",
                            child.child("checkIn").getValue(String::class.java) ?: "",
                            child.child("checkOut").getValue(String::class.java) ?: "",
                            guests,
                            child.child("status").getValue(String::class.java) ?: "pending",
                            created,
                            child.child("message").getValue(String::class.java) ?: ""
                        )
                        bookings.add(booking)
                    }
                    // Sort by created date (newest first)
                    bookings.sortByDescending { it.created }
                }
                bookingsAdapter.notifyDataSetChanged()
                binding.textTotalBookings.text = "Total: ${bookings.size}"
            }
            
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminDashboardActivity, "Error loading bookings", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun loadAnalytics() {
        val bookingsRef = FirebaseConfig.database.reference.child("bookings")
        // Fetch bookings once to calculate totals and pending stats
        //Aaditya. “How to Perform Some Calculations in Firebase Realtime Database before Fetching Data and Fetch Data Accordilngly.” Stack Overflow, 28 Oct. 2022, stackoverflow.com/questions/74231001/how-to-perform-some-calculations-in-firebase-realtime-database-before-fetching-d.
        bookingsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalBookings = 0
                var pendingBookings = 0
                
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        totalBookings++
                        val status = child.child("status").getValue(String::class.java) ?: "pending"
                        if (status == "pending") pendingBookings++
                    }
                }
                // Update dashboard stats
                binding.textTotalBookingsCount.text = totalBookings.toString()
                binding.textPendingBookingsCount.text = pendingBookings.toString()
                binding.textConfirmedBookingsCount.text = (totalBookings - pendingBookings).toString()
            }
            
            override fun onCancelled(error: DatabaseError) {

            }
        })
        
        // Load reviews count
        val reviewsRef = FirebaseConfig.database.reference.child("reviews")
        reviewsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviewCount = if (snapshot.exists()) snapshot.children.count() else 0
                binding.textTotalReviewsCount.text = (reviewCount + 6).toString() // Include default reviews
            }
            
            override fun onCancelled(error: DatabaseError) {
                binding.textTotalReviewsCount.text = "6"
            }
        })
    }
    // Marks a booking as confirmed and sends an email to the guest
    //JamesPierson. “How to Send an Email with Emailjs?” Stack Overflow, 11 Oct. 2019, stackoverflow.com/questions/58348989/how-to-send-an-email-with-emailjs.
    private fun confirmBooking(booking: Booking) {
        val bookingRef = FirebaseConfig.database.reference.child("bookings").child(booking.id)
        bookingRef.child("status").setValue("confirmed")
            .addOnSuccessListener {
                // Send confirmation email
                CoroutineScope(Dispatchers.Main).launch {
                    val emailResult = EmailService.sendBookingConfirmationEmail(
                        context = this@AdminDashboardActivity,
                        guestName = booking.name,
                        guestEmail = booking.email,
                        phone = booking.phone,
                        checkIn = booking.checkIn,
                        checkOut = booking.checkOut,
                        guests = booking.guests.toString(),
                        message = booking.message
                    )
                    
                    if (emailResult.isSuccess) {
                        Toast.makeText(this@AdminDashboardActivity, "Booking confirmed! Confirmation email sent to ${booking.email}", Toast.LENGTH_LONG).show()
                    } else {
                        val errorMsg = emailResult.exceptionOrNull()?.message ?: "Unknown error"
                        android.util.Log.e("AdminDashboard", "Email send failed: $errorMsg")
                        Toast.makeText(this@AdminDashboardActivity, "Booking confirmed, but email failed: $errorMsg", Toast.LENGTH_LONG).show()
                    }
                    loadAnalytics() // Refresh analytics
                }
            }
            // Handle error gracefully
            .addOnFailureListener {
                Toast.makeText(this, "Failed to confirm booking: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    // MArk booking as declined
    private fun declineBooking(booking: Booking) {
        val bookingRef = FirebaseConfig.database.reference.child("bookings").child(booking.id)
        bookingRef.child("status").setValue("declined")
            .addOnSuccessListener {
                Toast.makeText(this, "Booking declined", Toast.LENGTH_SHORT).show()
                loadAnalytics() // Refresh analytics
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to decline booking: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    
    private fun deleteBooking(booking: Booking) {
        // Show confirmation dialog
        //“Window Confirm() Method.” W3schools.com, 2019, www.w3schools.com/jsref/met_win_confirm.asp.
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Delete Booking")
            .setMessage("Are you sure you want to delete this booking request? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                val bookingRef = FirebaseConfig.database.reference.child("bookings").child(booking.id)
                bookingRef.removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Booking deleted successfully", Toast.LENGTH_SHORT).show()
                        loadAnalytics() // Refresh analytics
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to delete booking: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
