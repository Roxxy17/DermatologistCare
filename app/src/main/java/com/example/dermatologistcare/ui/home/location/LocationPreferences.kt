package com.example.dermatologistcare.ui.home.location

import android.content.Context

class LocationPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)

    // Save location data
    fun saveLocation(name: String, latitude: Double, longitude: Double) {
        val editor = sharedPreferences.edit()
        editor.putString("location_name", name)
        editor.putFloat("latitude", latitude.toFloat())
        editor.putFloat("longitude", longitude.toFloat())
        editor.apply()
    }

    // Get location data
    fun getLocation(): Pair<String, Pair<Double, Double>> {
        val name = sharedPreferences.getString("location_name", "Lokasi Tidak Dikenal") ?: "Lokasi Tidak Dikenal"
        val latitude = sharedPreferences.getFloat("latitude", 0.0f).toDouble()
        val longitude = sharedPreferences.getFloat("longitude", 0.0f).toDouble()
        return Pair(name, Pair(latitude, longitude))
    }
}
