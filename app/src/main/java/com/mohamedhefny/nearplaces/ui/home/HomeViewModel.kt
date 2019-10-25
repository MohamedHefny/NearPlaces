package com.mohamedhefny.nearplaces.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mohamedhefny.nearplaces.dataSource.Entities.Place
import com.mohamedhefny.nearplaces.dataSource.PlacesRepository

class HomeViewModel : ViewModel() {

    /**This method get places from Places repository
     * @param updateType is the type of updating mode 0 or 1 indicating to realtime or single update sequentially
     */
    fun getPlaces(): LiveData<List<Place>> = PlacesRepository.getPlaces()


    fun getUpdatingMode(): Int {
        return 0
    }

    fun changeUpdatingMode(mode: Int) {

    }
}