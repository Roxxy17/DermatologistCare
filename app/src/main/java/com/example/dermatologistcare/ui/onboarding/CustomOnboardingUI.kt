import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermatologistcare.R
import com.example.dermatologistcare.ui.onboarding.ButtonUi
import com.example.dermatologistcare.ui.onboarding.OnboardingModel
import com.example.dermatologistcare.ui.onboarding.OnboardingScreen
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily
import kotlinx.coroutines.delay

@Composable
fun CustomOnboardingUI(onboardingModel: OnboardingModel.CustomPage) {

    val iconColor = remember { mutableStateOf(Color(0xFF229799)) } // Initial color
    val iconColor2 = remember { mutableStateOf(Color(0xFF229799)) }
    // Smoothly animate the color change
    val animatedColor = animateColorAsState(
        targetValue = iconColor.value,
        animationSpec = tween(durationMillis = 500, easing = { it }) // Smooth transition
    )

    val animatedColor2 = animateColorAsState(
        targetValue = iconColor2.value,
        animationSpec = tween(durationMillis = 500, easing = { it }) // Smooth transition
    )

    // Use LaunchedEffect to loop the color change every 1 second
    LaunchedEffect(Unit) {
        while (true) {
            iconColor.value = Color(0xFF229799) // Green
            iconColor2.value = Color(0xFFF5F5F5) // Light Gray
            delay(1000)  // Wait for 2 seconds
            iconColor.value = Color(0xFF229799) // Green
            iconColor2.value = Color(0xFF229799) // Light Gray
            delay(1000)  // Wait for 2 seconds

            iconColor.value = Color(0xFFF5F5F5) // Light Gray
            iconColor2.value = Color(0xFF229799) // Green
            delay(1000)  // Wait for 2 seconds

        }
    }





    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image with zoom
        Image(
            painter = painterResource(id = onboardingModel.image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Ensures the image scales to fill without distortion
        )

        // Apply an overlay gradient for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                    )
                )
        )

        // Content overlaying the background
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp) // Adjust spacing between the texts
            ) {


                Text(
                    text = "Welcome to",
                    fontFamily = coolveticaFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 36.sp,
                    color = Color(0xFF424242),
                    modifier = Modifier.offset(y = (40).dp)
                )

                Text(
                    text = "C-Derm",
                    fontFamily = coolveticaFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 96.sp,
                    color = Color(0xFF229799),
                    modifier = Modifier.offset(y = (20).dp)

                )
            }
            Text(
                text = "Your personal companion for healthier skin. " +
                        "Our mission is to empower you with tools and knowledge " +
                        "to take control of your skin health.",
                fontFamily = coolveticaFontFamily,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 32.sp,
                lineHeight = 32.sp,
                color = Color(0xFF424242)
            )
            Spacer(modifier = Modifier.size(110.dp))
        }

            Icon(
                painter = painterResource(id = R.drawable.next), // Replace with your drawable ID
                contentDescription = "Logo Animation",
                tint = animatedColor.value,
                modifier = Modifier
                    .size(80.dp) // Adjust the size as needed
                    .align(Alignment.CenterEnd)
            )

        Icon(
            painter = painterResource(id = R.drawable.next), // Replace with your drawable ID
            contentDescription = "Logo Animation",
            tint = animatedColor2.value,
            modifier = Modifier
                .size(80.dp) // Adjust the size as needed
                .align(Alignment.CenterEnd)
                .offset(x = (-80).dp)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen {

    }
}