package com.mohamedhefny.nearplaces.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamedhefny.nearplaces.R
import com.mohamedhefny.nearplaces.dataSource.Entities.Place
import kotlinx.android.synthetic.main.item_place.view.*

class HomePlacesAdapter(private val places: MutableList<Place>) :
    RecyclerView.Adapter<HomePlacesAdapter.PlaceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_place, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.placeName.text = places[position].venue.name
        holder.placeAddress.text = places[position].venue.location.address
    }

    override fun getItemCount(): Int {
        return places.size
    }

    fun setPlacesList(placesList: List<Place>) {
        places.addAll(placesList)
        notifyDataSetChanged()
    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName = itemView.item_place_name
        val placeAddress = itemView.item_place_address
        val placeImage = itemView.item_place_image
    }
}