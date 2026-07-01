package com.example.androidassignment4travelplannerapp.domain.model

data class Trip(
    val id: Int,
    val title: String,
    val destination: String,
    val latitude: Double,
    val longitude: Double,
    val startDate: Long,
    val endDate: Long,
    val weatherSummary: String?,
    val forecastJson: String?,
    val photoReference: String?
)

data class Attraction(
    val id: String,
    val name: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val photoReference: String?,
    val dayNumber: Int = 1
)

data class WeatherInfo(
    val cityName: String,
    val currentTemp: Int,
    val description: String,
    val latitude: Double,
    val longitude: Double
)

data class ForecastItem(
    val date: Long,
    val temp: Int,
    val description: String
)

data class ForecastInfo(
    val items: List<ForecastItem>
)
