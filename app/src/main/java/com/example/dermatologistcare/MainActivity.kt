package com.example.dermatologistcare

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dermatologistcare.navigation.Screen
import com.example.dermatologistcare.setting.SettingsViewModel
import com.example.dermatologistcare.setting.ThemeViewModel
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme


@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Ganti dengan logo aplikasi Anda
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val themeState = themeViewModel.themeState.collectAsState()

            if (!themeState.value.isLoading) {
                DermatologistCareTheme(darkTheme = themeState.value.isDarkMode) {
                    // Your app content here
                    MyApp()
                }
            } else {
                SplashScreen()
            }
        }
    }
}
@Composable
fun SubtractedNavigationShape(
    fabSize: Dp,
    cornerRadius: Dp
): Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val fabSizePx = with(density) { fabSize.toPx() }
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val fabRadiusPx = fabSizePx / 2f
        val additionalSpacing = 16f // Tambahan jarak di sekitar FAB
        val concaveDepthPx = fabRadiusPx / 1.5f

        return Outline.Generic(
            Path().apply {
                // Start dari top-left
                moveTo(0f, cornerRadiusPx)

                // Top-left corner
                quadraticBezierTo(0f, 0f, cornerRadiusPx, 0f)

                // Line ke start of cekungan
                lineTo(size.width / 2 - fabRadiusPx - additionalSpacing - cornerRadiusPx, 0f)

                // Cekungan kiri
                quadraticBezierTo(
                    size.width / 2 - fabRadiusPx - additionalSpacing, 0f,
                    size.width / 2 - fabRadiusPx - additionalSpacing, concaveDepthPx
                )

                // Cekungan bawah
                cubicTo(
                    size.width / 2 - fabRadiusPx - additionalSpacing, concaveDepthPx + fabRadiusPx,
                    size.width / 2 + fabRadiusPx + additionalSpacing, concaveDepthPx + fabRadiusPx,
                    size.width / 2 + fabRadiusPx + additionalSpacing, concaveDepthPx
                )

                // Cekungan kanan
                quadraticBezierTo(
                    size.width / 2 + fabRadiusPx + additionalSpacing, 0f,
                    size.width / 2 + fabRadiusPx + additionalSpacing + cornerRadiusPx, 0f
                )

                // Line ke top-right corner
                lineTo(size.width - cornerRadiusPx, 0f)

                // Top-right corner
                quadraticBezierTo(size.width, 0f, size.width, cornerRadiusPx)

                // Right side
                lineTo(size.width, size.height)

                // Bottom side
                lineTo(0f, size.height)

                // Left side
                lineTo(0f, cornerRadiusPx)

                close()
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val themeViewModel: ThemeViewModel = viewModel()
    val themeState by themeViewModel.themeState.collectAsState()

    val fabSize = 72.dp

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            navController.navigate(Screen.Camera.route)
        }
    }
Background()
    Scaffold(topBar = {
        if (currentRoute != Screen.Profile.route) {
        TopAppBar(

            title = {
                Column{

                    Text(
                        text = "Hello, Atmint!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Welcome back to your dashboard.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }
            },
            actions = {
                // Notification Icon
                IconButton(onClick = {  }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_notification), // Replace with your notification icon
                        contentDescription = "Notifications",
                        modifier = Modifier.size(15.dp)
                    )
                }
                // Profile Image
                IconButton(onClick = {navController.navigate(Screen.Profile.route)}) {
                    Image(
                        painter = painterResource(id = R.drawable.teresa), // Replace with your profile image
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                }
            },
            modifier = Modifier.shadow(elevation = 0.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent // Set transparency
            )
        )
    }
                      },
        bottomBar = {
            val scrollState = rememberScrollState()
            val isDarkMode = isSystemInDarkTheme()

            // Choose color based on dark/light theme
            val cardColor = if (themeState.isDarkMode) {
                Color(0xFFE3FEF7) // Dark mode color
            } else {
                Color(0xFF424242) // Light mode color
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                // Navigation Bar
                NavigationBar(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(SubtractedNavigationShape(fabSize = fabSize, cornerRadius = 40.dp))
                        .shadow(
                            elevation = 8.dp,
                            shape = SubtractedNavigationShape(fabSize = fabSize, cornerRadius = 40.dp)
                        ),

                ) {
                    // Home Item
                    NavigationBarItem(
                        selected = currentRoute == Screen.Home.route,
                        onClick = { navController.navigate(Screen.Home.route) },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_home),
                                contentDescription = "Home",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            if (currentRoute == Screen.Home.route) {
                                Text(
                                    "Home",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = cardColor,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedTextColor = cardColor,

                        )
                    )


                    // Track Item
                    NavigationBarItem(
                        selected = currentRoute == Screen.Track.route,
                        onClick = { navController.navigate(Screen.Track.route) },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_track),
                                contentDescription = "Track",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            if (currentRoute == Screen.Track.route){
                            Text(
                                "Track",
                                fontSize = 12.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )}
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = cardColor,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedTextColor = cardColor,

                        )
                    )

                    // Camera Item (Spacer)
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = {
                            Spacer(modifier = Modifier.size(fabSize))
                        },
                        label = { Text("") },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.White
                        )
                    )

                    // Resource Item
                    NavigationBarItem(
                        selected = currentRoute == Screen.Resource.route,
                        onClick = { navController.navigate(Screen.Resource.route) },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_resource),
                                contentDescription = "Resource",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { if (currentRoute == Screen.Resource.route){
                            Text(
                                "Resource",
                                fontSize = 12.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )}
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = cardColor,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedTextColor = cardColor,

                        )
                    )

                    // Profile Item
                    NavigationBarItem(
                        selected = currentRoute == Screen.Profile.route,
                        onClick = { navController.navigate(Screen.Profile.route) },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_profile),
                                contentDescription = "Profile",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            if (currentRoute == Screen.Profile.route){
                            Text(
                                "Profile",
                                fontSize = 12.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )}
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = cardColor,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedTextColor = cardColor,

                        )
                    )
                }

                // Camera FAB
                FloatingActionButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(fabSize)
                        .offset(y = (-fabSize/3))
                        .shadow(elevation = 8.dp, shape = CircleShape),
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_scan),
                        contentDescription = "Camera",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Track.route) { TrackScreen() }
            composable(Screen.Camera.route) { CameraScreen() }
            composable(Screen.Resource.route) { ResourceScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun GreetingPreview() {
    DermatologistCareTheme(darkTheme = false) {
        MyApp()
    }
}

