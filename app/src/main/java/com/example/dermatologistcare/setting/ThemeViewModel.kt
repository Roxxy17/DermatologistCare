package com.example.dermatologistcare.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dermatologistcare.setting.ThemePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ThemeState(
    val isDarkMode: Boolean = false,
    val isLoading: Boolean = true
)

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val themePreferences = ThemePreferences(application)

    val themeState: StateFlow<ThemeState> = themePreferences.isDarkMode
        .map { isDarkMode ->
            ThemeState(
                isDarkMode = isDarkMode,
                isLoading = false
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ThemeState()
        )

    fun toggleTheme() {
        viewModelScope.launch {
            themePreferences.toggleTheme()
        }
    }
}
