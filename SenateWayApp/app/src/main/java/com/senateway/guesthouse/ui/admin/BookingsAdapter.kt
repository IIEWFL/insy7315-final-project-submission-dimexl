package com.senateway.guesthouse.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senateway.guesthouse.databinding.ItemBookingBinding
import java.text.SimpleDateFormat
import java.util.*

// Adapter to display a list of bookings with admin actions (confirm, decline, delete)
//Stornelli, Bishma. “ActiveAdmin Action Items Depending on Status of Data.” Stack Overflow, 1 Oct. 2012, stackoverflow.com/questions/12680145/activeadmin-action-items-depending-on-status-of-data.
class BookingsAdapter(
    private val bookings: List<Booking>,
    private val onConfirm: (Booking) -> Unit, // Display list of bookings
    private val onDecline: (Booking) -> Unit, // Display confirmed list
    private val onDelete: (Booking) -> Unit // Display declined list
) : RecyclerView.Adapter<BookingsAdapter.BookingViewHolder>() { //Callback when booking is deleteed
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookingViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        // Bind bookig data to item view
        //groo. “Use of Binding Breaks RecyclerView Item Layout.” Stack Overflow, 23 Jan. 2021, stackoverflow.com/questions/65865527/use-of-binding-breaks-recyclerview-item-layout.
        holder.bind(bookings[position])
    }
    
    override fun getItemCount() = bookings.size // Total number of bookings in the list
    // ViewHolder class for binding booking data to the item layout
    inner class BookingViewHolder(private val binding: ItemBookingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Populate booking info in the item layout
        fun bind(booking: Booking) {
            binding.textBookingName.text = booking.name
            binding.textBookingEmail.text = booking.email
            binding.textBookingPhone.text = booking.phone
            binding.textBookingDates.text = "Check-in: ${booking.checkIn} - Check-out: ${booking.checkOut}"
            binding.textBookingGuests.text = "${booking.guests} guest(s)"
            binding.textBookingStatus.text = booking.status.replaceFirstChar { it.uppercase() }

            // Format and display the booking creation date
            //G, Nina. “Format Created Date and Modified Date Columns to Display Normal Time.” Stack Overflow, 27 Oct. 2015, stackoverflow.com/questions/33376563/format-created-date-and-modified-date-columns-to-display-normal-time.
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val createdDate = Date(booking.created)
            binding.textBookingDate.text = "Submitted: ${dateFormat.format(createdDate)}"
            
            // Set status color
            val statusColor = when (booking.status.lowercase()) {
                "pending" -> android.graphics.Color.parseColor("#FF9800") // Pending
                "confirmed" -> android.graphics.Color.parseColor("#4CAF50") // Confirmed
                "cancelled", "declined" -> android.graphics.Color.parseColor("#F44336") //Declined
                else -> android.graphics.Color.parseColor("#757575") // Unkniwn
            }
            binding.textBookingStatus.setTextColor(statusColor)
            
            // Show/hide action buttons based on status
            val isPending = booking.status.lowercase() == "pending"
            binding.buttonConfirm.visibility = if (isPending) android.view.View.VISIBLE else android.view.View.GONE
            binding.buttonDecline.visibility = if (isPending) android.view.View.VISIBLE else android.view.View.GONE
            
            // Set button click listeners
            binding.buttonConfirm.setOnClickListener {
                onConfirm(booking)
            }
            
            binding.buttonDecline.setOnClickListener {
                onDecline(booking)
            }
            
            binding.buttonDelete.setOnClickListener {
                onDelete(booking)
            }
        }
    }
}

