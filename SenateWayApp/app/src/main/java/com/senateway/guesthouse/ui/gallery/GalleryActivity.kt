package com.senateway.guesthouse.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.senateway.guesthouse.databinding.ActivityGalleryBinding
import kotlinx.parcelize.Parcelize

// Data class holding all images in the gallery
@Parcelize
data class GalleryImage(
    val id: Int,
    val url: String, // Local oath
    val title: String, // Title of image
    val category: String // category
) : Parcelable

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var adapter: GalleryAdapter

    // Hardcoded gallery images for demo purposes and simplicity
    private val galleryImages = listOf(
        GalleryImage(1, "images/Senate1.jpg", "Main Exterior View", "Exterior"),
        GalleryImage(2, "images/Senate2.jpg", "Swimming Pool View", "Exterior"),
        GalleryImage(3, "images/Senate3.jpg", "Braai Area", "Exterior"),
        GalleryImage(4, "images/Senate4.jpg", "Dining Room", "Facilities"),
        GalleryImage(5, "images/Senate5.jpg", "Shared Kitchen", "Facilities"),
        GalleryImage(6, "images/Senate6.jpg", "Kitchen Detail", "Facilities"),
        GalleryImage(7, "images/Senate7.jpg", "Facilities Signage", "Facilities"),
        GalleryImage(8, "images/Senate8.jpg", "Bathroom Amenities", "Facilities"),
        GalleryImage(9, "images/Senate9.jpg", "Room Interior", "Rooms"),
        GalleryImage(10, "images/Senate10.jpg", "Deluxe Room", "Rooms"),
        GalleryImage(11, "images/Senate11.jpg", "Family Room", "Rooms"),
        GalleryImage(12, "images/Senate12.jpg", "Executive Suite", "Rooms"),
        GalleryImage(13, "images/Senate13.jpg", "Modern Bathroom", "Rooms"),
        GalleryImage(14, "images/Senate14.jpg", "Room View", "Rooms"),
        GalleryImage(15, "images/Senate15.jpg", "Outdoor Seating Area", "Facilities"),
        GalleryImage(16, "images/Senate16.jpg", "Garden Patio", "Exterior"),
        GalleryImage(17, "images/Senate17.jpg", "Parking Area", "Facilities"),
        GalleryImage(18, "images/Senate18.jpg", "Sun Terrace", "Facilities"),
        GalleryImage(19, "images/Senate19.jpg", "Twin Room", "Rooms"),
        GalleryImage(20, "images/Senate20.jpg", "Double Room", "Rooms"),
        GalleryImage(21, "images/Senate21.jpg", "Single Room", "Rooms"),
        GalleryImage(22, "images/Senate22.jpg", "Room Interior Design", "Rooms"),
        GalleryImage(23, "images/Senate23.jpg", "Room Amenities", "Rooms"),
        GalleryImage(24, "images/Senate24.jpg", "Balcony View", "Rooms"),
        GalleryImage(25, "images/Senate25.jpg", "Property Grounds", "Exterior"),
        GalleryImage(26, "images/Senate26.jpg", "Outdoor Space", "Exterior"),
        GalleryImage(27, "images/Senate27.jpg", "Swimming Pool Area", "Facilities"),
        GalleryImage(28, "images/Senate28.jpg", "Common Area", "Facilities"),
        GalleryImage(29, "images/Senate29.jpg", "Guesthouse Overview", "Exterior")
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar with back button and title
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Photo Gallery"
        
        setupRecyclerView()
    }
    
    private fun setupRecyclerView() {
        adapter = GalleryAdapter(galleryImages) { position ->
            openImageViewer(position)
        }
        // 3-column grid layout for better viewing of images
        val layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewGallery.layoutManager = layoutManager
        binding.recyclerViewGallery.adapter = adapter
        binding.recyclerViewGallery.setHasFixedSize(false)
    }
    
    private fun openImageViewer(position: Int) {
        val intent = Intent(this, ImageViewerActivity::class.java)
        intent.putParcelableArrayListExtra("images", ArrayList(galleryImages))
        intent.putExtra("position", position)
        startActivity(intent)
    }
    
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
