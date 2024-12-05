package com.example.dermatologistcare.ui.onboarding

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermatologistcare.R
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily
import com.example.dermatologistcare.ui.theme.text
import kotlinx.coroutines.delay

@Composable
fun OnboardingGraphUI(onboardingModel: OnboardingModel) {
    val iconColor = remember { mutableStateOf(Color(0xFF229799)) } // Initial color
    val iconColor2 = remember { mutableStateOf(Color(0xFF229799)) }
    val animatedColor = animateColorAsState(
        targetValue = iconColor.value,
        animationSpec = tween(durationMillis = 500)
    )
    val animatedColor2 = animateColorAsState(
        targetValue = iconColor2.value,
        animationSpec = tween(durationMillis = 500)
    )

    // Color animation logic
    LaunchedEffect(Unit) {
        while (true) {
            iconColor.value = Color(0xFF229799)
            iconColor2.value = Color(0xFFF5F5F5)
            delay(1000)
            iconColor.value = Color(0xFF229799)
            iconColor2.value = Color(0xFF229799)
            delay(1000)
            iconColor.value = Color(0xFFF5F5F5)
            iconColor2.value = Color(0xFF229799)
            delay(1000)
        }
    }

    val isDarkMode = isSystemInDarkTheme()

    DermatologistCareTheme(darkTheme = isDarkMode) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.weight(1f)) // Push content to the bottom

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = onboardingModel.image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(330.dp, 192.dp)
                            .padding(bottom = 30.dp),
                        alignment = Alignment.Center
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth() // Makes the row take up full width
                            .padding(top = 16.dp, end = 16.dp), // Optional padding to keep icons from touching the right edge
                        horizontalArrangement = Arrangement.End, // Aligns the icons to the end (right side)
                        verticalAlignment = Alignment.CenterVertically // Aligns icons vertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.next), // Replace with your drawable ID
                            contentDescription = "Logo Animation",
                            tint = animatedColor.value,
                            modifier = Modifier.size(60.dp) // Adjust the size as needed
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.next), // Replace with your drawable ID
                            contentDescription = "Logo Animation",
                            tint = animatedColor2.value,
                            modifier = Modifier.size(60.dp) // Adjust the size as needed
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Text(
                        text = onboardingModel.title,
                        fontSize = 48.sp,
                        textAlign = TextAlign.Left,
                        fontFamily = coolveticaFontFamily,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = onboardingModel.description,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Left,
                        fontFamily = coolveticaFontFamily,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(110.dp)) // Push content to the bottom)
                }

                Spacer(modifier = Modifier.height(24.dp)) // Space before button

                    // Action for skip button
                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun OnboardingGraphUIPreview() {
    DermatologistCareTheme {
        OnboardingGraphUI(onboardingModel = OnboardingModel.FirstPage)
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingGraphUIPreview2() {
    DermatologistCareTheme {
        OnboardingGraphUI(onboardingModel = OnboardingModel.SecondPage)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OnboardingGraphUIPreview3Dark() {
    DermatologistCareTheme(darkTheme = true) {
        OnboardingGraphUI(onboardingModel = OnboardingModel.ThirdPage)
    }
}
