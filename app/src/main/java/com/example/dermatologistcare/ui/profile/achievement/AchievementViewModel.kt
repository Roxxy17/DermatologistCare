package com.example.dermatologistcare.ui.profile.achievement

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AchievementViewModel(private val context: Context) : ViewModel() {
    private var loginStreak = getLoginStreak(context)

    // Fungsi untuk mendapatkan streak login
    fun getLoginStreak(): Int {
        return loginStreak
    }

    // Fungsi untuk menambah streak
    fun incrementStreak() {
        loginStreak += 1
        saveLoginStreak(context, loginStreak)
    }

    // Fungsi untuk mereset streak
    fun resetStreak() {
        loginStreak = 0
        saveLoginStreak(context, loginStreak)
    }

    // Fungsi untuk mengecek pencapaian
    fun checkAndSaveAchievement() {
        if (loginStreak >= 7) {
            // Jika streak mencapai 7 hari, simpan pencapaian
            saveAchievement(context, "Wildfire", "Reach a 7-day streak")
        }
    }

    // Fungsi untuk menyimpan achievement ke SharedPreferences
    private fun saveAchievement(context: Context, title: String, description: String) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("achievement_title", title)
        editor.putString("achievement_description", description)
        editor.apply()
    }

    // Fungsi untuk menyimpan streak ke SharedPreferences
    private fun saveLoginStreak(context: Context, streak: Int) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("login_streak", streak)
        editor.apply()
    }

    // Fungsi untuk mendapatkan streak dari SharedPreferences
    private fun getLoginStreak(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("login_streak", 0)
    }
}

