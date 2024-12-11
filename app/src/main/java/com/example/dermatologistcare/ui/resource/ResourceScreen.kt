package com.example.dermatologistcare.ui.resource

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceScreen(viewModel: ArticleViewModel = viewModel(),context: Context
) {
    LaunchedEffect(Unit) {
        viewModel.fetchArticles()
    }

    val articlesState = viewModel.articlesState.value

    Scaffold(
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = articlesState) {
                is ArticlesUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ArticlesUiState.Success -> {
                    LazyColumn {
                        items(state.articles) { article ->
                            ArticleItem(
                                article,
                                context

                            )
                        }
                    }
                }
                is ArticlesUiState.Error -> {
                    Text(
                        text = state.message,
                        fontWeight = FontWeight.Light,
                        fontFamily = coolveticaFontFamily,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

fun formatDate(isoDateString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(isoDateString, DateTimeFormatter.ISO_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        dateTime.format(formatter)
    } catch (e: Exception) {
        isoDateString // Fallback to original string if parsing fails
    }
}


@Composable
fun ArticleItem(article: ResourceResponse, context: Context) {
    val uriHandler = LocalUriHandler.current
    Card( modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Publication Date in top right
            Text(
                text = "Date Published: " + formatDate(article.publication_date),
                fontWeight = FontWeight.Light,
                fontFamily = coolveticaFontFamily,
                modifier = Modifier.align(Alignment.TopEnd)
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    article.source_type,
                    fontWeight = FontWeight.Light,

                    fontFamily = coolveticaFontFamily,
                    modifier = Modifier
                        .background(color = Color.Blue.copy(alpha = 0.1F),
                            shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
                Text(
                    text = article.title,
                    fontWeight = FontWeight.Normal,
                    fontSize = 36.sp,
                    fontFamily = coolveticaFontFamily,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Author: ${article.author}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    fontFamily = coolveticaFontFamily,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.abstract,
                    fontWeight = FontWeight.Normal,
                    fontFamily = coolveticaFontFamily,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (article.url.isNotBlank()) {
                            try {
                                uriHandler.openUri(article.url)

                                incrementOpenSourceClickCount(context)
                            } catch (e: Exception) {
                                // Handle any exceptions
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors =   ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Open Source",
                        fontWeight = FontWeight.Light,
                        fontFamily = coolveticaFontFamily,)

                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25F)
                )

                // Created At in bottom left
                Text(
                    text = formatDate(article.created_at),
                    fontWeight = FontWeight.Light,
                    fontFamily = coolveticaFontFamily,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}
// Function to get Open Source Click count
fun getOpenSourceClickCount(context: Context): Int {
    val prefs = context.getSharedPreferences("LoginAchievementPrefs", Context.MODE_PRIVATE)
    return prefs.getInt("openSourceClickCount", 0)
}

// Function to save Open Source Click count
fun saveOpenSourceClickCount(context: Context, count: Int) {
    val prefs = context.getSharedPreferences("LoginAchievementPrefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("openSourceClickCount", count).apply()
}

// Function to increment Open Source Click count
fun incrementOpenSourceClickCount(context: Context) {
    val currentCount = getOpenSourceClickCount(context)
    val newCount = currentCount + 1
    saveOpenSourceClickCount(context, newCount)
}
