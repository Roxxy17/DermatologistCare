package com.example.dermatologistcare.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await
import java.util.Locale

object LocationHelper {
    // Fungsi untuk memeriksa apakah izin lokasi diberikan
    fun hasLocationPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    // Fungsi untuk mendapatkan lokasi dengan penanganan izin
    suspend fun getUserLocation(context: Context): Location? {
        // Periksa izin terlebih dahulu
        if (!hasLocationPermission(context)) {
            return null
        }

        return try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                null
            ).await()
        } catch (e: SecurityException) {
            // Log error atau tangani secara spesifik
            null
        } catch (e: Exception) {
            // Tangani exception lainnya
            null
        }
    }

    // Fungsi untuk mendapatkan nama kota
    fun getCityName(context: Context, location: Location?): String {
        location?.let {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                if (addresses != null && addresses.isNotEmpty()) {
                    return addresses[0].locality
                        ?: addresses[0].adminArea
                        ?: "Unknown Location"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return "Unknown Location"
    }
}