package com.example.dermatologistcare.ui.login

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dermatologistcare.R
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily

@Composable
fun LoginScreenWithNoAnimations(navController: NavHostController) {
    val isDarkMode = isSystemInDarkTheme()
    val scrollState = rememberScrollState()
    DermatologistCareTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo tanpa animasi
                Image(
                    painter = painterResource(id = if (isSystemInDarkTheme())
                        R.drawable.frame_58_dark
                    else R.drawable.frame_58),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(top = 32.dp)

                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Let's you in",
                        fontSize = 48.sp,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.tertiary
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Tombol login sosial tanpa animasi
                    listOf(
                        Pair("Continue with Facebook", R.drawable.image1),
                        Pair("Continue with Google", R.drawable.image2),
                        Pair("Continue with Apple", R.drawable.image3)
                    ).forEach { item ->
                        SocialButtonLogin(
                            text = item.first,
                            logoResId = item.second,
                            onClick = { /* TODO: Handle button click */ },
                            scale = 1f
                        )
                        Spacer(modifier = Modifier.height(16.dp)) // Tambahkan jarak antar tombol
                    }

                    Text(
                        text = "or",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                        , fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol login tanpa animasi
                    Button(
                        onClick = { /* Handle password login */ },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
                    ) {
                        Text(
                            text = "Sign in with password",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = coolveticaFontFamily,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Don't have an account?",
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface

                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sign up",
                        fontSize= 20.sp,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.clickable { navController.navigate("create_account") }
                    )
                }
            }
        }
    }
}

@Composable
fun SocialButtonLogin(
    text: String,
    logoResId: Int,
    onClick: () -> Unit,
    scale: Float = 1f
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Image(
                painter = painterResource(id = logoResId),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 20.sp,
                fontFamily = coolveticaFontFamily,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreenWithNoAnimations() {
    val navController = rememberNavController()  // Mock NavController
    LoginScreenWithNoAnimations(navController = navController)
}
