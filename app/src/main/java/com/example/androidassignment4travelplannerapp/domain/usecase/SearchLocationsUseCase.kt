package com.example.androidassignment4travelplannerapp.domain.usecase

import com.example.androidassignment4travelplannerapp.domain.repository.ITravelRepository
import javax.inject.Inject

class SearchLocationsUseCase @Inject constructor(private val repository: ITravelRepository) {
    suspend operator fun invoke(query: String) = repository.searchLocations(query)
}
