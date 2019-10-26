package com.mohamedhefny.nearplaces.ui.home

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mohamedhefny.nearplaces.dataSource.Entities.Place
import com.mohamedhefny.nearplaces.dataSource.PlacesRepository

class HomeViewModel : ViewModel() {

    /**
     * This method get places from PlacesRepository.
     * @param location is the current user location.
     */
    fun getPlaces(location: Location): LiveData<List<Place>> = PlacesRepository.getPlaces(location)

    /**
     * Returns 0 or 1 indicating to update mode Realtime or SingleUpdate respectively.
     */
    fun getUpdatingMode(context: Context) =
        PlacesRepository.getUpdateMode(context)

    /**
     * Update user placesUpdate mode.
     * @param context can be view or application context.
     * @param updateMode 0 or 1 indicating to update mode Realtime or SingleUpdate respectively.
     */
    fun changeUpdatingMode(context: Context, updateMode: Int) =
        PlacesRepository.changeUpdateMode(context, updateMode)

    /**
     * Update user current location.
     * Call this method only in a Realtime updating mode.
     * @param location is the current user location.
     */
    fun newLocation(location: Location) = PlacesRepository.onNewLocation(location)
}