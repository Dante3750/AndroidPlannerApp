package com.example.androidassignment4travelplannerapp.domain.usecase

import com.example.androidassignment4travelplannerapp.domain.repository.ITravelRepository
import javax.inject.Inject

class GetTripsUseCase @Inject constructor(private val repository: ITravelRepository) {
    operator fun invoke() = repository.getSavedTrips()
}
