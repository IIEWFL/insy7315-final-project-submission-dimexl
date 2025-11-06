package com.senateway.guesthouse.ui.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.senateway.guesthouse.config.FirebaseConfig
import com.senateway.guesthouse.data.model.Room
import com.senateway.guesthouse.databinding.ActivityRoomsBinding
import com.senateway.guesthouse.ui.contact.ContactActivity
import java.util.Date

class RoomsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRoomsBinding
    private lateinit var adapter: RoomsAdapter

    // Predefined list of all available rooms
    private val allRooms = listOf(
        Room(1, "Standard Single Room", "small", 1, "1 Single Bed", 450, "images/Senate14.jpg", listOf("WiFi", "Air Conditioning", "Private Bathroom", "TV", "Coffee Maker")),
        Room(2, "Standard Double Room", "medium", 2, "1 Double Bed", 650, "images/Senate11.jpg", listOf("WiFi", "Air Conditioning", "Private Bathroom", "TV", "Coffee Maker")),
        Room(3, "Deluxe Double Room", "large", 2, "1 Queen Bed", 850, "images/Senate20.jpg", listOf("WiFi", "Air Conditioning", "Private Bathroom", "TV", "Mini Fridge", "Coffee Maker", "Pool View")),
        Room(4, "Twin Room", "medium", 2, "2 Single Beds", 700, "images/Senate17.jpg", listOf("WiFi", "Air Conditioning", "Private Bathroom", "TV", "Coffee Maker")),
        Room(5, "Family Room", "large", 4, "1 Double + 2 Singles", 1200, "images/Senate20.jpg", listOf("WiFi", "Air Conditioning", "Private Bathroom", "TV", "Mini Fridge", "Coffee Maker")),
        Room(6, "Superior Room", "large", 2, "1 King Bed", 950, "images/Senate18.jpg", listOf("WiFi", "Air Conditioning", "Private Bathroom", "TV", "Mini Fridge", "Coffee Maker")),
        Room(7, "Economy Single Room", "small", 1, "1 Single Bed", 400, "images/Senate23.jpg", listOf("WiFi", "Air Conditioning", "Shared Bathroom", "Coffee Maker")),
        Room(8, "Deluxe Twin Room", "medium", 2, "2 Single Beds", 800, "images/Senate29.jpg", listOf("WiFi", "Air Conditioning", "Private Bathroom", "TV", "Mini Fridge", "Coffee Maker")),
        Room(9, "Executive Suite", "large", 2, "1 King Bed", 1400, "images/Senate14.jpg", listOf("WiFi", "Air Conditioning", "Private Bathroom", "TV", "Mini Fridge", "Coffee Maker", "Balcony")),
        Room(10, "Family Suite", "large", 5, "2 Double Beds + 1 Single", 1600, "images/Senate10.jpg", listOf("WiFi", "Air Conditioning", "Private Bathroom", "TV", "Mini Fridge", "Coffee Maker", "Living Area"))
    )
    
    private var filteredRooms = allRooms
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize UI components
        setupToolbar()
        setupFilters()
        setupRecyclerView()
    }
    // Sets up toolbar with a back button and title
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Our Rooms"
    }
    // Sets up filtering options, filter by size spinner, capacity and price sliders
    //Malick, Ali. “How to Make This Slideable Filters in Android.” Stack Overflow, 25 Oct. 2020, stackoverflow.com/questions/64526082/how-to-make-this-slideable-filters-in-android.
    private fun setupFilters() {
        // Setup size filter spinner
        val sizeOptions = arrayOf("All Sizes", "Small", "Medium", "Large")
        val sizeAdapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, sizeOptions)
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSizeFilter.adapter = sizeAdapter
        
        // Size filter
        binding.spinnerSizeFilter.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                applyFilters()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        
        // Capacity slider
        binding.sliderCapacity.addOnChangeListener { _, _, _ ->
            binding.textCapacityValue.text = "Minimum Capacity: ${binding.sliderCapacity.value.toInt()} guest(s)"
            applyFilters()
        }
        
        // Price slider
        binding.sliderPrice.addOnChangeListener { _, _, _ ->
            binding.textPriceValue.text = "Max Price: R${binding.sliderPrice.value.toInt()}"
            applyFilters()
        }
        
        // Clear filters button
        binding.buttonClearFilters.setOnClickListener {
            binding.spinnerSizeFilter.setSelection(0)
            binding.sliderCapacity.value = 1f
            binding.sliderPrice.value = 1600f
            applyFilters()
        }
        
        // Initialize filter display
        binding.textCapacityValue.text = "Minimum Capacity: 1 guest(s)"
        binding.textPriceValue.text = "Max Price: R1600"
        binding.textFilteredCount.text = "Showing ${allRooms.size} of ${allRooms.size} rooms"
    }
    // Applies the selected filters to the list of rooms
    private fun applyFilters() {
        val sizeFilter = binding.spinnerSizeFilter.selectedItem.toString()
        val capacityFilter = binding.sliderCapacity.value.toInt()
        val priceFilter = binding.sliderPrice.value.toInt()

        // Filter logic to checks size, capacity, and price
        filteredRooms = allRooms.filter { room ->
            val sizeMatch = sizeFilter == "All Sizes" || room.size == sizeFilter.lowercase()
            val capacityMatch = room.capacity >= capacityFilter
            val priceMatch = room.price <= priceFilter
            sizeMatch && capacityMatch && priceMatch
        }
        
        binding.textFilteredCount.text = "Showing ${filteredRooms.size} of ${allRooms.size} rooms"
        adapter.updateRooms(filteredRooms) // Update the displayed list
    }
    // Sets up RecyclerView and handles room click actions
    private fun setupRecyclerView() {
        adapter = RoomsAdapter(filteredRooms) { room ->
            // Save booking to Firebase and navigate to contact
            val bookingData = hashMapOf<String, Any>(
                "roomName" to room.name,
                "roomId" to room.id,
                "price" to room.price,
                "capacity" to room.capacity,
                "status" to "pending",
                "timestamp" to Date().toString(),
                "created" to System.currentTimeMillis(),
                "source" to "rooms_page"
            )
            //Push booking data into the "ookings node in Firebase Realtime Database
            //Shmoes. “Firebase Database Get Specific Values of Child Using Map.” Stack Overflow, 9 Apr. 2018, stackoverflow.com/questions/49724137/firebase-database-get-specific-values-of-child-using-map.
            FirebaseConfig.database.reference.child("bookings").push()
                .setValue(bookingData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Thank you for your interest in ${room.name}! Please fill out the contact form to complete your booking.", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, ContactActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error saving booking. Please try again.", Toast.LENGTH_SHORT).show()
                }
        }
        // RecyclerView setup which displays rooms in a vertical list
        binding.recyclerViewRooms.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.recyclerViewRooms.adapter = adapter
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
