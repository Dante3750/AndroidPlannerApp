package com.example.androidassignment4travelplannerapp.domain.usecase

import com.example.androidassignment4travelplannerapp.domain.model.Trip
import com.example.androidassignment4travelplannerapp.domain.repository.ITravelRepository
import javax.inject.Inject

class SyncTripWeatherUseCase @Inject constructor(private val repository: ITravelRepository) {
    suspend operator fun invoke(trips: List<Trip>) {
        trips.forEach { trip ->
            try {
                val weather = repository.fetchWeather(trip.destination)
                val forecastJson = repository.fetchForecastJson(trip.destination)
                repository.updateTripWeather(
                    tripId = trip.id,
                    weatherSummary = "${weather.currentTemp}°C, ${weather.description}",
                    forecastJson = forecastJson
                )
            } catch (e: Exception) {
                // Background sync fail silently
            }
        }
    }
}
