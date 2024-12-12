package com.example.dermatologistcare

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.shadow

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.dermatologistcare.navigation.Screen
import com.example.dermatologistcare.setting.ThemeViewModel
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import com.example.dermatologistcare.ui.home.maps.HighlightApp
import com.example.dermatologistcare.ui.camera.LiquidFabMenu
import com.example.dermatologistcare.ui.camera.SubtractedNavigationShape
import com.example.dermatologistcare.ui.camera.CameraActivity
import com.example.dermatologistcare.ui.home.Background
import com.example.dermatologistcare.ui.home.HomeScreen
import com.example.dermatologistcare.ui.login.CreateAccountScreen
import com.example.dermatologistcare.ui.login.LoginAccount
import com.example.dermatologistcare.ui.login.LoginScreenWithNoAnimations
import com.example.dermatologistcare.ui.home.maps.GoogleMapView
import com.example.dermatologistcare.ui.login.data.local.pref.getUserData
import com.example.dermatologistcare.ui.onboarding.OnboardingScreen
import com.example.dermatologistcare.ui.onboarding.OnboardingUtils
import com.example.dermatologistcare.ui.profile.ProfileScreen
import com.example.dermatologistcare.ui.profile.getSharedPreferences
import com.example.dermatologistcare.ui.resource.ResourceScreen
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        val onboardingUtils by lazy { OnboardingUtils(this) }

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000L)
            splashScreen.setKeepOnScreenCondition { false }
        }

        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val themeState = themeViewModel.themeState.collectAsState()
            val navController = rememberNavController()

            // Check if user is logged in and onboarding is complete
            val context = LocalContext.current
            val isLoggedIn = isLoggedIn(context) // Check login status
            val isOnboardingCompleted = onboardingUtils.isOnboardingCompleted() // Check if onboarding is done
            Log.d("MainActivity", "Is Logged In: $isLoggedIn")
            Log.d("MainActivity", "Is Onboarding Completed: $isOnboardingCompleted")
            if (!themeState.value.isLoading) {
                DermatologistCareTheme(darkTheme = themeState.value.isDarkMode) {
                    // Use login state to decide navigation
                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn) "my_app"  else "onboarding"
                    ) {
                        // Onboarding Screen
                        composable("onboarding") {
                            OnboardingScreen {
                                onboardingUtils.setOnboardingCompleted() // Mark onboarding as completed
                                navController.navigate("create_account") { popUpTo("onboarding") { inclusive = true } }
                            }
                        }

                        // Create Account Screen (After onboarding)
                        composable("create_account") {
                            CreateAccountScreen(navController) // Navigate to CreateAccountScreen
                        }
                        composable("login_screen") {
                            LoginScreenWithNoAnimations(navController) // Display LoginScreen when the route is "login_screen"
                        }
                        composable("login_account") {
                            LoginAccount(navController) // Display LoginScreen when the route is "login_screen"
                        }
                        // Home Screen (After login)
                        composable("my_app") {
                            saveLoginState(true, context)
                            MyApp()
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}

// SharedPreferences utility functions for login state

fun saveLoginState(isLoggedIn: Boolean, context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("is_logged_in", isLoggedIn)
    editor.apply()
}

fun isLoggedIn(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("is_logged_in", false)
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current // Obtain the context
    val (token, email) = getUserData(context)
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val themeViewModel: ThemeViewModel = viewModel()
    val themeState by themeViewModel.themeState.collectAsState()


    var isMenuExtended by remember { mutableStateOf(false) }


    val fabAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        )
    )

    val toggleAnimation = { isMenuExtended = !isMenuExtended }

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
        if (currentRoute != Screen.Profile.route && currentRoute != Screen.Camera.route) {
            TopAppBar(
                title = {
                    Column{
                        Text(
                            text = if (currentRoute == Screen.Track.route) {
                                "Track"
                                } else if (currentRoute == Screen.Resource.route) {
                                "Resource"
                            } else {
                                 if (email != null) "Hello, $email!" else "Welcome!"
                            },
                            fontSize =32.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = coolveticaFontFamily,
                        )

                    }
                },
                actions = {


                    // Profile Image
                    IconButton(onClick = {navController.navigate(Screen.Profile.route)}) {
                        Image(
                            painter = painterResource(id = R.drawable.pp), // Replace with your profile image
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
            isSystemInDarkTheme()

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
                        },//d
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

                var isFabMenuExpanded by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-fabSize / 3))
                ) {
                    LiquidFabMenu(
                        navController = navController,
                        isFabMenuExpanded = isFabMenuExpanded,
                        onToggleMenu = { expanded -> isFabMenuExpanded = expanded }
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
            composable(Screen.Home.route) { HomeScreen(navController = navController) }
            composable(Screen.Track.route) { TrackScreen() }
            composable(Screen.Camera.route) { navBackStackEntry ->
                val context = LocalContext.current
                // Launch CameraActivity when navigating to this route
                LaunchedEffect(navBackStackEntry) {
                    val intent = Intent(context, CameraActivity::class.java)
                    context.startActivity(intent)
                }
            }

            composable(Screen.Resource.route) { ResourceScreen(context = LocalContext.current) }
            composable(Screen.Profile.route) { ProfileScreen(navController = navController, context = LocalContext.current) }
            composable(Screen.MapsView.route) { GoogleMapView() }


        }
    }
}

