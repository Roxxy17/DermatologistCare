package com.example.dermatologistcare.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> get() = _isDarkMode

    fun toggleDarkMode(isEnabled: Boolean) {
        viewModelScope.launch {
            _isDarkMode.emit(isEnabled)
        }
    }
}
