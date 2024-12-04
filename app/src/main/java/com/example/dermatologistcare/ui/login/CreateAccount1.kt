package com.example.dermatologistcare.ui.login

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermatologistcare.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CreateAccountScreen() {
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
        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var rememberMe by remember { mutableStateOf(false) }
        var isVisible by remember { mutableStateOf(true) } // Untuk animasi visibilitas

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
                    painter = painterResource(id = R.drawable.frame_58),
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
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF008080)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // UserName Input
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "UserName") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_akun),
                        contentDescription = "UserName"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF5F5F5))
            )

            // Email input
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = "Email Icon"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF5F5F5))
            )

            // Password input
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = "Password Icon"
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                        ),
                        contentDescription = "Toggle Password Visibility",
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF5F5F5))
            )

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
                    colors = CheckboxDefaults.colors(checkmarkColor = Color(0xFF008080))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Remember me", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign up button with hover effect
            val buttonColor by animateColorAsState(
                targetValue = if (rememberMe) Color(0xFF008080) else Color.DarkGray,
                animationSpec = tween(durationMillis = 300)
            )
            Button(
                onClick = { isVisible = !isVisible },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                Text(
                    text = "Sign up",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Or continue with
            Text(
                text = "or continue with",
                color = Color.Gray,
                fontSize = 14.sp,
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
                Text(text = "Already have an account?", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign in",
                    color = Color(0xFF008080),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { /* TODO: Handle sign in */ }
                )
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
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = "Social Media Icon",
            modifier = Modifier.size(32.dp)
        )
    }
}
