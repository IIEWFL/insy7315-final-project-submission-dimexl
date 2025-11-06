package com.senateway.guesthouse.ui.reviews

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.senateway.guesthouse.config.FirebaseConfig
import com.senateway.guesthouse.databinding.ActivityReviewsBinding
import com.senateway.guesthouse.databinding.DialogReviewFormBinding

// Data class representing a single review entry
data class Review(
    val id: String,
    val name: String,
    val rating: Int,
    val date: String,
    val comment: String,
    val category: String
)

class ReviewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewsBinding
    private lateinit var adapter: ReviewsAdapter

    // Default reviews to display when Firebase is empty
    private val defaultReviews = listOf(
        Review("1", "Tumelo Makowa", 5, "October 2025", "Absolutely wonderful stay! The rooms were spotlessly clean and the staff went above and beyond to make us feel welcome. The swimming pool was a highlight, and the location is perfect for exploring Kimberley.", "Couple"),
        Review("2", "Sahil Ramesar", 5, "September 2025", "Great value for money. The air conditioning worked perfectly, WiFi was fast, and having free parking made everything so convenient. Would definitely recommend to anyone visiting Kimberley.", "Business Traveler"),
        Review("3", "Wendy Westhuizen", 4, "September 2025", "Lovely guesthouse with excellent facilities. The shared kitchen and BBQ area were great for our family gathering. Close to all major attractions and shopping centers.", "Family"),
        Review("4", "David Moyo", 5, "August 2025", "The cleanliness and comfort exceeded expectations. Private bathroom was modern and well-maintained. The outdoor terrace is perfect for relaxing after a day of sightseeing.", "Solo Traveler"),
        Review("5", "Lisa Moodley", 5, "August 2025", "Perfect location near the airport and city center. Staff support was exceptional - they helped us plan our entire itinerary. The rooms are modern and very comfortable.", "Couple"),
        Review("6", "James Merwe", 4, "July 2025", "Highly rated for good reason. Clean, comfortable, and well-located. The outdoor fireplace area was a nice touch. Will definitely stay here again on our next visit.", "Family")
    )
    // List holding both default and Firebase reviews
    private val allReviews = mutableListOf<Review>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Setup toolbar with back navigation
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Guest Reviews"
        
        setupRecyclerView()
        loadReviews() // Load from Firebase and defaults
        
        binding.buttonWriteReview.setOnClickListener {
            showReviewDialog()
        }
    }
    
    private fun setupRecyclerView() {
        adapter = ReviewsAdapter(allReviews)
        binding.recyclerViewReviews.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewReviews.adapter = adapter
    }
    
    private fun loadReviews() {
        allReviews.clear()
        allReviews.addAll(defaultReviews)
        
        val reviewsRef = FirebaseConfig.database.reference.child("reviews")
        reviewsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val review = Review(
                            child.key ?: "",
                            child.child("name").getValue(String::class.java) ?: "",
                            child.child("rating").getValue(Int::class.java) ?: 5,
                            child.child("date").getValue(String::class.java) ?: "",
                            child.child("comment").getValue(String::class.java) ?: "",
                            child.child("category").getValue(String::class.java) ?: "Guest"
                        )
                        allReviews.add(review)
                    }
                }
                // If Firebase read fails, still show local reviews so its not empty
                updateRatingSummary()
                adapter.notifyDataSetChanged()
            }
            
            override fun onCancelled(error: DatabaseError) {
                updateRatingSummary()
            }
        })
    }
    // Calculates and displays the average rating and total count
    //Kahn, Nick. “How to Calculate Average Rating.” Stack Overflow, 12 July 2011, stackoverflow.com/questions/6659036/how-to-calculate-average-rating.
    private fun updateRatingSummary() {
        val averageRating = if (allReviews.isNotEmpty()) {
            allReviews.map { it.rating }.average().toFloat()
        } else {
            4.8f
        }
        binding.textAverageRating.text = String.format("%.1f", averageRating)
        binding.textTotalReviewsCount.text = "${allReviews.size} reviews"
    }
    // Displays dialog for user to write and submit a review
    private fun showReviewDialog() {
        val dialogBinding = DialogReviewFormBinding.inflate(layoutInflater)
        
        // Setup category spinner
        val categories = arrayOf("Guest", "Couple", "Family", "Business Traveler", "Solo Traveler", "Group")
        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerCategory.adapter = adapter
        
        val dialog = AlertDialog.Builder(this)
            .setTitle("Write a Review")
            .setView(dialogBinding.root)
            .setPositiveButton("Submit") { _, _ ->
                submitReview(dialogBinding)
            }
            .setNegativeButton("Cancel", null)
            .create()

        // Live update rating text when user adjusts RatingBar
        //Jexcy. “How to Make a Ratingbar with Real Time Update in Android?” Stack Overflow, 13 Sept. 2011, stackoverflow.com/questions/7407363/how-to-make-a-ratingbar-with-real-time-update-in-android.
        dialogBinding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            dialogBinding.textRatingValue.text = "${rating.toInt()} / 5"
        }
        
        dialog.show()
    }
    // Submits the review to Firebase Realtime Database
    private fun submitReview(binding: DialogReviewFormBinding) {
        val name = binding.editTextName.text.toString().trim()
        val comment = binding.editTextComment.text.toString().trim()
        val rating = binding.ratingBar.rating.toInt()
        val category = binding.spinnerCategory.selectedItem.toString()

        // Basic validation
        if (name.isEmpty() || comment.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        // Prepare review data map for Firebase
        val reviewData = hashMapOf<String, Any>(
            "name" to name,
            "rating" to rating,
            "comment" to comment,
            "category" to category,
            "date" to java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
            "timestamp" to System.currentTimeMillis()
        )
        // Push review data to Firebase reviews node
        FirebaseConfig.database.reference.child("reviews").push()
            .setValue(reviewData)
            .addOnSuccessListener {
                Toast.makeText(this, "Thank you for your review!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to submit review. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
