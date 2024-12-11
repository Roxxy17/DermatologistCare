package com.example.dermatologistcare.ui.resource

import retrofit2.Response
import retrofit2.http.GET

interface ResourceApiService {
    @GET("articles")
    suspend fun getResource(): Response<List<ResourceResponse>>


}