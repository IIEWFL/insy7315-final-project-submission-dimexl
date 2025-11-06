package com.senateway.guesthouse.data.model

//data model for holding all the variables used by the rooms page
data class Room(
    val id: Int,
    val name: String,
    val size: String, // "small", "medium", "large"
    val capacity: Int,
    val beds: String,
    val price: Int,
    val image: String,
    val amenities: List<String>
)

