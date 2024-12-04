package com.example.dermatologistcare.ui.onboarding

import androidx.annotation.DrawableRes
import com.example.dermatologistcare.R

sealed class OnboardingModel (
    @DrawableRes val image: Int,
    val title:String,
    val description:String,
){
    data object CustomPage : OnboardingModel(
        image = R.drawable.welcome, // Replace with the resource for your custom image
        title = "Personalized Experience",
        description = "Tailor your journey with features designed to fit your needs."
    )

    data object FirstPage: OnboardingModel(
        image = R.drawable.detect,
        title = "Detect & Diagnose",
        description = "Snap or upload a photo of your skin condition for quick insights and possible diagnosis."

    )
    data object SecondPage: OnboardingModel(
        image = R.drawable.learn,
        title = "Learn & Understand",
        description = "Access detailed information on symptoms, causes, and treatments for various skin conditions."

    )
    data object ThirdPage: OnboardingModel(
        image = R.drawable.learn,
        title = "Track Your Progress",
        description = "Log updates and monitor changes over time to see how your skin responds to treatments."

    )
}

