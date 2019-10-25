package com.mohamedhefny.nearplaces.dataSource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mohamedhefny.nearplaces.dataSource.Entities.Place
import com.mohamedhefny.nearplaces.dataSource.Remote.PlacesApiService
import com.mohamedhefny.nearplaces.dataSource.Remote.ResponsesModels.VenuesRecommendationRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PlacesRepository : IPlacesRepository {

    private val apiService = PlacesApiService()
    private val placesList = MutableLiveData<List<Place>>()

    override fun getPlaces(): LiveData<List<Place>> {

        apiService.getPlaces().enqueue(object : Callback<VenuesRecommendationRes> {
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
}