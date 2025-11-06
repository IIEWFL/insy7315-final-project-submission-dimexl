package com.senateway.guesthouse.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senateway.guesthouse.R
import com.senateway.guesthouse.databinding.ItemGalleryBinding

class GalleryAdapter(
    private val images: List<GalleryImage>, // List of images
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    
    init {
        setHasStableIds(true)
    //Islam, Forhadul. “Best Practices for Efficient RecyclerView Performance in Android.” Medium, 18 Sept. 2024, medium.com/@fitareq/best-practices-for-efficient-recyclerview-performance-in-android-b405eedc6d34. Accessed 4 Nov. 2025.
    // Helps RecyclerView optimize performance for stable items
    }
    
    override fun getItemId(position: Int): Long {
        // Ensures each item has a unique, consistent ID
        return images[position].id.toLong()
    }

    // Maps each image ID to its corresponding drawable resource
    private fun getDrawableResourceId(imageId: Int): Int {
        return when (imageId) {
            1 -> R.drawable.senate1
            2 -> R.drawable.senate2
            3 -> R.drawable.senate3
            4 -> R.drawable.senate4
            5 -> R.drawable.senate5
            6 -> R.drawable.senate6
            7 -> R.drawable.senate7
            8 -> R.drawable.senate8
            9 -> R.drawable.senate9
            10 -> R.drawable.senate10
            11 -> R.drawable.senate11
            12 -> R.drawable.senate12
            13 -> R.drawable.senate13
            14 -> R.drawable.senate14
            15 -> R.drawable.senate15
            16 -> R.drawable.senate16
            17 -> R.drawable.senate17
            18 -> R.drawable.senate18
            19 -> R.drawable.senate19
            20 -> R.drawable.senate20
            21 -> R.drawable.senate21
            22 -> R.drawable.senate22
            23 -> R.drawable.senate23
            24 -> R.drawable.senate24
            25 -> R.drawable.senate25
            26 -> R.drawable.senate26
            27 -> R.drawable.senate27
            28 -> R.drawable.senate28
            29 -> R.drawable.senate29
            else -> android.R.drawable.ic_menu_gallery // Default fallback icon just in case an image is missing
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        // Creates a new view holder with the layout
        return GalleryViewHolder(binding)
    }
    // Binds image data to the view holder
    //dazai. “Load Image from Url in Viewholder.” Stack Overflow, 6 Aug. 2021, stackoverflow.com/questions/68683282/load-image-from-url-in-viewholder.
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(images[position], position)
    }
    // Returns number of images to display eg, 28/29
    override fun getItemCount() = images.size
    
    inner class GalleryViewHolder(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(image: GalleryImage, position: Int) {
        // Clears previous image to prevent flicker
            binding.imageGallery.setImageDrawable(null)
            

            binding.root.setOnClickListener {
                // Handles click events for each imag
                onItemClick(position)
            }
            // Retrieves corresponding drawable
            val resourceId = getDrawableResourceId(image.id)
            // Sets the image to ImageView
            binding.imageGallery.setImageResource(resourceId)
        }
    }
}

