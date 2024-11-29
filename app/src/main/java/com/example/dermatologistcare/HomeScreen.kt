package com.example.dermatologistcare

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dermatologistcare.setting.ThemeViewModel
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme
import com.example.dermatologistcare.ui.theme.highlight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun Background(
    isProfileScreen: Boolean = false,
    themeViewModel: ThemeViewModel = viewModel()
) {
    val themeState = themeViewModel.themeState.collectAsState()

    if (!themeState.value.isLoading) {
        Image(
            painter = if (themeState.value.isDarkMode) {
                painterResource(id = R.drawable.background_dark)
            } else {
                painterResource(id = R.drawable.background)
            },
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isProfileScreen) Modifier.rotate(180f)
                    else Modifier
                )
        )
    }
}




@Composable
fun HomeScreen(themeViewModel: ThemeViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val themeState = themeViewModel.themeState.collectAsState()

    val isDarkMode = themeState.value.isDarkMode

    // Choose color based on dark/light theme
    val cardColor = if (isDarkMode) {
        Color(0xFFE3FEF7).copy(alpha = 0.05f) // Dark mode color
    } else {
        Color(0xFF424242).copy(alpha = 0.05f) // Light mode color
    }
    Column( modifier = Modifier
        .verticalScroll(scrollState) // Menambahkan scrolling
        .fillMaxSize()) {

        Box(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(400.dp)
                    .height(190.dp) // Set a fixed height for the card
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape( bottomEnd = 10.dp, topEnd = 10.dp , bottomStart = 10.dp)),
                shape = RoundedCornerShape( bottomEnd = 10.dp,topEnd = 10.dp, bottomStart = 10.dp)
                ,
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x424242) // Warna hitam dengan transparansi 50%
                ),

            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // Title of the resource
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .fillMaxHeight(), // Membuat Row mengisi lebar maksimum
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bulan),
                            contentDescription = "Home Icon",
                            modifier = Modifier.size(100.dp)
                                .weight(1f)
                                ,tint =MaterialTheme.colorScheme.tertiary
                        )
                        Column (
                            modifier = Modifier.weight(2f)
                        ){
                            Row(
                                verticalAlignment = Alignment.CenterVertically,

                            ){
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_map_pin),
                                    contentDescription = "Home Icon",
                                    modifier = Modifier.size(10.dp)



                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Sleman, Yogyakarta",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.fillMaxWidth()

                                )
                            }
                            val currentDate = remember {
                                val dateFormat = SimpleDateFormat("EEEE - dd MMMM", Locale.getDefault())
                                dateFormat.format(Date())
                            }

                            Text(
                                text = currentDate,
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Card(

                                colors = CardDefaults.cardColors(
                                    containerColor = cardColor
                                )
                            ) {
                                Column (
                                    modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                                ){
                                    Text(
                                        text = "AQI",
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()

                                    )
                                    Text(
                                        text = "Unhealty (155)",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .fillMaxWidth()

                                    )
                                    LinearProgressIndicator(
                                        progress = {
                                            0.5f
                                        },
                                        color = MaterialTheme.colorScheme.tertiary,
                                        trackColor = cardColor.copy(alpha = 0.25f),
                                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)

                                    )

                                }

                            }
                            Row(
                                modifier = Modifier.padding(top = 5.dp)
                            ) {
                                Card(
                                    modifier =Modifier
                                        .weight(1f)
                                        .padding(end = 2.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = cardColor
                                    )
                                ) {Column (
                                    modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                                ) {

                                    Text(
                                        text = "UV Index",
                                        fontSize = 14.sp,

                                        modifier = Modifier

                                    )

                                    Text(
                                        text = "Low",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier

                                    )
                                }

                                }
                                Card(
                                    modifier =Modifier
                                        .weight(1f)
                                        .padding(start = 2.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = cardColor
                                    )
                                ) {
                                    Column (
                                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                                    ) {
                                        Text(
                                            text = "Temperature",
                                            fontSize = 14.sp,
                                            modifier = Modifier


                                        )

                                        Text(
                                            text = "25℃",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier

                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        Text(
            text = "Recent Diagnose",
            fontSize = 32.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 16.dp, bottom = 0.dp)
        )
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)) {
            items(10) { index ->
                RecentItem(index)
            }
        }
        Text(
            text = "Hospital Near Me",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 16.dp )
        )
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)) {
            items(2) { index ->
                HospitalItem(index)
            }
        }
    }


}
@Composable
fun RecentItem (index: Int){
    Box(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(),



        ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .width(250.dp)
                .height(110.dp) // Set a fixed height for the card
                .shadow(elevation = 2.dp, shape = RoundedCornerShape( bottomEnd = 10.dp, topEnd = 10.dp , bottomStart = 10.dp)),
            shape = RoundedCornerShape( bottomEnd = 10.dp,topEnd = 10.dp, bottomStart = 10.dp)
            ,
            colors = CardDefaults.cardColors(
                containerColor = Color(0x424242)
            )
        ) {
            Row(modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Title of the resource
                Image(
                    painter = painterResource(id = R.drawable.recent_diagnose),
                    contentDescription = "Home Icon",
                    modifier = Modifier
                        .width(78.dp)
                        .height(89.dp)
                        .clip(RoundedCornerShape(8.dp))

                    ,
                    contentScale = ContentScale.Crop

                )

                // Description of the resource (just a placeholder for now)
                Column {
                    Text(
                        text = "Chikenpox",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "The wound has started to dry out and has a significant healing rate.",
                        fontSize = 10.sp,
                        maxLines = 3,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        lineHeight = 10.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ){
                        Text(
                            text = "19.19",
                            fontSize = 10.sp,

                            )
                        Text(
                            text = "3 DAYS AGO",
                            fontSize = 10.sp,

                            )
                    }

                }
            }
        }
    }


}
@Composable
fun HospitalItem(index: Int) {
    Box(modifier = Modifier
        .padding(start = 16.dp)
        .fillMaxWidth()
      ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()

                .width(200.dp)
                .height(300.dp) // Set a fixed height for the card
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )

        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
            ) {
                Box {  // Added Box to overlay bookmark icon on image
                    Image(
                        painter = painterResource(id = R.drawable.rs),
                        contentDescription = "Home Icon",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(bottomEnd = 20.dp)),
                        contentScale = ContentScale.Crop

                    )

                    // Bookmark Icon
                    IconButton(
                        onClick = { /* Add your bookmark action here */ },
                        modifier = Modifier
                            .align(Alignment.TopEnd)

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.bookmarked),
                            contentDescription = "Bookmark",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "RSA UGM",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "OPEN 24 HOURS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                        )
                        Text(
                            text = "2.3KM",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO )
@Composable
fun HomeScreenPreview() {
    DermatologistCareTheme() {
        HomeScreen()
    }
}

@Preview(name = "Phone Landscape", device = "spec:width=891dp,height=411dp")
@Composable
fun PhoneLandscapePreview() {
    MyApp()
}

