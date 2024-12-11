package com.example.dermatologistcare.ui.profile.achievement

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AchievementViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AchievementViewModel::class.java)) {
            return AchievementViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}