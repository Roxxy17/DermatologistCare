package com.example.dermatologistcare.ui.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dermatologistcare.MyApp
import com.example.dermatologistcare.R
import com.example.dermatologistcare.navigation.Screen
import com.example.dermatologistcare.setting.ThemeViewModel
import com.example.dermatologistcare.ui.home.location.LocationPreferences
import com.example.dermatologistcare.ui.home.weather.retrofit.WeatherApiService
import com.example.dermatologistcare.ui.home.maps.GoogleMapView
import com.example.dermatologistcare.ui.home.maps.HighlightApp
import com.example.dermatologistcare.ui.home.maps.HighlightView
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme
import com.example.dermatologistcare.ui.theme.highlight
import com.google.android.gms.location.LocationServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
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
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val themeState = themeViewModel.themeState.collectAsState()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Location states
    val locationName = remember { mutableStateOf("Memuat lokasi...") }
    val longitude = remember { mutableStateOf(0.0) }
    val latitude = remember { mutableStateOf(0.0) }

    val aqi = remember { mutableStateOf<Int>(0) }

    var temperature = remember { mutableStateOf("") }
    val locationPreferences = LocationPreferences(context)
    val apiKey = "a34d8171b6400aacac7303f6fc160aef"


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
                    longitude.value = it.longitude
                    latitude.value = it.latitude
                    Log.d("Location", "Longitude: ${it.longitude}, Latitude: ${it.latitude}")

                    // Use Geocoder to get city and province
                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val city = when {
                                !address.subAdminArea.isNullOrBlank() -> address.subAdminArea // Kabupaten/Kota
                                !address.locality.isNullOrBlank() -> address.locality // Kota
                                !address.adminArea.isNullOrBlank() -> address.adminArea // Provinsi fallback
                                else -> "Lokasi Tidak Dikenal"
                            }
                            val province = addresses[0].adminArea ?: "Provinsi Tidak Dikenal"
                            locationName.value = "$city, $province"

                            // Simpan lokasi baru ke SharedPreferences
                            locationPreferences.saveLocation(
                                locationName.value,
                                latitude.value,
                                longitude.value
                            )
                        }
                    } catch (e: Exception) {
                        locationName.value = "Gagal mendapatkan lokasi"
                    }
                }
            }
            .addOnFailureListener {
                locationName.value = "Gagal mendapatkan lokasi"
            }
    }

    // Fetch weather and air quality data when location changes
    LaunchedEffect(latitude.value, longitude.value) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(WeatherApiService::class.java)

            // Fetch weather data
            val weatherResponse = service.getWeather(latitude.value, longitude.value, apiKey)
            if (weatherResponse.main.temp != null) {
                temperature.value = "${weatherResponse.main.temp}°C"
            } else {
                temperature.value = "Error"
            }

            // Fetch air pollution data
            val airPollutionResponse =
                service.getAirPollution(latitude.value, longitude.value, apiKey)
            if (airPollutionResponse.list.isNotEmpty()) {
                aqi.value = airPollutionResponse.list[0].main.aqi
            } else {
                aqi.value = 0  // Error case or fallback value
            }
        } catch (e: Exception) {
            temperature.value = "Error"
            aqi.value = 0
            Toast.makeText(context, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    val aqiCategory = when {
        aqi.value == 0 -> "Error"
        aqi.value <= 50 -> "Good"
        aqi.value in 51..100 -> "Moderate"
        aqi.value in 101..150 -> "Unhealthy for Sensitive Groups"
        aqi.value in 151..200 -> "Unhealthy"
        aqi.value in 201..300 -> "Very Unhealthy"
        else -> "Hazardous"
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
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()

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
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bulan),
                            contentDescription = "Home Icon",
                            modifier = Modifier
                                .size(100.dp)
                                .weight(1f),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
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
                                    text = locationName.value,
                                    fontWeight = FontWeight.Bold,
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
                                        painter = painterResource(id = R.drawable.ic_map_pin), // Gunakan ikon refresh
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
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "$aqiCategory (${aqi.value})",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    LinearProgressIndicator(
                                        progress = aqi.value.toFloat() / 20f,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        trackColor = cardColor.copy(alpha = 0.25f),
                                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
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
                                        )
                                        Text(
                                            text = "Low",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
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
                                            modifier = Modifier
                                        )
                                        Text(
                                            text = "${temperature.value}",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
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
            fontWeight = FontWeight.Bold,
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
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
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
                    style = MaterialTheme.typography.bodyLarge,
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
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "The wound has started to dry out and has a significant healing rate.",
                        fontSize = 10.sp,
                        maxLines = 3,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        lineHeight = 10.sp,
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


//}
//@Composable
//fun HospitalItem(index: Int) {
//    Box(modifier = Modifier
//        .padding(start = 16.dp)
//        .fillMaxWidth()
//      ) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//
//                .width(200.dp)
//                .height(300.dp) // Set a fixed height for the card
//                .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
//            shape = RoundedCornerShape(8.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = MaterialTheme.colorScheme.surface,
//                contentColor = MaterialTheme.colorScheme.onSurface
//            )
//
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxHeight(),
//            ) {
//                Box {  // Added Box to overlay bookmark icon on image
//                    Image(
//                        painter = painterResource(id = R.drawable.rs),
//                        contentDescription = "Home Icon",
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(200.dp)
//                            .clip(RoundedCornerShape(bottomEnd = 20.dp)),
//                        contentScale = ContentScale.Crop
//
//                    )
//
//                    // Bookmark Icon
//                    IconButton(
//                        onClick = { /* Add your bookmark action here */ },
//                        modifier = Modifier
//                            .align(Alignment.TopEnd)
//
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.bookmarked),
//                            contentDescription = "Bookmark",
//                            tint = MaterialTheme.colorScheme.secondary
//                        )
//                    }
//                }
//
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        text = "RSA UGM",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    HorizontalDivider(
//                        color = MaterialTheme.colorScheme.tertiary,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            text = "OPEN 24 HOURS",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 10.sp,
//                        )
//                        Text(
//                            text = "2.3KM",
//                            fontSize = 10.sp,
//                            color = MaterialTheme.colorScheme.tertiary
//                        )
//                    }
//                }
//
//            }
//        }
//    }
//}

