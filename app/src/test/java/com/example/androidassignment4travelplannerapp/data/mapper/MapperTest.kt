package com.example.androidassignment4travelplannerapp.data.mapper

import com.example.androidassignment4travelplannerapp.data.local.SavedPlaceEntity
import com.example.androidassignment4travelplannerapp.data.local.TripEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperTest {

    @Test
    fun `TripEntity to Trip domain model should map correctly`() {
        val entity = TripEntity(
            id = 1,
            title = "Vacation",
            destination = "Paris",
            lat = 48.8566,
            lon = 2.3522,
            startDate = 1000L,
            endDate = 2000L,
            weatherInfo = "20C, Sunny",
            forecastJson = "{}"
        )

        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.title, domain.title)
        assertEquals(entity.destination, domain.destination)
        assertEquals(entity.lat, domain.latitude, 0.0)
        assertEquals(entity.lon, domain.longitude, 0.0)
        assertEquals(entity.startDate, domain.startDate)
    }

    @Test
    fun `SavedPlaceEntity to Attraction domain model should clean category names`() {
        val entity = SavedPlaceEntity(
            id = 1,
            tripId = 10,
            name = "Eiffel Tower",
            kinds = "tourist_attraction",
            lat = 48.8584,
            lon = 2.2945,
            xid = "xyz123",
            dayNumber = 2
        )

        val domain = entity.toDomain()

        assertEquals("Nearby Place", domain.category)
        assertEquals("Eiffel Tower", domain.name)
        assertEquals(2, domain.dayNumber)
    }

    @Test
    fun `SavedPlaceEntity with snake_case kinds should be formatted to Title Case`() {
        val entity = SavedPlaceEntity(
            id = 1,
            tripId = 1,
            name = "Park",
            kinds = "amusement_park",
            lat = 0.0,
            lon = 0.0,
            xid = "id",
            dayNumber = 1
        )

        val domain = entity.toDomain()

        assertEquals("Amusement park", domain.category)
    }
}
