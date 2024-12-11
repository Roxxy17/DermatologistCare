package com.example.dermatologistcare.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dermatologistcare.R
import com.example.dermatologistcare.navigation.Screen
import com.example.dermatologistcare.setting.ThemeViewModel
import com.example.dermatologistcare.ui.home.weather.LocationViewModel
import com.example.dermatologistcare.ui.home.weather.retrofit.WeatherApiConfig
import com.example.dermatologistcare.ui.maps.HighlightApp
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale


@Composable
fun Background(
    isProfileScreen: Boolean = false,
    themeViewModel: ThemeViewModel = viewModel()
) {
    val themeState = themeViewModel.themeState.collectAsState()

    if (!themeState.value.isLoading) {
        Image(
            painter = if (themeState.value.isDarkMode) {
                painterResource(id = R.drawable.background_dark)
            } else {
                painterResource(id = R.drawable.background)
            },
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isProfileScreen) Modifier.rotate(180f)
                    else Modifier
                )
        )
    }
}



@Composable
fun HomeScreen(
    themeViewModel: ThemeViewModel = viewModel(),navController: NavController
    , locationViewModel: LocationViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val themeState = themeViewModel.themeState.collectAsState()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Location states
    val locationName = locationViewModel.locationName.value
    val latitude = locationViewModel.latitude.value
    val longitude = locationViewModel.longitude.value
    val temperature = locationViewModel.temperature.value
    val aqi = locationViewModel.aqi.value

    val uvIndex = locationViewModel.uvIndex.value // Misalnya ini adalah Double val uvIndexFloat = uvIndex.toFloat()
    val currentHour = LocalDateTime.now().hour

    // Determine whether it's day or night
    val isDayTime = currentHour in 6..18 // Daytime is between 6 AM and 6 PM

    // Choose the appropriate icon based on the time of day
    val weatherCondition = locationViewModel.weatherMain.value

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    val isLocationEnabled = isLocationEnabled(context)


// Menentukan ikon berdasarkan kondisi cuaca
    val iconResource = when (weatherCondition) {
        "Clear" -> if (isDayTime) R.drawable.ic_sun else R.drawable.ic_bulan
        "Clouds" -> R.drawable.ic_cloud // Ikon awan jika mendung
        "Rain" -> R.drawable.ic_rain // Ikon hujan jika ada hujan
        "Snow" -> R.drawable.ic_snow // Ikon salju jika ada salju
        else -> R.drawable.ic_sun // Ikon bulan jika tidak diketahui
    }
    // Location fetching function
    fun getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permission
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { locationResult ->
                locationResult?.let {
                    val newLongitude = it.longitude
                    val newLatitude = it.latitude
                    Log.d("Location", "Longitude: $newLongitude, Latitude: $newLatitude")

                    // Use Geocoder to get city and province
                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(newLatitude, newLongitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val city = when {
                                !address.subAdminArea.isNullOrBlank() -> address.subAdminArea
                                !address.locality.isNullOrBlank() -> address.locality
                                !address.adminArea.isNullOrBlank() -> address.adminArea
                                else -> "Lokasi Tidak Dikenal"
                            }
                            val province = addresses[0].adminArea ?: "Provinsi Tidak Dikenal"
                            val location = "$city, $province"

                            // Update location in ViewModel
                            locationViewModel.updateLocation(location, newLatitude, newLongitude)

                        }
                    } catch (e: Exception) {
                        locationViewModel.updateLocation("Gagal mendapatkan lokasi", 0.0, 0.0)
                    }
                }
            }
            .addOnFailureListener {
                locationViewModel.updateLocation("Gagal mendapatkan lokasi", 0.0, 0.0)
            }
    }

    // Fetch weather and air quality data when location changes
    LaunchedEffect(latitude, longitude) {
        try {
            val retrofit = WeatherApiConfig.provideRetrofit()
            // Get Retrofit instance from ApiConfig
            val service = WeatherApiConfig.provideWeatherApiService(retrofit)

            // Fetch weather data
            val weatherResponse = service.getWeather(latitude, longitude, WeatherApiConfig.API_KEY)
            val temp = weatherResponse.main.temp?.let { "${it}°C" } ?: "Error"
            val weatherMain = weatherResponse.weather.firstOrNull()?.main ?: "Unknown"
            Log.d("HomeScreen", "Weather Response: $weatherResponse")

            locationViewModel.updateWeather(temp, 0)
            locationViewModel.updateWeatherMain(weatherMain)
            // Fetch air pollution data
            val airPollutionResponse =
                service.getAirPollution(latitude, longitude, WeatherApiConfig.API_KEY)
            val airQualityIndex = airPollutionResponse.list.firstOrNull()?.main?.aqi ?: 0
            locationViewModel.updateWeather(temp, airQualityIndex)

            val uvIndexResponse = service.getUvIndex(latitude, longitude, WeatherApiConfig.API_KEY)
            locationViewModel.updateUvIndex(uvIndexResponse.value?.toFloat() ?: 0f)

            Log.d("HomeScreen", "UV Index Response: $uvIndexResponse")

            val uvIndexCategory = when {
                uvIndex in 0.0..2.0 -> "Low"
                uvIndex in 3.0..5.0 -> "Moderate"
                uvIndex in 6.0..7.0 -> "High"
                uvIndex in 8.0..10.0 -> "Very High"
                uvIndex > 10.0 -> "Extreme"
                else -> "Unknown"
            }
        } catch (e: Exception) {

        }
    }
    val aqiCategory = when {
        aqi == 0 -> "Loading..."
        aqi <= 50 -> "Good"
        aqi in 51..100 -> "Moderate"
        aqi in 101..150 -> "Unhealthy for Sensitive Groups"
        aqi in 151..200 -> "Unhealthy"
        aqi in 201..300 -> "Very Unhealthy"
        else -> "Hazardous"
    }
    val uvIndexCategory = when {
        uvIndex in 0.0..2.0 -> "Low"
        uvIndex in 3.0..5.0 -> "Moderate"
        uvIndex in 6.0..7.0 -> "High"
        uvIndex in 8.0..10.0 -> "Very High"
        uvIndex > 10.0 -> "Extreme"
        else -> "Unknown"
    }

    LaunchedEffect(true) {
        getDeviceLocation()
    }




    val isDarkMode = themeState.value.isDarkMode

    // Choose color based on dark/light theme
    val cardColor = if (isDarkMode) {
        Color(0xFFE3FEF7).copy(alpha = 0.05f) // Dark mode color
    } else {
        Color(0xFF424242).copy(alpha = 0.05f) // Light mode color
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
        if (!isLocationEnabled) {
            // Show card with message to enable location
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center) // Ensure text and button are centered
                    ) {
                        Text(
                            text = "Aktifkan Lokasi untuk melihat cuaca dan lokasi terkini",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = coolveticaFontFamily,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            // Ask the user to turn on location in settings
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            context.startActivity(intent)
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(50.dp) // Adjust height as needed
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            ) {
                            Text("Nyalakan Lokasi",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                                fontFamily = coolveticaFontFamily,
                                color = MaterialTheme.colorScheme.tertiary )
                        }
                    }

                    // Add the refresh button to the right corner
                    IconButton(
                        onClick = {
                            // Call your function to refresh location or retry the action
                            getDeviceLocation()
                        },
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.TopEnd) // Align at top-end (right corner)
                            .padding(8.dp) // Adjust padding to your liking
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.refresh), // Use refresh icon
                            contentDescription = "Refresh Location",
                            tint = Color.Gray
                        )
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentHeight()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(
                            bottomEnd = 10.dp,
                            topEnd = 10.dp,
                            bottomStart = 10.dp
                        )
                    ),
                shape = RoundedCornerShape(
                    bottomEnd = 10.dp,
                    topEnd = 10.dp,
                    bottomStart = 10.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x424242)
                )
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Icon
                            Icon(
                                painter = painterResource(id = iconResource),
                                contentDescription = "Home Icon",
                                modifier = Modifier.size(100.dp), // Adjust size as needed
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.height(1.dp)) // Add space between the icon and text

                            // Weather Condition Text
                            Text(
                                text = weatherCondition, // The weather condition, e.g., "Clear", "Rain"
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Light,
                                fontFamily = coolveticaFontFamily,
                                color = MaterialTheme.colorScheme.tertiary // Change color as needed
                            )
                        }


                        // You can optionally add more information, like temperature, etc.

                        Column(
                            modifier = Modifier.weight(2f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_map_pin),
                                    contentDescription = "Location Icon",
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = locationName,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = coolveticaFontFamily,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        // Mendapatkan lokasi dan memperbarui data cuaca saat tombol ditekan
                                        getDeviceLocation()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.refresh), // Gunakan ikon refresh
                                        contentDescription = "Refresh Location",
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                }


                            }
                            val currentDate = remember {
                                val dateFormat =
                                    SimpleDateFormat("EEEE - dd MMMM", Locale.getDefault())
                                dateFormat.format(Date())
                            }

                            Text(
                                text = currentDate,
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth()
                                ,fontWeight = FontWeight.Light,
                                fontFamily = coolveticaFontFamily,
                            )
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = cardColor
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                                ) {
                                    Text(
                                        text = "AQI",
                                        fontSize = 14.sp,
                                        modifier = Modifier.fillMaxWidth(),
                                        fontWeight = FontWeight.Light,
                                        fontFamily = coolveticaFontFamily,
                                    )
                                    Text(
                                        text = "$aqiCategory (${aqi})",
                                        fontSize = 15.sp,

                                        modifier = Modifier.fillMaxWidth(),
                                        fontWeight = FontWeight.Light,
                                        fontFamily = coolveticaFontFamily,
                                    )
                                    LinearProgressIndicator(
                                        progress = { aqi.toFloat() / 20f },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        color = MaterialTheme.colorScheme.tertiary,
                                        trackColor = cardColor.copy(alpha = 0.25f),
                                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.padding(top = 5.dp)
                            ) {
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 2.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = cardColor
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                                    ) {
                                        Text(
                                            text = "UV Index",
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                            ,fontWeight = FontWeight.Light,
                                            fontFamily = coolveticaFontFamily,
                                        )
                                        Text(
                                            text = "${locationViewModel.uvIndex.value} - ${uvIndexCategory}",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Light,
                                            fontFamily = coolveticaFontFamily,
                                            modifier = Modifier
                                        )
                                    }
                                }
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 2.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = cardColor
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                                    ) {
                                        Text(
                                            text = "Temperature",
                                            fontSize = 14.sp,
                                            modifier = Modifier,
                                            fontWeight = FontWeight.Light,
                                            fontFamily = coolveticaFontFamily,
                                        )
                                        Text(
                                            text = "${locationViewModel.temperature.value}",
                                            fontSize = 15.sp,

                                            modifier = Modifier,
                                            fontWeight = FontWeight.Light,
                                            fontFamily = coolveticaFontFamily,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Text(
            text = "Recent Diagnose",
            fontSize = 32.sp,
            fontWeight = FontWeight.Light,
            fontFamily = coolveticaFontFamily,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, bottom = 0.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            items(10) { index ->
                RecentItem(index)
            }
        }

        Text(
            text = "Hospital Near Me",
            fontSize = 32.sp,
            fontWeight = FontWeight.Light,
            fontFamily = coolveticaFontFamily,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp)
        )

        // Box containing HighlightApp and the bottom section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // HighlightApp section with larger map
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Ensure it fills the width
                    .height(400.dp) // Larger map area
            ) {
                HighlightApp()
            }

            // Spacer to add space between the map and the bottom section
            Spacer(modifier = Modifier.height(16.dp))

            // Bottom section after the map
            Button(
                onClick = { navController.navigate(Screen.MapsView.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp) // Adjust height as needed
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        // Navigating to the MapsView screen
                        navController.navigate(Screen.MapsView.route)
                    },
            ) {
                Text(
                    text = "See Nearby Hospitals", // Button text
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = coolveticaFontFamily,
                    color = MaterialTheme.colorScheme.tertiary // Text color to contrast the background
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            // You can add more UI elements here (e.g., more buttons, text)
        }
    }
}

// Companion object for constant
private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

@Composable
fun RecentItem(index: Int) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),

        ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .width(250.dp)
                .height(110.dp) // Set a fixed height for the card
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(
                        bottomEnd = 10.dp,
                        topEnd = 10.dp,
                        bottomStart = 10.dp
                    )
                ),
            shape = RoundedCornerShape(bottomEnd = 10.dp, topEnd = 10.dp, bottomStart = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x424242)
            )
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title of the resource
                Image(
                    painter = painterResource(id = R.drawable.recent_diagnose),
                    contentDescription = "Home Icon",
                    modifier = Modifier
                        .width(78.dp)
                        .height(89.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop

                )

                // Description of the resource (just a placeholder for now)
                Column {
                    Text(
                        text = "Chikenpox",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = coolveticaFontFamily,
                    )
                    Text(
                        text = "The wound has started to dry out and has a significant healing rate.",
                        fontSize = 10.sp,
                        maxLines = 3,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = coolveticaFontFamily,
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "19.19",
                            fontSize = 10.sp,

                            )
                        Text(
                            text = "3 DAYS AGO",
                            fontSize = 10.sp,

                            )
                    }

                }
            }
        }
    }
}

