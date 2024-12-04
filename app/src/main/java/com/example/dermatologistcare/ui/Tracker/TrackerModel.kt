package com.example.dermatologistcare.ui.Tracker

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConditionList() {
    // Sample data
    val conditions = listOf(
        Condition(
            title = "Cancer",
            severity = "Mild",
            symptoms = "Dry skin, itching, redness.",
            timestamp = "10:10 AM - 3 DAYS AGO"
        ),
        Condition(
            title = "Cancer",
            severity = "Moderate",
            symptoms = "Redness, swelling, pain.",
            timestamp = "11:00 AM - 1 DAY AGO"
        ),
        Condition(
            title = "Cancer",
            severity = "Severe",
            symptoms = "Severe redness, blistering, pain.",
            timestamp = "12:30 PM - 2 HOURS AGO"
        ),
        Condition(
            title = "Cancer",
            severity = "Moderate",
            symptoms = "Itching, rash, dry patches.",
            timestamp = "1:00 PM - 4 HOURS AGO"
        ),
        Condition(
            title = "Cancer",
            severity = "Mild",
            symptoms = "Itching, irritation, mild redness.",
            timestamp = "2:30 PM - 30 MIN AGO"
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