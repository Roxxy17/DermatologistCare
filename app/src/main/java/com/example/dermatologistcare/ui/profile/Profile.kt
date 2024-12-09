package com.example.dermatologistcare.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dermatologistcare.R
import com.example.dermatologistcare.setting.ThemeViewModel
import com.example.dermatologistcare.ui.home.Background
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily

@Composable
fun ProfileScreen(themeViewModel: ThemeViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val isImageDialogOpen = remember { mutableStateOf(false) }
    val themeState = themeViewModel.themeState.collectAsState()

    val isLogoutDialogOpen = remember { mutableStateOf(false) }

    val isChangePasswordDialogOpen = remember { mutableStateOf(false) }
    Background()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                                text = "Atmin",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Light,
                                fontFamily = coolveticaFontFamily,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }

                    // Edit Icon in the Top-Right Corner of the Card
                   IconButton(onClick = {},
                       modifier = Modifier

                           .align(Alignment.TopEnd)
                           .offset(x = (-16).dp, y = 16.dp)

                       ) {
                       Icon(

                           painter = painterResource(id = R.drawable.ic_edit),
                           contentDescription = "Edit Profile",
                           tint = MaterialTheme.colorScheme.tertiary,
                           modifier = Modifier.size(24.dp)
                       )
                   }
                }

                // Profile Picture
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clickable { isImageDialogOpen.value = true }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.teresa),
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
                AchievementItem("Wildfire", "Reach a 7-day streak", 6, 7)
                AchievementItem("Scholar", "Learn about 7 diseases", 6, 7)
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
                Text(text = "Change Password", fontSize = 20.sp, fontWeight = FontWeight.Light,
                    fontFamily = coolveticaFontFamily,
                    modifier = Modifier.clickable { isChangePasswordDialogOpen.value = true }
                        .padding(vertical = 8.dp)
                )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(vertical = 8.dp)
                    )



                // Logout Button
                TextButton(
                    onClick = { isLogoutDialogOpen.value = true},
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red),

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
            imageRes = R.drawable.teresa,
            onDismiss = { isImageDialogOpen.value = false }
        )
    }

    if (isLogoutDialogOpen.value) {
        LogoutConfirmationDialog(
            onDismiss = { isLogoutDialogOpen.value = false },
            onConfirm = {

            }
        )
    }
}


@Composable
fun AchievementItem(title: String, subtitle: String, progress: Int, max: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),

        shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_track), // Ganti dengan ikon
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, fontSize = 16.sp, fontFamily = coolveticaFontFamily, fontWeight = FontWeight.Light)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, fontSize = 14.sp, fontFamily = coolveticaFontFamily, fontWeight = FontWeight.Light)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress / max.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
            )
        }
    }
}
