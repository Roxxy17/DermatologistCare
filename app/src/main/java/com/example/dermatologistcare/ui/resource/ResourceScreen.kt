 package com.example.dermatologistcare.ui.resource

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme


@Composable
fun ResourceScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar
        SearchBar(query = searchQuery, onQueryChanged = { searchQuery = it })

        // List of resources (can be updated based on the search query)
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)) {
            items(10) { index ->
                ResourceItem(index)
            }
        }

        // Vertical list of resources (LazyColumn)
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)) {
            items(10) { index ->
                ResourceItem(index)
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = { onQueryChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp)),
        label = { Text("Search resources") },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        }
    )
}
@Composable
fun ResourceItem(index: Int) {
    // Create a Card for each resource item
    Box(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .width(300.dp)
                .height(150.dp) // Set a fixed height for the card
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Title of the resource
                Text(
                    text = "Resource $index",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                // Description of the resource (just a placeholder for now)
                Text(
                    text = "This is a brief description of resource $index. You can add more details here.",
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResourceScreenPreview() {
    DermatologistCareTheme {
        ResourceScreen() // Preview the ResourceScreen with the search bar
    }
}
