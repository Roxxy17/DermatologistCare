package com.example.dermatologistcare.ui.profile

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


fun performLogout(
    context: Context,
    email: String?,
    token: String?,
    onLogoutSuccess: () -> Unit,
    onLogoutError: (String) -> Unit
) {
    if (email.isNullOrBlank() || token.isNullOrBlank()) {
        Log.e("Logout", "Authentication data is missing")
        onLogoutError("Authentication data is missing")
        return
    }

    val client = OkHttpClient()

    val jsonBody = JSONObject().apply {
        put("email", email)
    }.toString()

    val requestBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("https://capd-442807.et.r.appspot.com/logout")
        .post(requestBody)
        .addHeader("Authorization", "Bearer $token")
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()

            // Log the full response details
            Log.d("Logout", "Response Code: ${response.code}")
            Log.d("Logout", "Response Message: ${response.message}")

            // Log response headers
            response.headers.forEach { (name, value) ->
                Log.d("Logout", "Header - $name: $value")
            }

            // Log response body
            val responseBody = response.body?.string() ?: "Empty response body"
            Log.d("Logout", "Response Body: $responseBody")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Clear user data from shared preferences
                    clearUserData(context)

                    // Log successful logout
                    Log.i("Logout", "Logout successful for email: $email")

                    onLogoutSuccess()
                } else {
                    // Log logout failure
                    Log.e("Logout", "Logout failed. Code: ${response.code}, Body: $responseBody")
                    onLogoutError("Logout failed: $responseBody")
                }
            }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                // Log network error
                Log.e("Logout", "Network error during logout", e)
                onLogoutError("Network error: ${e.message}")
            }
        }
    }
}


// Function to clear user data from shared preferences
fun clearUserData(context: Context) {
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    sharedPref.edit().apply {
        remove("TOKEN") // Kunci harus sama seperti saat menyimpan
        remove("EMAIL") // Kunci harus sama seperti saat menyimpan
        apply() // Simpan perubahan
    }
}