package com.example.dermatologistcare.ui.home.maps

import androidx.compose.runtime.MutableState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

// Function to search for nearby hospitals and add them to the map
fun findNearbyHospitals(latitude: Double, longitude: Double, apiKey: String, hospitalMarkers: MutableList<MarkerState>, markerTitles: MutableState<Map<LatLng, String>>) {
    val radius = 5000 // meters
    val type = "hospital"
    val location = "$latitude,$longitude"
    val urlString =
        "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$location&radius=$radius&type=$type&key=$apiKey"

    thread {
        try {
            val url = URL(urlString)
            val urlConnection = url.openConnection() as HttpURLConnection
            val inputStream = urlConnection.inputStream
            val response = inputStream.bufferedReader().use { it.readText() }

            val result = Gson().fromJson(response, PlacesSearchResponse::class.java)

            // Clear the existing hospital markers and add new ones
            hospitalMarkers.clear()
            val titlesMap = mutableMapOf<LatLng, String>()
            result.results?.forEach { place ->
                place.geometry?.location?.let {
                    val latLng = LatLng(it.lat, it.lng)
                    hospitalMarkers.add(MarkerState(position = latLng))
                    titlesMap[latLng] = place.name ?: "Unnamed Hospital"
                }
            }
            markerTitles.value = titlesMap
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}