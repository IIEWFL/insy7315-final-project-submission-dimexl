package com.senateway.guesthouse.ui.rooms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.senateway.guesthouse.R
import com.senateway.guesthouse.data.model.Room
import com.senateway.guesthouse.databinding.ItemRoomBinding

class RoomsAdapter(
    private var rooms: List<Room>,
    private val onItemClick: (Room) -> Unit
) : RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>() {

    // Updates the list of rooms when filters change
    fun updateRooms(newRooms: List<Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        // Inflates the layout for each room card
        val binding = ItemRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoomViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(rooms[position])
    }
    // Number of items in the RecyclerView
    override fun getItemCount() = rooms.size

    inner class RoomViewHolder(private val binding: ItemRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(room: Room) {
            binding.textRoomName.text = room.name
            binding.textRoomPrice.text = "R${room.price}/night"
            binding.textRoomCapacity.text = "${room.capacity} guest${if (room.capacity > 1) "s" else ""}"
            binding.textRoomBeds.text = room.beds
            
            // Load image from drawable resources
            try {

                val imageName = room.image
                    .replace("images/", "")
                    .replace(".jpg", "")
                    .lowercase()

                // Get resource ID by name
                val resourceId = binding.root.context.resources.getIdentifier(
                    imageName,
                    "drawable",
                    binding.root.context.packageName
                )
                // If found, load image with Glide otherwise use placeholder
                //“How to Set a Profile Picture If It Exists Using Glide, Otherwise Use a Placeholder?” Stack Overflow, stackoverflow.com/questions/71901847/how-to-set-a-profile-picture-if-it-exists-using-glide-otherwise-use-a-placehold.
                if (resourceId != 0) {
                    Glide.with(binding.root.context)
                        .load(resourceId)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(binding.imageRoom)
                } else {
                    binding.imageRoom.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } catch (e: Exception) {
                binding.imageRoom.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            binding.layoutAmenities.removeAllViews()

            addAmenitiesInRows(room.amenities)

            binding.root.post {
                if (binding.layoutAmenities.width > 0) {
                    optimizeAmenitiesLayout(room.amenities, binding.layoutAmenities.width)
                }
            }
            // Handle click on "Book Now" button
            binding.buttonBookNow.setOnClickListener {
                onItemClick(room)
            }
            // Also handle click anywhere on the room card
            binding.root.setOnClickListener {
                onItemClick(room)
            }
        }

        // Adds amenities to the layout in rows (3 items per row by default)
        private fun addAmenitiesInRows(amenities: List<String>) {
            binding.layoutAmenities.removeAllViews()

            val estimatedItemsPerRow = 3
            // Splits amenities list into groups of 3 for each row
            amenities.chunked(estimatedItemsPerRow).forEach { rowAmenities ->
                val row = android.widget.LinearLayout(binding.root.context).apply {
                    orientation = android.widget.LinearLayout.HORIZONTAL
                    layoutParams = android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = 6.dpToPx()
                    }
                }
                // Add each amenity chip into the row
                rowAmenities.forEachIndexed { index, amenity ->
                    val chip = createAmenityChip(amenity)
                    val layoutParams = android.widget.LinearLayout.LayoutParams(
                        0,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    ).apply {
                        marginEnd = if (index < rowAmenities.size - 1) 4.dpToPx() else 0
                    }
                    row.addView(chip, layoutParams)
                }
                
                binding.layoutAmenities.addView(row)
            }
        }
        
        private fun optimizeAmenitiesLayout(amenities: List<String>, parentWidth: Int) {

        }
        // Creates a chip style TextView for each amenity
        //https://tutorialwing.com/create-android-filter-chip-programmatically-in-kotlin/
        private fun createAmenityChip(amenity: String): android.widget.TextView {
            return android.widget.TextView(binding.root.context).apply {
                text = amenity
                setPadding(8.dpToPx(), 6.dpToPx(), 8.dpToPx(), 6.dpToPx())
                setBackgroundResource(R.drawable.amenity_chip_background)
                setTextColor(binding.root.context.getColor(R.color.text_secondary))
                textSize = 11f
                gravity = android.view.Gravity.CENTER_VERTICAL
            }
        }
        // Converts dp (density independent pixels) to actual pixels
        //SilentKnight. “How to Convert DP, PX, SP among Each Other, Especially DP and SP?” Stack Overflow, 16 Apr. 2015, stackoverflow.com/questions/29664993/how-to-convert-dp-px-sp-among-each-other-especially-dp-and-sp.
        private fun Int.dpToPx(): Int {
            return (this * binding.root.context.resources.displayMetrics.density).toInt()
        }
    }
}

