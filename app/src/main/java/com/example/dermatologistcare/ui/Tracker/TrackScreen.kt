package com.example.dermatologistcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.dermatologistcare.ui.Tracker.ConditionList


class TrackScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val diseaseName = intent.getStringExtra("diseaseName")
        val imageUri = intent.getStringExtra("imageUri")

        setContent {
            TrackScreen()
        }
    }
}

@Composable
fun TrackScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {

            ConditionList()  // Add the condition list here

    }
}

@Preview(showBackground = true)
@Composable
fun TrackScreenPreview() {
    TrackScreen()
}
