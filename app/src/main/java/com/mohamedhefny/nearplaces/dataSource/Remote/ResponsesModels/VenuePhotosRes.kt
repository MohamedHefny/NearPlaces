package com.mohamedhefny.nearplaces.dataSource.Remote.ResponsesModels

import com.google.gson.annotations.SerializedName

data class VenuePhotosRes(@SerializedName("response") val photosRes: PhotosResponse)

data class PhotosResponse(@SerializedName("items") val VenuePhotos: List<Photo>)

data class Photo(val prefix: String, val suffix: String, val width: Int, val height: String)