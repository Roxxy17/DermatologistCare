package com.example.dermatologistcare.ui.home.weather.retrofit

data class WeatherResponse(
    val main: Main,
    val name: String
)

data class Main(
    val temp: Double
)

data class Weather(
    val description: String
)

data class AirPollutionResponse(
    val list: List<AirPollution>
)

data class AirPollution(
    val main: AirQuality
)

data class AirQuality(
    val aqi: Int // AQI value
)