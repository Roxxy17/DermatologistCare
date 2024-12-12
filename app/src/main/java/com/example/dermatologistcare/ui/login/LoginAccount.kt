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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.dermatologistcare.ui.login.data.local.pref.saveUserData
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAccount(navController: NavHostController) {
    val isDarkMode = isSystemInDarkTheme()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    DermatologistCareTheme(darkTheme = isDarkMode) {
        // Mengatur state untuk animasi halaman pertama kali
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var rememberMe by remember { mutableStateOf(false) }
        var isVisible by remember { mutableStateOf(true) } // Untuk animasi visibilitas
        var pageVisible by remember { mutableStateOf(false) }
        var otp by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        var loginMessage by remember { mutableStateOf("") }
        var otpSent by remember { mutableStateOf(false) }
        var countdown by remember { mutableStateOf(0) }

        fun isValidPassword(password: String): Boolean {
            val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            return password.matches(regex.toRegex())
        }


        // Function to handle sending OTP
        val onSendOtpClicked = {
            if (countdown == 0) {
                isLoading = true
                otpSent = false
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Send OTP request using the loginUser function
                        val response = NetworkClient.loginUser(email, password)
                        val jsonResponse = JSONObject(response)
                        val message = jsonResponse.getString("message") // Extract the message field
                        withContext(Dispatchers.Main) {
                            loginMessage = message
                            otpSent = message.contains("OTP sent", ignoreCase = true) // Determine OTP success based on the message
                            if (otpSent) {
                                countdown = 60 // Start countdown when OTP is sent successfully
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            loginMessage = e.message ?: "Unknown error occurred"
                            otpSent = false
                        }
                    } finally {
                        isLoading = false
                    }
                }
            }
        }

        // Countdown timer to decrement the countdown every second
        LaunchedEffect(countdown) {
            if (countdown > 0) {
                delay(1000L) // Delay for 1 second
                countdown -= 1
            }
        }

        // Function to handle login after OTP is verified
        val onLoginClicked = {
            if (!otpSent) {
                loginMessage = "Please request OTP first!"
            } else if (otp.isBlank()) {
                loginMessage = "Please enter the OTP."
            } else {
                isLoading = true
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Verify OTP using the verifyOtp function
                        val response = NetworkClient.verifyOtp(email, otp) // Response dari API
                        val jsonResponse = JSONObject(response) // Parsing JSON
                        val message = jsonResponse.getString("message")
                        val token = jsonResponse.optString("token") // Mengambil token jika ada


                        withContext(Dispatchers.Main) {
                            loginMessage = message
                        if (message == "OTP verified successfully" && token.isNotBlank()) {

                            saveUserData(context, token, email)
                            navController.navigate("my_app") {
                                popUpTo(0) { inclusive = true } // Hapus semua rute dari stack
                                launchSingleTop = true
                            }


                        }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            // If the error message is direct from the server, use it
                            loginMessage = e.message ?: "Unknown error occurred"
                        }
                    } finally {
                        isLoading = false
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            // Menunggu halaman pertama kali dirender, kemudian melakukan animasi
            pageVisible = true
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {


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
                        text = "Login to Your Account",
                        fontSize = 36.sp,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Email input
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    textStyle = TextStyle(
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    ),
                    label = {
                        Text(
                            text = "Email",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                            fontFamily = coolveticaFontFamily,
                            fontWeight = FontWeight.Light
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = "Email Icon",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email // Change this line to specify email keyboard
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.05f
                        )
                    )
                )

                // Password input
                // Password input
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        // Cek apakah password valid setiap kali ada perubahan
                        if (isValidPassword(password)) {
                            loginMessage = "" // Hilangkan peringatan jika password valid
                        }
                    },
                    textStyle = TextStyle(
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    ),
                    label = {
                        Text(
                            text = "Password",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                            fontFamily = coolveticaFontFamily,
                            fontWeight = FontWeight.Light
                        )
                    },
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
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.05f
                        )
                    )
                )

                if (password.isNotBlank() && !isValidPassword(password)) {
                    Text(
                        text = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.",
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light
                    )
                }


                // Kirim OTP Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    // OTP TextField
                    TextField(
                        value = otp,
                        onValueChange = { otp = it },
                        textStyle = TextStyle(
                            fontFamily = coolveticaFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 18.sp
                        ),
                        label = {
                            Text(
                                text = "OTP",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                                fontFamily = coolveticaFontFamily,
                                fontWeight = FontWeight.Light
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_email),
                                contentDescription = "OTP Icon",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number // Change this line to specify numeric keyboard
                        ),
                        modifier = Modifier
                            .weight(1f) // This makes the TextField take available space
                            .padding(end = 8.dp), // Add spacing between TextField and Button
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                        )
                    )

                    // Send OTP Button
                    Button(
                        onClick = { onSendOtpClicked() }, // Handle OTP send
                        enabled = countdown == 0,
                        modifier = Modifier.align(Alignment.CenterVertically)
                            ,

                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(text = if (countdown == 0) "Send OTP" else "$countdown",
                            fontFamily = coolveticaFontFamily,
                            fontWeight = FontWeight.Light)
                    }
                }

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }

                if (loginMessage.isNotEmpty()) {
                    AlertDialog(
                        onDismissRequest = { loginMessage = "" }, // Clear message when dialog is dismissed
                        confirmButton = {
                            Button(
                                onClick = { loginMessage = "" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )// Dismiss dialog on button click
                            ) {
                                Text("OK",
                                    fontFamily = coolveticaFontFamily,
                                    fontWeight = FontWeight.Light)
                            }
                        },
                        title = {
                            Text("Login Status") // Dialog title
                        },
                        text = {
                            Text(loginMessage) // Display the login message
                        }
                    )
                }
                // Remember me checkbox
                Spacer(modifier = Modifier.height(16.dp))

                // Sign up button with hover effect
                val buttonColor by animateColorAsState(
                    targetValue = if (rememberMe) Color(0xFF008080) else Color.DarkGray,
                    animationSpec = tween(durationMillis = 300)
                )
                Button(
                    onClick = { onLoginClicked() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    enabled = email.isNotBlank() &&
                            password.isNotBlank() && otp.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
                ) {
                    Text(
                        text = "Sign In",
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

                    MediaButton(R.drawable.image2, scale)

                }

                Spacer(modifier = Modifier.height(16.dp))

                // Already have an account
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Don't have an account?",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sign Up",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp,
                        modifier = Modifier.clickable { navController.navigate("create_account") }
                    )
                }
            }
        }
    }
}

@Composable
fun MediaButton(iconResId: Int, scale: Float) {
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
fun PreviewLogin() {
    val navController = rememberNavController()  // Mock NavController
    LoginAccount(navController = navController)
}