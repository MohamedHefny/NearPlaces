package com.mohamedhefny.nearplaces.dataSource.Remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mohamedhefny.nearplaces.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlacesApiClient {
    companion object {
        operator fun invoke(): PlacesApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request().url().newBuilder()
                    .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
                    .addQueryParameter("client_secret", BuildConfig.CLIENT_SECRET)
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