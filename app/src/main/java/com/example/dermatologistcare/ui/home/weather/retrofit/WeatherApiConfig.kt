package com.example.dermatologistcare.ui.home.weather.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiConfig {
    private const val BASE_URL = "https://api.openweathermap.org/"

    // The API Key
    const val API_KEY = "a34d8171b6400aacac7303f6fc160aef"

    // Provide Retrofit instance
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Provide WeatherApiService instance
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }
}