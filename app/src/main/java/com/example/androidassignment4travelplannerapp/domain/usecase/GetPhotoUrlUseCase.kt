package com.example.androidassignment4travelplannerapp.domain.usecase

import com.example.androidassignment4travelplannerapp.domain.repository.ITravelRepository
import javax.inject.Inject

class GetPhotoUrlUseCase @Inject constructor(private val repository: ITravelRepository) {
    operator fun invoke(photoReference: String?) = repository.getPhotoUrl(photoReference)
}
