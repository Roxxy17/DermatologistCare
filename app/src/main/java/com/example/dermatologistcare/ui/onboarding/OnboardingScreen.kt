package com.example.dermatologistcare.ui.onboarding



import CustomOnboardingUI
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val isDarkMode = isSystemInDarkTheme()

    DermatologistCareTheme(darkTheme = isDarkMode)   {
        val pages = listOf(
            OnboardingModel.CustomPage
            ,OnboardingModel.FirstPage, OnboardingModel.SecondPage, OnboardingModel.ThirdPage
        )

        val pagerState = rememberPagerState(initialPage = 0) {
            pages.size
        }
        val buttonState = remember {
            derivedStateOf {
                when (pagerState.currentPage) {
                    1 -> listOf("", "NEXT")
                    2 -> listOf("PREVIOUS", "NEXT")
                    3 -> listOf("PREVIOUS", "NEXT")
                    else -> listOf("", "")
                }
            }
        }

        val scope = rememberCoroutineScope()

        Scaffold(topBar = {
            if (pagerState.currentPage > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ButtonUi(
                        text = "SKIP",

                        textColor = MaterialTheme.colorScheme.primary
                    ) {
                        onFinished()
                    }
                }
            }
        },
            bottomBar = {
                if (pagerState.currentPage > 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (buttonState.value[0].isNotEmpty()) {
                                ButtonUi(
                                    text = buttonState.value[0],
                                    backgroundColor = Color.Transparent,
                                    textColor = MaterialTheme.colorScheme.onSurface,

                                ) {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                }
                            }
                        }
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            if (pagerState.currentPage > 0) {
                                IndicatorUI(pageSize = pages.size -1, currentPage = pagerState.currentPage -1)
                            }
                        }
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            ButtonUi(
                                text = buttonState.value[1],
                                backgroundColor = Color.Transparent,
                                textColor = MaterialTheme.colorScheme.onSurface
                            ) {
                                scope.launch {
                                    if (pagerState.currentPage < pages.size - 1) {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    } else {
                                        onFinished()
                                    }
                                }
                            }
                        }
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.primary,
            content = {
                Column(Modifier.padding(it)) {
                    HorizontalPager(state = pagerState) { index ->
                        val onboardingModel = pages[index]
                        when (onboardingModel) {
                            is OnboardingModel.FirstPage,
                            is OnboardingModel.SecondPage,
                            is OnboardingModel.ThirdPage -> OnboardingGraphUI(onboardingModel)
                            is OnboardingModel.CustomPage -> CustomOnboardingUI(onboardingModel)
                        }
                    }
                }
            }
        )


    }

}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen {

    }
}
@Preview(
    name = "Big Screen Preview",
    showBackground = true,
    widthDp = 1024, // Adjust for your desired width
    heightDp = 768  // Adjust for your desired height
)
@Composable
fun OnboardingScreenBigPreview() {
    OnboardingScreen {

    }
}

