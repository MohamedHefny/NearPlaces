package com.mohamedhefny.nearplaces.dataSource.Entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Place(val venue: Venue)

data class Venue(
    @SerializedName("id")
    val placeId: String,
    val name: String,
    @Expose(serialize = false, deserialize = false)
    val image: String = "N/A",
    val location: Location
)

data class Location(val address: String)