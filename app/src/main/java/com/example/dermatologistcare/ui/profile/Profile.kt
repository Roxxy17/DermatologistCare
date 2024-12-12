package com.example.dermatologistcare.ui.profile

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import java.time.LocalDate
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.dermatologistcare.MainActivity
import com.example.dermatologistcare.R
import com.example.dermatologistcare.saveLoginState
import com.example.dermatologistcare.setting.ThemeViewModel
import com.example.dermatologistcare.ui.home.Background
import com.example.dermatologistcare.ui.login.CreateAccountScreen
import com.example.dermatologistcare.ui.login.data.local.pref.getUserData
import com.example.dermatologistcare.ui.resource.getOpenSourceClickCount
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily



@Composable
fun ProfileScreen(context: Context, themeViewModel: ThemeViewModel = viewModel(),navController: NavController) {
    val scrollState = rememberScrollState()
    val isImageDialogOpen = remember { mutableStateOf(false) }
    val themeState = themeViewModel.themeState.collectAsState()

    val isLogoutDialogOpen = remember { mutableStateOf(false) }

    val isChangePasswordDialogOpen = remember { mutableStateOf(false) }

    val loginCount = remember { mutableStateOf(getLoginCount(context)) }
    val targetLoginCount = 7
    val isAchievementCompleted = loginCount.value >= targetLoginCount

    val isTodayLoggedIn = remember { mutableStateOf(false) }
// Penentuan warna border berdasarkan status pencapaian

    val profileBorderColor = if (isAchievementCompleted) Color(0xFFFFD700) else Color.Gray
    val openSourceClickCount = remember { mutableStateOf(getOpenSourceClickCount(context)) }
    val targetOpenSourceClick = 7
    val isOpenSourceAchievementCompleted = openSourceClickCount.value >= targetOpenSourceClick

    val (token, email) = getUserData(context)
    // Cek status login hari ini
    LaunchedEffect(Unit) {
        val lastLoginDate = getLastLoginDate(context)
        val today = LocalDate.now()

        if (lastLoginDate == today) {
            isTodayLoggedIn.value = true
        } else if (lastLoginDate != null && lastLoginDate.isBefore(today)) {

            isTodayLoggedIn.value = false
        }
    }

    Background()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Card with Icon in the Top-Right Corner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp) // Memberi ruang untuk gambar overlap
                ) {
                    // Card Background
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.height(60.dp)) // Ruang untuk profil image overlap
                            // Profile Name
                            Text(
                                text =   if (email != null) "$email!" else "Welcome!",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Light,
                                fontFamily = coolveticaFontFamily,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Badge",
                                fontWeight = FontWeight.Light,
                                fontFamily = coolveticaFontFamily,


                            )
                            if (isOpenSourceAchievementCompleted) {
                                Image(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .offset(x = (-16).dp, y = 16.dp).size(100.dp),
                                    painter = painterResource(id = R.drawable.ic_edit),
                                    contentDescription = "Edit Profile",
                                )

                            } else {
                                Text(
                                    text = "Get Knowledge Seeker to get badge",
                                    fontWeight = FontWeight.Light,
                                    fontFamily = coolveticaFontFamily,
                                    )
                            }


                        }

                    }

                }

                // Profile Picture
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, profileBorderColor, CircleShape)
                        .clickable { isImageDialogOpen.value = true }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pp),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }


            // Achievement Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Achievement",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = coolveticaFontFamily,

                )
                Spacer(modifier = Modifier.height(8.dp))
