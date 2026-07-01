package com.example.androidassignment4travelplannerapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface TravelApiService {
    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int = 5000,
        @Query("type") type: String = "tourist_attraction",
        @Query("key") apiKey: String
    ): GooglePlacesResponse

    @GET("maps/api/place/details/json")
    suspend fun getPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("fields") fields: String = "name,rating,formatted_address,photos,editorial_summary,geometry",
        @Query("key") apiKey: String
    ): GooglePlaceDetailsResponse
}

data class GooglePlacesResponse(
    val results: List<GooglePlaceModel>,
    val status: String
)

data class GooglePlaceModel(
    @SerializedName("place_id") val placeId: String,
    val name: String,
    val types: List<String>?,
    val geometry: GoogleGeometry,
    val photos: List<GooglePhoto>?
)

data class GoogleGeometry(
    val location: GoogleLatLng
)

data class GoogleLatLng(
    val lat: Double,
    val lng: Double
)

data class GooglePhoto(
    @SerializedName("photo_reference") val photoReference: String
)

data class GooglePlaceDetailsResponse(
    val result: GooglePlaceDetailModel,
    val status: String
)

data class GooglePlaceDetailModel(
    val name: String,
    val rating: Double?,
    @SerializedName("formatted_address") val address: String?,
    @SerializedName("editorial_summary") val summary: GoogleSummary?,
    val photos: List<GooglePhoto>?,
    val geometry: GoogleGeometry
)

data class GoogleSummary(
    val overview: String?
)
