package com.example.dermatologistcare.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext



private val DarkColorScheme = darkColorScheme(
    primary = d_base,
    secondary = d_accent,
    tertiary = d_highlight,
    surface = d_base,
    background = Color.Transparent,
    secondaryContainer = d_accent,
    onSurface = text,
    onBackground = text

)

private val LightColorScheme = lightColorScheme(
    primary = base,
    secondary = accent,
    tertiary = highlight,
    surface = base,
    secondaryContainer =Color.Transparent,
    background = Color.Transparent,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = d_text,
    onSurface = d_text

)

@Composable
fun DermatologistCareTheme(
    darkTheme: Boolean = false,

    content: @Composable () -> Unit
) {



    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}