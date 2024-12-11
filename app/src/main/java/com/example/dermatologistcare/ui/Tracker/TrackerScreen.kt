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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import com.example.dermatologistcare.R
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily

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
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
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
                fontWeight = FontWeight.Normal,
                fontFamily = coolveticaFontFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp) // Menambah ruang antara gambar dan title
            )

            // Symptoms Label and Text
            Text(
                text = "Symptoms : ",
                fontWeight = FontWeight.Light,
                fontFamily = coolveticaFontFamily,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = condition.symptoms,
                fontWeight = FontWeight.Light,
                fontFamily = coolveticaFontFamily,
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
                    fontWeight = FontWeight.Light,
                    fontFamily = coolveticaFontFamily,
                    color = Color.Gray
                )

                // Severity
                Text(
                    text = condition.severity,
                    fontWeight = FontWeight.Light,
                    fontFamily = coolveticaFontFamily,
                    style = MaterialTheme.typography.bodyMedium.copy(
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


