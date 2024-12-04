package com.example.dermatologistcare.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermatologistcare.R // Pastikan nama package sesuai dengan proyek Anda

@Preview
@Composable
fun LoginScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(75.dp)
                    .offset(y = 50.dp)
                    .height(200.dp), // Tinggi disesuaikan

                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.frame_58), // Ganti dengan nama file gambar
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(400.dp) // Ukuran gambar
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Let's you in",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50) // Green color
                )

                Spacer(modifier = Modifier.height(32.dp))

                SocialButton("Continue with Facebook")
                Spacer(modifier = Modifier.height(16.dp))
                SocialButton("Continue with Google")
                Spacer(modifier = Modifier.height(16.dp))
                SocialButton("Continue with Apple")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "or",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* TODO: Handle password login */ },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text(
                        text = "Sign in with password",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Don't have an account?",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign up",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.clickable { /* TODO: Handle sign up */ }
                )
            }
        }
    }
}

@Composable
fun SocialButton(text: String) {
    Button(
        onClick = { /* TODO: Handle button click */ },
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(10.dp)), // Outline hitam
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent // Latar belakang transparan
        ),
        shape = RoundedCornerShape(10.dp) // Bentuk tombol
    ) {
        Text(
            text = text,
            color = Color.Black, // Warna teks hitam agar sesuai dengan outline
            fontWeight = FontWeight.Bold
        )
    }
}

