# Travel Itinerary Planner

A travel planning app for Android. Users can search destinations, browse nearby places, check weather, and build day wise itineraries that also work offline.

## Architecture

The project uses Clean Architecture with three layers.

Domain Layer
Business logic and data models in plain Kotlin, with no Android or API code. This makes the core logic easy to test on its own.

Data Layer
Handles Room for local storage and Retrofit for network calls. Mapper classes convert API responses from Google Places and OpenWeatherMap into the app's own data models.

Presentation Layer (MVVM)
Built with Jetpack Compose. ViewModels hold UI state and call Use Cases to run business logic, instead of doing that work themselves.

## Tech Stack

Kotlin, Jetpack Compose, Material 3
Hilt for dependency injection
Retrofit and OkHttp for networking
Room for local database and offline cache
Coroutines and Flow for async work and parallel API calls
WorkManager for background sync
Google Maps and Places SDK
OpenWeatherMap API

## Key Features

Destination search and exploration using Google Places
Itinerary builder that organizes places by day, supports multi day trips
Weather info per destination, refreshed with a background sync worker
Offline access, saved trips stay viewable without internet
Combined data model that merges Google Places and weather data into one object for the UI

## Testing

Mapper tests to check API data converts correctly
Use Case tests to check business logic like saving trips and adding places
ViewModel tests to check UI state updates, using MockK and coroutines test

## Setup

1. Clone the repository
2. Open or create local.properties in the project root
3. Add your API keys

GOOGLE_MAPS_KEY=your_key
WEATHER_API_KEY=your_key

local.properties is excluded from git.

4. Sync Gradle and run the app

## Implementation Notes

Trip context filtering: the Add to Trip dialog only shows trips that match the city currently being explored, so places don't get added to the wrong trip.
Search uses Google's Autocomplete Session Tokens to reduce billed API requests.
Room is the single source of truth for the UI. Network calls refresh the local cache rather than being shown directly.

## Assumptions
Free tier API keys and rate limits are sufficient for demo and evaluation use.
A single user uses the app at a time, so no login or multi user account system is included.
Device has internet access at least once, at trip creation time, to fetch initial data before offline caching applies.

## Known Limitations

Search results are not paginated, only the first page of results is shown.
Weather sync runs on a fixed interval rather than being configurable by the user.
Compose screens do not have UI level tests, only ViewModel, use case, and mapper level tests are included.
Map preview shows a static or basic view rather than a fully interactive map, depending on time constraints during the build.