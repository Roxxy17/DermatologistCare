package com.example.dermatologistcare.ui.home.weather

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dermatologistcare.ui.home.weather.retrofit.WeatherResponse
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val _locationName = mutableStateOf("Memuat lokasi...")
    val locationName: State<String> = _locationName

    private val _latitude = mutableStateOf(0.0)
    val latitude: State<Double> = _latitude

    private val _longitude = mutableStateOf(0.0)
    val longitude: State<Double> = _longitude

    private val _temperature = mutableStateOf("Loading...")
    val temperature: State<String> = _temperature

    private val _weatherMain = mutableStateOf("Clear")  // Store the 'main' weather condition
    val weatherMain: State<String> = _weatherMain

    private val _aqi = mutableStateOf(0)
    val aqi: State<Int> = _aqi

    private val _uvIndex = mutableStateOf(0f)
    val uvIndex: State<Float> get() = _uvIndex

    private val _uvCategory = mutableStateOf("Low")
    val uvCategory: State<String> = _uvCategory

    fun updateWeatherMain(main: String) {
        _weatherMain.value = main
    }


    // Function to update the location in ViewModel
    fun updateLocation(name: String, latitude: Double, longitude: Double) {
        _locationName.value = name
        _latitude.value = latitude
        _longitude.value = longitude
    }

    // Function to update the weather data in ViewModel
    fun updateWeather(temperature: String, aqi: Int) {
        _temperature.value = temperature
        _aqi.value = aqi
    }

    // Method to update the UV Index and Category
    fun updateUvIndex(newUvIndex: Float) {
        _uvIndex.value = newUvIndex
    }
}
