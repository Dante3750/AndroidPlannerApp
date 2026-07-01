package com.example.androidassignment4travelplannerapp.data.remote

import com.google.gson.annotations.SerializedName

// OpenWeatherMap
data class WeatherResponse(
    val main: MainWeather,
    val weather: List<WeatherDescription>,
    val name: String,
    val coord: Coord
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class MainWeather(
    val temp: Double,
    val feels_like: Double,
    val humidity: Int
)

data class WeatherDescription(
    val description: String,
    val icon: String
)

// Forecast Models
data class ForecastResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val dt: Long,
    val main: MainWeather,
    val weather: List<WeatherDescription>,
    @SerializedName("dt_txt") val dtTxt: String
)