//                AchievementItem("Wildfire", "Login for 7 days", 6, 7)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),

                    shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )

                ){
                Column(modifier = Modifier.padding(16.dp)) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_track), // Ganti dengan ikon
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Wildfire",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = coolveticaFontFamily,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Login for 7 days and get Gold Border",
                        fontSize = 14.sp,
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { loginCount.value / targetLoginCount.toFloat() },
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.tertiary,
                        trackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
                    )
                    Text(
                        text = "Progress: ${loginCount.value}/$targetLoginCount",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = coolveticaFontFamily,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button untuk menambah progres
                    Button(
                        onClick = {
                            if (!isAchievementCompleted && !isTodayLoggedIn.value) {
                                loginCount.value++
                                saveLoginCount(context, loginCount.value)
                                saveLastLoginDate(context, LocalDate.now())
                                isTodayLoggedIn.value = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(50.dp) // Adjust height as needed
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        enabled = !isAchievementCompleted && !isTodayLoggedIn.value
                    ) {
                        Text(
                            text = when {
                                isAchievementCompleted -> "Completed!"
                                isTodayLoggedIn.value -> "Logged In Today"
                                else -> "Login Today"
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = coolveticaFontFamily,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_track), // Replace with your icon
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Knowledge Seeker",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = coolveticaFontFamily,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Learn About 7 diseases",
                            fontSize = 14.sp,
                            fontFamily = coolveticaFontFamily,
                            fontWeight = FontWeight.Light
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = openSourceClickCount.value / targetOpenSourceClick.toFloat(),
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.tertiary,
                            trackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f),
                            strokeCap = StrokeCap.Round,
                        )
                        Text(
                            text = "Progress: ${openSourceClickCount.value}/$targetOpenSourceClick",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = coolveticaFontFamily,
                        )
                        if (isOpenSourceAchievementCompleted) {
                            Button(
                            onClick = {

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(50.dp) // Adjust height as needed
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            enabled = !isOpenSourceAchievementCompleted
                        ) {
                            Text(
                                text =  "Completed!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                                fontFamily = coolveticaFontFamily,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                    }
                }
            }

            // Preference Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Preference",
                    fontSize = 20.sp,

                    fontWeight = FontWeight.Light,
                    fontFamily = coolveticaFontFamily,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),

                    shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )

                ){Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Dark Mode", fontSize = 20.sp, fontWeight = FontWeight.Light,
                        fontFamily = coolveticaFontFamily)
                    Switch(
                        checked = themeState.value.isDarkMode,
                        onCheckedChange = { themeViewModel.toggleTheme() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onSurface, // Color of the thumb when checked
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurface, // Color of the thumb when unchecked
                            checkedTrackColor = MaterialTheme.colorScheme.tertiary, // Color of the track when checked
                            uncheckedTrackColor = MaterialTheme.colorScheme.surface // Color of the track when unchecked
                        )
                    )

                }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(vertical = 8.dp)
                    )



                // Logout Button
                    TextButton(
                        onClick = { isLogoutDialogOpen.value = true },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) {
                        Text(
                            text = "Logout",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = coolveticaFontFamily,
                                fontWeight = FontWeight.Light
                            )
                        )
                    }

                }
                }
            }
        }
    }
    if (isChangePasswordDialogOpen.value) {
        ChangePasswordDialog(onDismiss = { isChangePasswordDialogOpen.value = false })
    }
    // Dialog untuk menampilkan gambar penuh
    if (isImageDialogOpen.value) {
        FullImageDialog(
            imageRes = R.drawable.pp,
            onDismiss = { isImageDialogOpen.value = false }
        )
    }

    if (isLogoutDialogOpen.value) {
        LogoutConfirmationDialog(
            onDismiss = { isLogoutDialogOpen.value = false },
            onConfirm = {
                performLogout(
                    context = context,
                    email = email,
                    token = token,
                    onLogoutSuccess = {
                        // Navigate to login screen or perform necessary actions
                        clearUserData(context)
                        Toast.makeText(context, "Logout successful", Toast.LENGTH_SHORT).show()
                        saveLoginState(isLoggedIn = false, context)
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    },
                    onLogoutError = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
                isLogoutDialogOpen.value = false
            }
        )
    }
}
// Function to get SharedPreferences
// Function to get SharedPreferences
fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("LoginAchievementPrefs", Context.MODE_PRIVATE)
}

// Save the last login date
fun saveLastLoginDate(context: Context, date: LocalDate) {
    val prefs = getSharedPreferences(context)
    prefs.edit().putString("lastLoginDate", date.toString()).apply()
}

// Get the last login date
fun getLastLoginDate(context: Context): LocalDate? {
    val prefs = getSharedPreferences(context)
    val dateString = prefs.getString("lastLoginDate", null)
    return dateString?.let { LocalDate.parse(it) }
}

// Save login count
fun saveLoginCount(context: Context, count: Int) {
    val prefs = getSharedPreferences(context)
    prefs.edit().putInt("loginCount", count).apply()
}

// Get login count
fun getLoginCount(context: Context): Int {
    val prefs = getSharedPreferences(context)
    return prefs.getInt("loginCount", 0)
}