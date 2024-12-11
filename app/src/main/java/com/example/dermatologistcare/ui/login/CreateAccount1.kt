package com.example.dermatologistcare.ui.login

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dermatologistcare.R
import com.example.dermatologistcare.ui.home.HomeScreen
import com.example.dermatologistcare.ui.login.data.NetworkClient
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(navController: NavHostController) {
    val isDarkMode = isSystemInDarkTheme()
    val scrollState = rememberScrollState()
    DermatologistCareTheme(darkTheme = isDarkMode) {

        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var rememberMe by remember { mutableStateOf(false) }
        var isVisible by remember { mutableStateOf(true) } // Untuk animasi visibilitas
        var otp by remember { mutableStateOf("") }
        var otpSent by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var registrationMessage by remember { mutableStateOf("") }

        // Function to handle sending OTP
        val onSendOtpClicked = {
            isLoading = true
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = NetworkClient.registerUser(email, username, password)
                    registrationMessage = response // Show server response
                    otpSent = true // OTP sent, show OTP field
                } catch (e: Exception) {
                    registrationMessage = "Error: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }

        // Function to verify OTP and complete registration
        val onVerifyOtpClicked = {
            if (otp.isBlank()) {
                registrationMessage = "Please enter the OTP."
            } else {
                isLoading = true
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = NetworkClient.verifyOtp(email, otp)
                        registrationMessage = response // Show server response
                        if (response == "Registration Successful") {
                            // Assuming the backend returns JWT token on successful registration
                            val token = response // Replace with actual token extraction
                            // Store token for future use (e.g., SharedPreferences, ViewModel)
                            navController.navigate("my_app")
                        }
                    } catch (e: Exception) {
                        registrationMessage = "Registration failed: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            }
        }

        // Mengatur state untuk animasi halaman pertama kali
        var pageVisible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            // Menunggu halaman pertama kali dirender, kemudian melakukan animasi
            pageVisible = true
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
           // State untuk cek apakah OTP sudah dikirim

            // Animasi padding
            val animatedPadding by animateDpAsState(
                targetValue = if (isVisible) 16.dp else 0.dp,
                animationSpec = tween(durationMillis = 500)
            )

            // Animasi skala halaman pertama kali dimuat
            val pageScale by animateFloatAsState(
                targetValue = if (pageVisible) 1f else 0.9f,
                animationSpec = tween(durationMillis = 700)
            )

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(all = animatedPadding)
                    .scale(pageScale), // Terapkan animasi skala ke kolom utama
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(500)) + expandVertically(),
                    exit = fadeOut(animationSpec = tween(500)) + shrinkVertically()
                ) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Image(
                        painter = painterResource(
                            id = if (isSystemInDarkTheme()) R.drawable.frame_58_dark else R.drawable.frame_58
                        ),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(120.dp)
                            .padding(bottom = 16.dp)
                    )
                }

                // Title
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { -50 }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { -50 }) + fadeOut()
                ) {
                    Text(
                        text = "Create Your Account",
                        fontSize =36.sp,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // UserName Input
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    textStyle = TextStyle(
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    ),
                    label = { Text(text = "Username",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light
                    ) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_akun),
                            contentDescription = "UserName",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                )

                // Email input
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    textStyle = TextStyle(
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    ),
                    label = { Text(text = "Email",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = "Email Icon",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                )

                // Password input
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    textStyle = TextStyle(
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    ),
                    label = { Text(text = "Password",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock),
                            contentDescription = "Password Icon",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            ),
                            contentDescription = "Toggle Password Visibility",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                )

                // Kirim OTP Button
                if (email.isNotBlank() && password.isNotBlank() && password.isNotBlank() && !otpSent) {
                    Button(
                        onClick = { onSendOtpClicked()  }, // Handle kirim OTP
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Kirim OTP")
                    }
                }

                // OTP Input muncul jika otpSent true
                if (otpSent) {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = otp,
                        onValueChange = { otp = it },
                        label = { Text("Masukkan OTP") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lock),
                                contentDescription = "OTP Icon"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                    )
                }

                // Display Loading Indicator and Messages
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }

                if (registrationMessage.isNotEmpty()) {
                    Text(text = registrationMessage, color = Color.Red, modifier = Modifier.padding(top = 16.dp))
                }

                // Remember me checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkmarkColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Remember me",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sign up button with hover effect
                val buttonColor by animateColorAsState(
                    targetValue = if (rememberMe) Color(0xFF008080) else Color.DarkGray,
                    animationSpec = tween(durationMillis = 300)
                )
                Button(
                    onClick = { onVerifyOtpClicked() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    enabled = username.isNotBlank() &&
                            email.isNotBlank() &&
                            password.isNotBlank() ,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
                ) {
                    Text(
                        text = "Sign up",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light
                    )
                }

                // Or continue with
                Text(
                    text = "or continue with",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = coolveticaFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Social media buttons with scaling animation
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    val scale by animateFloatAsState(
                        targetValue = if (isVisible) 1f else 0.9f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    SocialMediaButton(R.drawable.image1, scale)
                    SocialMediaButton(R.drawable.image2, scale)
                    SocialMediaButton(R.drawable.image3, scale)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Already have an account
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Already have an account?", color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light
                        ,fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sign in",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp,
                        modifier = Modifier.clickable {  navController.navigate("login_account")}
                    )
                }
            }
        }
    }
}

@Composable
fun SocialMediaButton(iconResId: Int, scale: Float) {
    Box(
        modifier = Modifier
            .size(width = (100 * scale).dp, height = (60 * scale).dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = "Social Media Icon",
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview
@Composable
fun PreviewCreate() {
    val navController = rememberNavController()  // Mock NavController
    CreateAccountScreen(navController = navController)
}
