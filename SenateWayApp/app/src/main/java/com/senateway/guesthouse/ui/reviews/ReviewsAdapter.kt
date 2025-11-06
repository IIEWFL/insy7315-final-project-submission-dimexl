package com.senateway.guesthouse.ui.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senateway.guesthouse.databinding.ItemReviewBinding

// Adapter class for displaying a list of reviews in a RecyclerView
//Android Developers. “Create Dynamic Lists with RecyclerView.” Android Developers, developer.android.com/develop/ui/views/layout/recyclerview.
class ReviewsAdapter(
    private val reviews: List<Review>
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    // Creates a new ViewHolder when there are no existing ones to reuse
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }
    // Binds review data to each item view in the list
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }
    // Returns total number of reviews in the list
    override fun getItemCount() = reviews.size

    // ViewHolder class holds reference to UI components for a single review item
    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Populates the UI with data from a Review object
        fun bind(review: Review) {
            binding.textReviewerName.text = review.name
            binding.textReviewCategory.text = review.category
            binding.textReviewDate.text = review.date
            binding.textReviewComment.text = review.comment
            
            // Set reviewer initials for avatar
            val initials = review.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
            binding.textReviewerInitials.text = initials.uppercase()
            
            // Set rating stars
            val rating = review.rating
            binding.ratingBar.rating = rating.toFloat()
            binding.ratingBar.isEnabled = false
        }
    }
}

