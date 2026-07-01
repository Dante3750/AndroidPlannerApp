package com.example.androidassignment4travelplannerapp.domain.usecase

import com.example.androidassignment4travelplannerapp.domain.model.Attraction
import com.example.androidassignment4travelplannerapp.domain.repository.ITravelRepository
import javax.inject.Inject

class AddAttractionUseCase @Inject constructor(private val repository: ITravelRepository) {
    suspend operator fun invoke(tripId: Int, attraction: Attraction, day: Int) = 
        repository.addAttractionToTrip(tripId, attraction, day)
}
