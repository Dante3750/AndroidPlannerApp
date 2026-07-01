package com.example.androidassignment4travelplannerapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidassignment4travelplannerapp.data.remote.ForecastResponse
import com.example.androidassignment4travelplannerapp.data.remote.WeatherResponse
import com.example.androidassignment4travelplannerapp.data.remote.GooglePlaceDetailModel
import com.example.androidassignment4travelplannerapp.domain.model.Attraction
import com.example.androidassignment4travelplannerapp.domain.model.Trip
import com.example.androidassignment4travelplannerapp.domain.usecase.*
import com.google.android.libraries.places.api.model.Place
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelViewModel @Inject constructor(
    getTripsUseCase: GetTripsUseCase,
    private val searchLocationsUseCase: SearchLocationsUseCase,
    private val getNearbyAttractionsUseCase: GetNearbyAttractionsUseCase,
    private val saveTripUseCase: SaveTripUseCase,
    private val deleteTripUseCase: DeleteTripUseCase,
    private val syncTripWeatherUseCase: SyncTripWeatherUseCase,
    private val addAttractionUseCase: AddAttractionUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val fetchForecastJsonUseCase: FetchForecastJsonUseCase,
    private val getPlaceDetailsUseCase: GetPlaceDetailsUseCase,
    private val getAttractionsForTripUseCase: GetAttractionsForTripUseCase,
    private val getPhotoUrlUseCase: GetPhotoUrlUseCase,
) : ViewModel() {

    val savedTrips: StateFlow<List<Trip>> = getTripsUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    private val _searchResults = MutableStateFlow<List<Attraction>>(emptyList())
    val searchResults: StateFlow<List<Attraction>> = _searchResults

    private val _currentWeather = MutableStateFlow<WeatherResponse?>(null)
    val currentWeather: StateFlow<WeatherResponse?> = _currentWeather

    private val _forecast = MutableStateFlow<ForecastResponse?>(null)
    val forecast: StateFlow<ForecastResponse?> = _forecast

    private val _suggestions = MutableStateFlow<List<Place>>(emptyList())
    val suggestions: StateFlow<List<Place>> = _suggestions

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _mapFocus = MutableStateFlow<Pair<Double, Double>?>(null)
    val mapFocus: StateFlow<Pair<Double, Double>?> = _mapFocus

    private val _selectedTripPlaces = MutableStateFlow<List<Attraction>>(emptyList())
    val selectedTripPlaces: StateFlow<List<Attraction>> = _selectedTripPlaces

    private val _selectedPlaceDetail = MutableStateFlow<GooglePlaceDetailModel?>(null)
    val selectedPlaceDetail: StateFlow<GooglePlaceDetailModel?> = _selectedPlaceDetail

    private var searchJob: Job? = null

    fun syncAllTripWeather() {
        viewModelScope.launch {
            syncTripWeatherUseCase(savedTrips.value)
        }
    }

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        searchJob?.cancel()
        if (newQuery.length < 3) {
            _suggestions.value = emptyList()
            return
        }
        searchJob = viewModelScope.launch {
            delay(500)
            try {
                _suggestions.value = searchLocationsUseCase(newQuery)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = if (e is java.net.UnknownHostException) "No internet" else "Error: ${e.message}"
            }
        }
    }

    fun startDiscoveryForCity(name: String, lat: Double, lon: Double) {
        _searchQuery.value = name
        viewModelScope.launch {
            clearData()
            setMapFocus(lat, lon)
            try {
                val weatherInfo = getWeatherUseCase(name)
                _searchResults.value = getNearbyAttractionsUseCase(lat, lon)
                
                _currentWeather.value = WeatherResponse(
                    main = com.example.androidassignment4travelplannerapp.data.remote.MainWeather(weatherInfo.currentTemp.toDouble(), 0.0, 0),
                    weather = listOf(com.example.androidassignment4travelplannerapp.data.remote.WeatherDescription(weatherInfo.description, "")),
                    name = weatherInfo.cityName,
                    coord = com.example.androidassignment4travelplannerapp.data.remote.Coord(weatherInfo.latitude, weatherInfo.longitude)
                )
                
                val forecastJson = fetchForecastJsonUseCase(name)
                _forecast.value = Gson().fromJson(forecastJson, ForecastResponse::class.java)

                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun selectSuggestion(place: Place) {
        _searchQuery.value = place.name ?: ""
        viewModelScope.launch {
            clearData()
            try {
                val latLng = place.latLng ?: return@launch
                setMapFocus(latLng.latitude, latLng.longitude)
                
                val weatherInfo = getWeatherUseCase(place.name ?: "")
                _searchResults.value = getNearbyAttractionsUseCase(latLng.latitude, latLng.longitude)
                
                _currentWeather.value = WeatherResponse(
                    main = com.example.androidassignment4travelplannerapp.data.remote.MainWeather(weatherInfo.currentTemp.toDouble(), 0.0, 0),
                    weather = listOf(com.example.androidassignment4travelplannerapp.data.remote.WeatherDescription(weatherInfo.description, "")),
                    name = weatherInfo.cityName,
                    coord = com.example.androidassignment4travelplannerapp.data.remote.Coord(weatherInfo.latitude, weatherInfo.longitude)
                )
                
                val forecastJson = fetchForecastJsonUseCase(place.name ?: "")
                _forecast.value = Gson().fromJson(forecastJson, ForecastResponse::class.java)

                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun fetchPlaceDetail(placeId: String) {
        viewModelScope.launch {
            try {
                _selectedPlaceDetail.value = getPlaceDetailsUseCase(placeId)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error loading details"
            }
        }
    }

    fun saveTrip(title: String, weather: WeatherResponse, forecast: ForecastResponse?, start: Long, end: Long) {
        viewModelScope.launch {
            val photoRef = _searchResults.value.firstOrNull { it.photoReference != null }?.photoReference
            
            val trip = Trip(
                id = 0,
                title = title,
                destination = weather.name,
                latitude = weather.coord.lat,
                longitude = weather.coord.lon,
                startDate = start,
                endDate = end,
                weatherSummary = "${weather.main.temp.toInt()}°C, ${weather.weather.firstOrNull()?.description}",
                forecastJson = forecast?.let { Gson().toJson(it) },
                photoReference = photoRef
            )
            saveTripUseCase(trip)
        }
    }

    fun addPlaceToTrip(tripId: Int, attraction: Attraction, day: Int) {
        viewModelScope.launch {
            addAttractionUseCase(tripId, attraction, day)
            _selectedTripPlaces.value = getAttractionsForTripUseCase(tripId)
        }
    }

    fun deleteTrip(tripId: Int) {
        viewModelScope.launch { deleteTripUseCase(tripId) }
    }

    fun loadTripDetails(trip: Trip) {
        viewModelScope.launch {
            setMapFocus(trip.latitude, trip.longitude)
            _selectedTripPlaces.value = getAttractionsForTripUseCase(trip.id)
        }
    }

    fun setMapFocus(lat: Double, lon: Double) { _mapFocus.value = Pair(lat, lon) }
    fun getPhotoUrl(ref: String?) = getPhotoUrlUseCase(ref)
    fun clearPlaceDetail() { _selectedPlaceDetail.value = null }
    private fun clearData() {
        _errorMessage.value = null
        _suggestions.value = emptyList()
        _searchResults.value = emptyList()
        _currentWeather.value = null
        _forecast.value = null
    }
}
