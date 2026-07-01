package com.example.androidassignment4travelplannerapp.data.mapper

import com.example.androidassignment4travelplannerapp.data.local.SavedPlaceEntity
import com.example.androidassignment4travelplannerapp.data.local.TripEntity
import com.example.androidassignment4travelplannerapp.data.remote.ForecastResponse
import com.example.androidassignment4travelplannerapp.data.remote.WeatherResponse
import com.example.androidassignment4travelplannerapp.domain.model.*

fun TripEntity.toDomain(): Trip = Trip(
    id = id,
    title = title,
    destination = destination,
    latitude = lat,
    longitude = lon,
    startDate = startDate,
    endDate = endDate,
    weatherSummary = weatherInfo,
    forecastJson = forecastJson,
    photoReference = photoReference
)

fun Trip.toEntity(): TripEntity = TripEntity(
    id = id,
    title = title,
    destination = destination,
    lat = latitude,
    lon = longitude,
    startDate = startDate,
    endDate = endDate,
    weatherInfo = weatherSummary,
    forecastJson = forecastJson,
    photoReference = photoReference
)

fun SavedPlaceEntity.toDomain(): Attraction {
    val cleanCategory = if (kinds == "tourist_attraction") "Nearby Place" 
    else kinds.replace("_", " ").lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }

    return Attraction(
        id = xid,
        name = name,
        category = cleanCategory,
        latitude = lat,
        longitude = lon,
        photoReference = null, 
        dayNumber = dayNumber
    )
}

fun WeatherResponse.toDomain(): WeatherInfo = WeatherInfo(
    cityName = name,
    currentTemp = main.temp.toInt(),
    description = weather.firstOrNull()?.description ?: "",
    latitude = coord.lat,
    longitude = coord.lon
)

fun ForecastResponse.toDomain(): ForecastInfo = ForecastInfo(
    items = list.map { item ->
        ForecastItem(
            date = item.dt,
            temp = item.main.temp.toInt(),
            description = item.weather.firstOrNull()?.description ?: ""
        )
    }
)
