package com.example.dermatologistcare.maps

data class PlacesSearchResponse(
    val results: List<Place>?
)

data class Place(
    val name: String? = null,
    val geometry: Geometry? = null
)

data class Geometry(
    val location: Location?
)

data class Location(
    val lat: Double,
    val lng: Double
)