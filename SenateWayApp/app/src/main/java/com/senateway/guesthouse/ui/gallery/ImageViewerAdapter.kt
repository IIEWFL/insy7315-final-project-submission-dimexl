package com.senateway.guesthouse.ui.gallery

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.senateway.guesthouse.databinding.ItemImageViewerBinding

class ImageViewerAdapter(
    private val images: List<GalleryImage>
) : RecyclerView.Adapter<ImageViewerAdapter.ImageViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        // Inflate the layout for each iage page in the viewer
        val binding = ItemImageViewerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }
    
    override fun getItemCount() = images.size
    
    inner class ImageViewHolder(private val binding: ItemImageViewerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(image: GalleryImage) {
            // Show a loading spinner while preparing the image
            binding.progressBar.visibility = View.VISIBLE
            
            try {
                // Convert "images/Senate1.jpg" to "senate1"
                //in. “Stack Overflow.” Stack Overflow, 19 Apr. 2016, stackoverflow.com/questions/36708191/convert-base64-to-png-jpeg-file-in-r. Accessed 4 Nov. 2025.
                val imageName = image.url
                    .replace("images/", "")
                    .replace(".jpg", "")
                    .lowercase()
                // Get the resource ID for the image
                @Suppress("DiscouragedApi")
                val resourceId = binding.root.context.resources.getIdentifier(
                    imageName,
                    "drawable",
                    binding.root.context.packageName
                )
                
                if (resourceId != 0) {
                    // Load image efficiently using Glide
                    //duded. “Efficient and Faster Way of Loading Images from Url.” Stack Overflow, 2 Aug. 2019, stackoverflow.com/questions/57330782/efficient-and-faster-way-of-loading-images-from-url.
                    Glide.with(binding.root.context)
                        .load(resourceId)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(binding.imageViewer)
                    
                    // Hide progress bar after a short delay (images load quickly from resources) (we were just experimenting )
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.progressBar.visibility = View.GONE
                    }, 300)
                } else {
                    // Fallback if the resource name wasn’t found
                    binding.progressBar.visibility = View.GONE
                    binding.imageViewer.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } catch (e: Exception) {
                // Handle any unexpected errors gracefully
                binding.progressBar.visibility = View.GONE
                binding.imageViewer.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }
    }
}

