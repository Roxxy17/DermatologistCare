package com.example.dermatologistcare

import android.content.Context
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("DermaCarePrefs", Context.MODE_PRIVATE)

    // Read tracking state from SharedPreferences
    val isTracking = remember {
        mutableStateOf(sharedPref.getBoolean("isTracking", false))
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {

        // Hanya tampilkan ConditionList jika tracking aktif
        if (isTracking.value) {
            ConditionList()
        } else {
            Text("Start tracking by clicking 'Track' in the previous screen")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TrackScreenPreview() {
    TrackScreen()
}
