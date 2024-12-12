package com.example.dermatologistcare.ui.profile

data class LogoutRequest(
    val email: String,
    val token: String
)

data class LogoutResponse(
    val success: Boolean,
    val message: String
)