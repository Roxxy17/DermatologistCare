package com.example.dermatologistcare.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.Polyline
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.math.pow
import androidx.compose.foundation.layout.Column
import androidx.navigation.NavHostController


@Composable
fun GoogleMapView() {
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    val userLocation = remember { mutableStateOf<LatLng?>(null) }
    val hospitalMarkers = remember { mutableStateListOf<MarkerState>() }
    val markerTitles = remember { mutableStateOf<Map<LatLng, String>>(emptyMap()) }
    val polylineOptionsList = remember { mutableStateListOf<PolylineOptions>() }
    val distance = remember { mutableStateOf<Float?>(null) }  // Change to Float?

    var selectedHospitalLatLng by remember { mutableStateOf<LatLng?>(null) }
    var selectedHospitalName by remember { mutableStateOf<String>("") }
    var showPopup by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    userLocation.value = currentLatLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
                }
            }
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Request permission and handle user location
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    userLocation.value = currentLatLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // This LaunchedEffect will be triggered whenever the selected hospital changes.
    LaunchedEffect(selectedHospitalName) {
        if (selectedHospitalName.isNotEmpty() && distance.value != null) {
            val formattedDistance = String.format("%.2f", distance.value)  // Format distance to 2 decimal places
            Toast.makeText(context, "$selectedHospitalName\nDistance: $formattedDistance km", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState) {
            // Adding user location marker
            userLocation.value?.let {
                Marker(state = MarkerState(position = it), title = "Your Location")
            }

            // Adding hospital markers
            hospitalMarkers.forEach { markerState ->
                Marker(
                    state = markerState,
                    title = markerTitles.value[markerState.position],
                    onClick = { marker ->
                        // Set selected hospital
                        selectedHospitalLatLng = markerState.position
                        selectedHospitalName = markerTitles.value[markerState.position] ?: "Unknown Hospital"

                        // Calculate distance
                        userLocation.value?.let { userLatLng ->
                            val dist = calculateDistance(userLatLng, selectedHospitalLatLng!!)
                            distance.value = dist // Store the distance as Float
                        }

                        // Move the camera to the selected hospital
                        selectedHospitalLatLng?.let { hospitalLatLng ->
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(hospitalLatLng, 15f)
                        }

                        // Show popup above the selected marker
                        showPopup = true
                        true // Consume the click event
                    }
                )
            }

            // Draw polyline between user and selected hospital
            selectedHospitalLatLng?.let { hospitalLatLng ->
                userLocation.value?.let { userLatLng ->
                    val polylineOptions = PolylineOptions()
                        .add(userLatLng, hospitalLatLng)
                        .width(5f)
                        .color(Color.Red.toArgb())
                    polylineOptionsList.clear()
                    polylineOptionsList.add(polylineOptions)
                }
            }

            // Draw polyline only for the selected hospital
            polylineOptionsList.forEach { polylineOptions ->
                Polyline(
                    points = polylineOptions.points,
                    color = Color(polylineOptions.color),
                    width = polylineOptions.width
                )
            }
        }

        // Show popup when a hospital is selected
        if (showPopup) {
            AlertDialog(
                onDismissRequest = { showPopup = false },
                title = {
                    Text(
                        text = "Hospital Information",
                        style = MaterialTheme.typography.headlineLarge, // Use headlineMedium for the title in Material 3
                        color = MaterialTheme.colorScheme.secondary // Optional: Customizing color
                    )
                },
                text = {
                    // Safely handle nullable Float and format the distance to 2 decimal places
                    val formattedDistance = String.format("%.2f", distance.value?.toFloat() ?: 0f)
                    Column {
                        Text("Hospital Name: $selectedHospitalName", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp)) // Adding space between texts
                        Text("Distance: $formattedDistance km", style = MaterialTheme.typography.bodyMedium)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showPopup = false }) {
                        Text("Close", style = MaterialTheme.typography.labelLarge) // Use labelLarge for the button
                    }
                },
                modifier = Modifier.padding(16.dp) // Optional padding around the dialog content
            )
        }

        // Button to find nearby hospitals
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
            FloatingActionButton(onClick = {
                userLocation.value?.let { location ->
                    findNearbyHospitals(location.latitude, location.longitude, "AIzaSyBLsDGc8Sf-DkXzLuuSmloIKDcwrsHANrc", hospitalMarkers, markerTitles)
                }
            }) {
                Icon(Icons.Filled.Place, contentDescription = "Nearby Location")
            }
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

// Function to calculate the distance between two LatLng points using Haversine formula
fun calculateDistance(start: LatLng, end: LatLng): Float {
    val radius = 6371 // Radius of the Earth in kilometers
    val latDiff = Math.toRadians(end.latitude - start.latitude)
    val lonDiff = Math.toRadians(end.longitude - start.longitude)

    val a = Math.sin(latDiff / 2).pow(2) + Math.cos(Math.toRadians(start.latitude)) * Math.cos(Math.toRadians(end.latitude)) * Math.sin(lonDiff / 2).pow(2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    return (radius * c).toFloat()  // Return the distance as a Float
}