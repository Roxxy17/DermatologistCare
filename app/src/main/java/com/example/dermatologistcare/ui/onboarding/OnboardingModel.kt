package com.example.dermatologistcare.ui.onboarding

import androidx.annotation.DrawableRes
import com.example.dermatologistcare.R

sealed class OnboardingModel (
    @DrawableRes val image: Int,
    val title:String,
    val description:String,
){
    data object FirstPage: OnboardingModel(
        image = R.drawable.teresa,
        title = "Detect & Diagnose",
        description = "Snap or upload a photo of your skin condition for quick insights and possible diagnosis."

    )
    data object SecondPage: OnboardingModel(
        image = R.drawable.teresa,
        title = "Learn & Understand",
        description = "Access detailed information on symptoms, causes, and treatments for various skin conditions."

    )
    data object ThirdPage: OnboardingModel(
        image = R.drawable.teresa,
        title = "Track Your Progress",
        description = "Log updates and monitor changes over time to see how your skin responds to treatments."

    )
}