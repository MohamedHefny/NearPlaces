package com.mohamedhefny.nearplaces.dataSource

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mohamedhefny.nearplaces.dataSource.Entities.Place
import com.mohamedhefny.nearplaces.dataSource.Preferences.UserPreferences
import com.mohamedhefny.nearplaces.dataSource.Remote.PlacesApiService
import com.mohamedhefny.nearplaces.dataSource.Remote.ResponsesModels.VenuesRecommendationRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PlacesRepository : IPlacesRepository {

    private val apiService = PlacesApiService()
    private val placesList = MutableLiveData<List<Place>>()

    private lateinit var currentLocation: Location

    private const val locationMaxDistance = 500

    override fun getPlaces(location: Location): LiveData<List<Place>> {
        currentLocation = location
        apiService.getPlaces(location = "${location.latitude},${location.longitude}")
            .enqueue(object : Callback<VenuesRecommendationRes> {
                override fun onResponse(
                    call: Call<VenuesRecommendationRes>,
                    response: Response<VenuesRecommendationRes>
                ) {
                    placesList.postValue(response.body()?.responseData?.groupsRes?.get(0)?.places)
                }

                override fun onFailure(call: Call<VenuesRecommendationRes>, t: Throwable) {
                    placesList.postValue(null)
                }

            })
        return placesList
    }

    override fun onNewLocation(location: Location) {
        if (location.distanceTo(currentLocation) > locationMaxDistance)
            getPlaces(location)
    }

    override fun changeUpdateMode(context: Context, updateMode: Int) =
        UserPreferences.saveUpdateMode(context, updateMode)

    override fun getUpdateMode(context: Context) =
        UserPreferences.getUpdateMode(context)
}