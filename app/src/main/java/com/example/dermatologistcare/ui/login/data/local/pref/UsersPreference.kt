package com.example.dermatologistcare.ui.login.data.local.pref

import android.content.Context
import android.content.SharedPreferences


fun saveUserData(context: Context, token: String, email: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("TOKEN", token)
    editor.putString("EMAIL", email)
    editor.apply() // Simpan perubahan secara asinkron
}
fun getUserData(context: Context): Pair<String?, String?> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("TOKEN", null)
    val email = sharedPreferences.getString("EMAIL", null)
    return Pair(token, email)
}
