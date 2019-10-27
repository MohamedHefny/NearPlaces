package com.mohamedhefny.nearplaces.dataSource.Remote

import com.mohamedhefny.nearplaces.dataSource.Remote.ResponsesModels.PhotosResponse
import com.mohamedhefny.nearplaces.dataSource.Remote.ResponsesModels.VenuePhotosRes
import com.mohamedhefny.nearplaces.dataSource.Remote.ResponsesModels.VenuesRecommendationRes
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlacesApiService {

    /**Call this method to perform a request that get places near you.
     *
     * @param location is you current location in lat,lng form
     * @param version is the date in YYYYMMDD form
     * @param radius is the area you want to retrieve result within in meters
     * @param resultLimit maximum numbers of places in the request
     */
    @GET("venues/explore")
    fun getPlaces(
        @Query("ll") location: String = "30.006738,30.969423",
        @Query("v") version: String = "20191026",
        @Query("radius") radius: Int = 1000,
        @Query("limit") resultLimit: Int = 30
    ): Call<VenuesRecommendationRes>

    /**This method reasonable for making a call to get a venue photos
     * @param venueId is required for venue you want to request it's photos
     * @param version is the date in YYYYMMDD form
     * @param resultLimit maximum numbers of places in the request
     */
    @GET("venues/{venueId}/photos")
    fun getPlacePic(
        @Path("venueId") venueId: String,
        @Query("v") version: String = "20191026",
        @Query("limit") resultLimit: Int = 1
    ): Call<VenuePhotosRes>
}