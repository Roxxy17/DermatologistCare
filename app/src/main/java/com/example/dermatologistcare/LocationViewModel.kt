package com.example.dermatologistcare.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dermatologistcare.utils.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val _cityName = MutableStateFlow("Enable Location")
    val cityName = _cityName.asStateFlow()

    fun updateLocation(context: Context) {
        viewModelScope.launch {
            // Periksa izin terlebih dahulu
            if (!LocationHelper.hasLocationPermission(context)) {
                Log.d("LocationViewModel", "Location Permission Not Granted")
                _cityName.value = "Location Permission Needed"
                return@launch
            }

            val location = LocationHelper.getUserLocation(context)
            if (location != null) {
                val city = LocationHelper.getCityName(context, location)
                _cityName.value = city
                Log.d("LocationViewModel", "Location: $city")
            } else {
                Log.d("LocationViewModel", "Failed to get location")
                _cityName.value = "Location not available"
            }
        }
    }


    // Metode untuk mengupdate status izin
    fun updatePermissionStatus(status: String) {
        _cityName.value = status
    }
}