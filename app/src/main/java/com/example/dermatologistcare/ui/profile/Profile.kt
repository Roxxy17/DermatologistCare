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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
            // Profile Card with Overlapping Profile Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp).shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)), // Tambahkan padding untuk memberi ruang bagi gambar
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)

                    ) {
                        Spacer(modifier = Modifier.height(60.dp)) // Ruang untuk profil image overlap
                        // Profile Name
                        Text(
                            text = "Atmin",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = coolveticaFontFamily,
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clickable { isImageDialogOpen.value = true } // Buka dialog saat gambar diklik
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Dark Mode", fontSize = 16.sp, fontWeight = FontWeight.Normal)
                    Switch(
                        checked = themeState.value.isDarkMode,
                        onCheckedChange = { themeViewModel.toggleTheme() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onSurface, // Color of the thumb when checked
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurface, // Color of the thumb when unchecked
                            checkedTrackColor = MaterialTheme.colorScheme.surface, // Color of the track when checked
                            uncheckedTrackColor = MaterialTheme.colorScheme.surface // Color of the track when unchecked
                        )
                    )

                }
                Text(text = "Change Password", fontSize = 16.sp, fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable { isChangePasswordDialogOpen.value = true }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Logout Button
                TextButton(
                    onClick = { /* Handle logout */ },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Logout")
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
}

@Composable
fun FullImageDialog(imageRes: Int, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable { onDismiss() }, // Tutup dialog saat latar belakang diklik
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Full Profile Picture",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(300.dp) // Ukuran gambar penuh
                .clip(RoundedCornerShape(16.dp))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit) {
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            , // Dismiss when clicking outside
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,

            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Change Password",
                    fontFamily = coolveticaFontFamily,
                    fontWeight = FontWeight.Light, fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                    )
                Spacer(modifier = Modifier.height(16.dp))
                // Current Password
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    textStyle = TextStyle(
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    ),
                    label = { Text(text = "Current Password",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock),
                            contentDescription = "Password Icon",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            ),
                            contentDescription = "Toggle Password Visibility",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // New Password
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    textStyle = TextStyle(
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    ),
                    label = { Text(text = "New Password",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock),
                            contentDescription = "Password Icon",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            ),
                            contentDescription = "Toggle Password Visibility",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Confirm New Password
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    textStyle = TextStyle(
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    ),
                    label = { Text(text = "Confirm New Password",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                        fontFamily = coolveticaFontFamily,
                        fontWeight = FontWeight.Light) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock),
                            contentDescription = "Password Icon",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            ),
                            contentDescription = "Toggle Password Visibility",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onDismiss()
                            },
                        modifier = Modifier

                            .padding(vertical = 8.dp),
                        /*enabled = username.isNotBlank() &&
                                email.isNotBlank() &&
                                password.isNotBlank() ,*/
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = coolveticaFontFamily,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Button(
                        onClick = {

                        },
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        /*enabled = username.isNotBlank() &&
                                email.isNotBlank() &&
                                password.isNotBlank() ,*/
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
                    ) {
                        Text(
                            text = "Confirm",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = coolveticaFontFamily,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }
    }
}



