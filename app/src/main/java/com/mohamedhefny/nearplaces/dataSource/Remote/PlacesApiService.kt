package com.mohamedhefny.nearplaces.dataSource.Remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mohamedhefny.nearplaces.dataSource.Remote.ResponsesModels.VenuesRecommendationRes
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//BaseURL = https://api.foursquare.com/v2/venues/explore

const val CLIENT_ID = "PUT YOUR OWN CLIENT ID HERE"
const val CLIENT_SECRET = "PUT YOUR OWN CLIENT SECRET HERE"

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
        @Query("v") version: String = "20191020",
        @Query("radius") radius: Int = 1000,
        @Query("limit") resultLimit: Int = 30
    ): Call<VenuesRecommendationRes>

    companion object {
        operator fun invoke(): PlacesApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request().url().newBuilder()
                    .addQueryParameter("client_id", CLIENT_ID)
                    .addQueryParameter("client_secret", CLIENT_SECRET)
                    .build()

                val request = chain.request().newBuilder().url(url).build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder().addInterceptor(requestInterceptor).build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.foursquare.com/v2/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlacesApiService::class.java)
        }
    }
}