package com.mohamedhefny.nearplaces.dataSource

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.mohamedhefny.nearplaces.dataSource.Entities.Place

interface IPlacesRepository {

    fun getPlaces(location: Location): LiveData<List<Place>>
    fun loadPlacesPhotos()
    fun populatePlacesWithPhotos()
    fun onNewLocation(location: Location)
    fun changeUpdateMode(context: Context, updateMode: Int)
    fun getUpdateMode(context: Context): Int
}