package com.senateway.guesthouse.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.senateway.guesthouse.databinding.FragmentHomeBinding
import com.senateway.guesthouse.ui.rooms.RoomsActivity
import com.senateway.guesthouse.ui.contact.ContactActivity

class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel for the home fragment
    private lateinit var viewModel: HomeViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using view binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        
        setupViews()
        observeViewModel()
    }
    
    private fun setupViews() {
        binding.buttonViewRooms.setOnClickListener {
            startActivity(Intent(requireContext(), RoomsActivity::class.java))
        }
        
        binding.buttonBookNow.setOnClickListener {
            startActivity(Intent(requireContext(), ContactActivity::class.java))
        }
        
        // Load hero image with Glide
        //“Loading Image Using Glide in Android.” Stack Overflow, stackoverflow.com/questions/41320925/loading-image-using-glide-in-android.
        try {
            val resourceId = resources.getIdentifier("senate1", "drawable", requireContext().packageName)
            if (resourceId != 0) {
                com.bumptech.glide.Glide.with(requireContext())
                    .load(resourceId)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(binding.imageHero)
            } else {
                // Use placeholder if image not found
                binding.imageHero.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        } catch (e: Exception) {
            binding.imageHero.setImageResource(android.R.drawable.ic_menu_gallery)
        }
        
        // Load about image with Glide
        //“Loading Image Using Glide in Android.” Stack Overflow, stackoverflow.com/questions/41320925/loading-image-using-glide-in-android.
        try {
            val aboutResourceId = resources.getIdentifier("senate27", "drawable", requireContext().packageName)
            if (aboutResourceId != 0) {
                com.bumptech.glide.Glide.with(requireContext())
                    .load(aboutResourceId)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(binding.imageAbout)
            }
        } catch (e: Exception) {
            // Image will use default background
        }
        
        // Load rating data
        viewModel.loadRatingData()
    }
    
    private fun observeViewModel() {
        // Observe changes to average rating and update UI dynamically
        //https://stackoverflow.com/questions/72516711/android-simplest-way-to-update-ui-when-parameter-changes
        viewModel.averageRating.observe(viewLifecycleOwner) { rating ->
            binding.textRating.text = String.format("%.1f", rating)
            binding.textRatingHighlight.text = "Rated ${String.format("%.1f", rating)}/5"
        }
        
        viewModel.totalReviews.observe(viewLifecycleOwner) { total ->
            binding.textTotalReviews.text = "$total reviews"
            binding.textReviewsHighlight.text = "Based on $total verified guest reviews"
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

