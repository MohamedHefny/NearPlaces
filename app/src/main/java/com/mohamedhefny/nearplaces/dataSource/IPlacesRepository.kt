package com.mohamedhefny.nearplaces.dataSource

import androidx.lifecycle.LiveData
import com.mohamedhefny.nearplaces.dataSource.Entities.Place

interface IPlacesRepository {
    fun getPlaces(): LiveData<List<Place>>
}