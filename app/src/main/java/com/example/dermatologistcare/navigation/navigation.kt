package com.example.dermatologistcare.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Track : Screen("track")
    object Camera : Screen("camera")
    object Resource : Screen("resource")
    object Profile : Screen("profile")
}
