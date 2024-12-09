package com.example.dermatologistcare.ui.home.weather.retrofit

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)

data class Main(
    val temp: Double
)

data class Weather(
    val main: String

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
// Define the response model for UV Index API
data class UVIndexResponse(
    val value: Double? // The value of UV Index as Double (can be null if the data is unavailable)
)
