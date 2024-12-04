package com.example.dermatologistcare.ui.Tracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.dermatologistcare.R

// Data class to hold condition details
data class Condition(
    val title: String,
    val severity: String,
    val symptoms: String,
    val timestamp: String
)

@Composable
fun ConditionCard(
    condition: Condition,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp), // Card padding
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Ganti dengan warna yang diinginkan
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) // Padding inside the card for spacing
        ) {
            // Gambar Section (bisa disesuaikan dengan gambar yang diinginkan)
            Image(
                painter = painterResource(id = R.drawable.kulit), // Ganti dengan gambar Anda
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Ukuran gambar, bisa disesuaikan
                    .clip(RoundedCornerShape(12.dp)) // Memberikan sudut melengkung pada gambar
            )

            // Title Section
            Text(
                text = condition.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp) // Menambah ruang antara gambar dan title
            )

            // Symptoms Label and Text
            Text(
                text = "Symptoms : ",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = condition.symptoms,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.DarkGray),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Horizontal Divider between symptoms and timestamp
            Divider(
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Row for Timestamp and Severity
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Timestamp
                Text(
                    text = condition.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                // Severity
                Text(
                    text = condition.severity,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = when (condition.severity) {
                            "Mild" -> Color.Green
                            "Moderate" -> Color.Yellow
                            "Severe" -> Color.Red
                            else -> Color.Gray
                        }
                    ),
                    modifier = Modifier
                        .background(
                            color = when (condition.severity) {
                                "Mild" -> Color.Green.copy(alpha = 0.1f)
                                "Moderate" -> Color.Yellow.copy(alpha = 0.1f)
                                "Severe" -> Color.Red.copy(alpha = 0.1f)
                                else -> Color.Gray.copy(alpha = 0.1f)
                            },
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}


