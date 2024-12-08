package com.example.dermatologistcare.ui.home.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCityAndProvince(): String = withContext(Dispatchers.IO) {
        try {
            val location = fusedLocationClient.lastLocation.await()
            location?.let {
                return@withContext getCityProvinceFromCoordinates(it.latitude, it.longitude)
            } ?: "Location not found"
        } catch (e: Exception) {
            e.printStackTrace()
            "Error fetching location"
        }
    }

    private fun getCityProvinceFromCoordinates(lat: Double, lon: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val city = address.locality ?: "Unknown City"
                val province = address.adminArea ?: "Unknown Province"
                return "$city, $province"
            }
        }
        return "Unknown City, Unknown Province"
    }
}
