package com.mohamedhefny.nearplaces.dataSource.Remote.ResponsesModels

import com.google.gson.annotations.SerializedName
import com.mohamedhefny.nearplaces.dataSource.Entities.Place
import com.mohamedhefny.nearplaces.dataSource.Entities.Venue

data class VenuesRecommendationRes(@SerializedName("response") val responseData: ResponseData)

data class ResponseData(@SerializedName("groups") val groupsRes: List<GroupRes>)

data class GroupRes(
    val type: String,
    val name: String,
    @SerializedName("items")
    val places: List<Place>
)