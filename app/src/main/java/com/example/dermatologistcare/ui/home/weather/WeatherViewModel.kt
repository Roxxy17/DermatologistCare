package com.example.dermatologistcare.ui.home.weather


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.lifecycle.viewModelScope
import com.example.dermatologistcare.ui.home.weather.retrofit.WeatherApiService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

      private val _locationName = MutableLiveData("Memuat lokasi...")
      val locationName: LiveData<String> = _locationName

      private val _latitude = MutableLiveData(0.0)
      val latitude: LiveData<Double> = _latitude

      private val _longitude = MutableLiveData(0.0)
      val longitude: LiveData<Double> = _longitude

      private val _temperature = MutableLiveData("")
      val temperature: LiveData<String> = _temperature

      private val _aqi = MutableLiveData(0)
      val aqi: LiveData<Int> = _aqi

      private val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(application)

      fun getDeviceLocation() {
            val context = getApplication<Application>()
            if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                  ) != PackageManager.PERMISSION_GRANTED
            ) {
                  // Permission handling
                  return
            }

            fusedLocationClient.lastLocation
                  .addOnSuccessListener { location ->
                        location?.let {
                              _latitude.value = it.latitude
                              _longitude.value = it.longitude

                              // Use Geocoder for location name
                              val geocoder = Geocoder(context, Locale.getDefault())
                              val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                              if (!addresses.isNullOrEmpty()) {
                                    val city = addresses[0].locality ?: "Lokasi Tidak Dikenal"
                                    val province = addresses[0].adminArea ?: "Provinsi Tidak Dikenal"
                                    _locationName.value = "$city, $province"
                              }
                        }
                  }
      }

      fun fetchWeatherData(apiKey: String) {
            viewModelScope.launch {
                  try {
                        val retrofit = Retrofit.Builder()
                              .baseUrl("https://api.openweathermap.org/")
                              .addConverterFactory(GsonConverterFactory.create())
                              .build()
                        val service = retrofit.create(WeatherApiService::class.java)

                        // Fetch weather
                        val weatherResponse = service.getWeather(
                              latitude.value ?: 0.0,
                              longitude.value ?: 0.0,
                              apiKey
                        )
                        _temperature.value = "${weatherResponse.main.temp}°C"

                        // Fetch AQI
                        val airPollutionResponse = service.getAirPollution(
                              latitude.value ?: 0.0,
                              longitude.value ?: 0.0,
                              apiKey
                        )
                        _aqi.value = airPollutionResponse.list.firstOrNull()?.main?.aqi ?: 0
                  } catch (e: Exception) {
                        _temperature.value = "Error"
                        _aqi.value = 0
                  }
            }
      }
}
