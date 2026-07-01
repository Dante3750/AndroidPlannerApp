package com.example.androidassignment4travelplannerapp.domain.usecase

import com.example.androidassignment4travelplannerapp.domain.model.Trip
import com.example.androidassignment4travelplannerapp.domain.repository.ITravelRepository
import javax.inject.Inject

class SaveTripUseCase @Inject constructor(private val repository: ITravelRepository) {
    suspend operator fun invoke(trip: Trip) = repository.saveNewTrip(trip)
}
