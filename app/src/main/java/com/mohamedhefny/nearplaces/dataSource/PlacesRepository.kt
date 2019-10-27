package com.mohamedhefny.nearplaces.dataSource

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mohamedhefny.nearplaces.dataSource.Entities.Place
import com.mohamedhefny.nearplaces.dataSource.Preferences.UserPreferences
import com.mohamedhefny.nearplaces.dataSource.Remote.PlacesApiClient
import com.mohamedhefny.nearplaces.dataSource.Remote.ResponsesModels.PhotosResponse
import com.mohamedhefny.nearplaces.dataSource.Remote.ResponsesModels.VenuePhotosRes
import com.mohamedhefny.nearplaces.dataSource.Remote.ResponsesModels.VenuesRecommendationRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PlacesRepository : IPlacesRepository {

    private val apiService = PlacesApiClient.invoke()
    private val placesList = MutableLiveData<List<Place>>()
    private val placesPhotos: MutableList<VenuePhotosRes?> = mutableListOf()

    private lateinit var currentLocation: Location

    private const val locationMaxDistance = 500

    private var currentPlaceToLoadPic = 0

    override fun getPlaces(location: Location): LiveData<List<Place>> {
        currentLocation = location
        apiService.getPlaces(location = "${location.latitude},${location.longitude}")
            .enqueue(object : Callback<VenuesRecommendationRes> {
                override fun onResponse(
                    call: Call<VenuesRecommendationRes>, response: Response<VenuesRecommendationRes>
                ) {
                    placesList.postValue(response.body()?.responseData?.groupsRes?.get(0)?.places)
                }

                override fun onFailure(call: Call<VenuesRecommendationRes>, t: Throwable) {
                    placesList.postValue(null)
                }

            })
        return placesList
    }

    override fun loadPlacesPhotos() {
        apiService.getPlacePic(placesList.value?.get(currentPlaceToLoadPic)?.venue?.placeId.toString())
            .enqueue(
                object : Callback<VenuePhotosRes> {
                    override fun onResponse(
                        call: Call<VenuePhotosRes>, response: Response<VenuePhotosRes>
                    ) {
                        placesPhotos.add(response.body())

                        if (currentPlaceToLoadPic < placesList.value!!.size - 1) {
                            currentPlaceToLoadPic++
                            loadPlacesPhotos()
                        } else
                            populatePlacesWithPhotos()
                    }

                    override fun onFailure(call: Call<VenuePhotosRes>, t: Throwable) {

                    }
                }
            )
    }

    override fun populatePlacesWithPhotos() {
        val places = placesList.value
        for ((placeIndex, place) in places!!.withIndex()) {
            place.venue.image =
                placesPhotos[placeIndex]?.photosRes?.VenuePhotos?.get(0)?.prefix.toString()
                    .plus("500x300")
                    .plus(placesPhotos[placeIndex]?.photosRes?.VenuePhotos?.get(0)?.suffix.toString())
        }
        placesList.postValue(places)
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