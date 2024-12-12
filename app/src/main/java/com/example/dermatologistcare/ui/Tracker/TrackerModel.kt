package com.example.dermatologistcare.ui.Tracker

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dermatologistcare.R

@Composable
fun ConditionList() {
    // Sample data
    val conditions = listOf(
        Condition(
            title = "Tinea Ringworm Candidiasis",
            image = R.drawable.ring,
            severity = "Mild",
            symptoms = "Dry skin, itching, redness.",
            timestamp = "10:10 AM"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth() // The list fills the width of the screen
            .padding(horizontal = 16.dp, vertical = 8.dp) // Padding for the list as a whole
    ) {
        items(conditions.size) { index ->  // Display dynamic cards based on conditions list
            ConditionCard(
                condition = conditions[index]
            )
            Spacer(modifier = Modifier.height(16.dp)) // Space between cards
        }
    }
}