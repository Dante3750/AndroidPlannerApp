package com.example.androidassignment4travelplannerapp.domain.usecase

import com.example.androidassignment4travelplannerapp.domain.model.Attraction
import com.example.androidassignment4travelplannerapp.domain.model.Trip
import com.example.androidassignment4travelplannerapp.domain.repository.ITravelRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UseCaseTest {

    private val repository: ITravelRepository = mockk(relaxed = true)

    @Test
    fun `GetTripsUseCase should return trips from repository`() = runTest {
        val mockTrips = listOf(
            Trip(1, "Trip 1", "London", 51.5, -0.1, 0L, 0L, null, null, null)
        )
        coEvery { repository.getSavedTrips() } returns flowOf(mockTrips)

        val getTripsUseCase = GetTripsUseCase(repository)
        val result = getTripsUseCase().first()

        assertEquals(mockTrips, result)
        coVerify(exactly = 1) { repository.getSavedTrips() }
    }

    @Test
    fun `SaveTripUseCase should delegate save to repository`() = runTest {
        val trip = Trip(0, "New", "Tokyo", 35.6, 139.6, 0L, 0L, null, null, null)
        val saveTripUseCase = SaveTripUseCase(repository)

        saveTripUseCase(trip)

        coVerify { repository.saveNewTrip(trip) }
    }

    @Test
    fun `AddAttractionUseCase should call repository with correct params`() = runTest {
        val attraction = Attraction("id", "Park", "Cat", 0.0, 0.0, null)
        val addAttractionUseCase = AddAttractionUseCase(repository)

        addAttractionUseCase(1, attraction, 3)

        coVerify { repository.addAttractionToTrip(1, attraction, 3) }
    }

    @Test
    fun `SearchLocationsUseCase should return locations from repository`() = runTest {
        coEvery { repository.searchLocations("London") } returns emptyList()
        val searchUseCase = SearchLocationsUseCase(repository)

        val result = searchUseCase("London")

        assertEquals(0, result.size)
        coVerify { repository.searchLocations("London") }
    }
}
