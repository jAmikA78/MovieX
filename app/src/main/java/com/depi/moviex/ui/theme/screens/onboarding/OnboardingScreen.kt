package com.depi.moviex.ui.theme.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depi.moviex.R
import com.depi.moviex.ui.theme.*
import com.depi.moviex.ui.theme.screens.onboarding.OnboardingPage
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource

@Preview
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen(onFinish = {})
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = listOf(
        OnboardingPage(R.drawable.ic_onboard_search, stringResource(R.string.onboarding_title_1), stringResource(R.string.onboarding_desc_1)),
        OnboardingPage(R.drawable.ic_onboard_calendar, stringResource(R.string.onboarding_title_2), stringResource(R.string.onboarding_desc_2)),
        OnboardingPage(R.drawable.ic_onboard_watchlist, stringResource(R.string.onboarding_title_3), stringResource(R.string.onboarding_desc_3)),
        OnboardingPage(R.drawable.ic_onboard_play, stringResource(R.string.onboarding_title_4), stringResource(R.string.onboarding_desc_4))
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = {

            OnboardingBottomBar(
                pagerState = pagerState,
                pagesSize = pages.size,
                onSkip = {

                    coroutineScope.launch { pagerState.scrollToPage(pages.size - 1) }
                },
                onNext = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < pages.size - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            onFinish()
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        //  عرض الصفحات (التقليب الأفقي)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { position ->
            OnboardingPageContent(page = pages[position])
        }
    }
}

//
@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = page.iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 60.dp)
        )

        Text(
            text = page.title,
            color = TextColorWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (page.description.isNotEmpty()) {
            Text(
                text = page.description,
                color = TextColorWhite.copy(alpha = 0.8f), // شفافية خفيفة
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingBottomBar(
    pagerState: androidx.compose.foundation.pager.PagerState,
    pagesSize: Int,
    onSkip: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // لو مش في آخر صفحة، اظهر "Skip"
        if (pagerState.currentPage < pagesSize - 1) {
            TextButton(onClick = onSkip) {
                Text(text = stringResource(R.string.btn_skip), color = OnboardingIconColor, fontSize = 16.sp)
            }
        } else {

            Spacer(modifier = Modifier.width(60.dp))
        }

        OnboardingPagerIndicator(
            pagerState = pagerState,
            pagesSize = pagesSize
        )

        //  الزرار اليمين (Next أو Start)
        val isLastPage = pagerState.currentPage == pagesSize - 1
        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLastPage) PrimaryRed else OnboardingIconColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = if (isLastPage) 30.dp else 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = if (isLastPage) stringResource(R.string.btn_get_started) else stringResource(R.string.btn_next),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingPagerIndicator(
    pagerState: androidx.compose.foundation.pager.PagerState,
    pagesSize: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagesSize) { index ->
            val isSelected = pagerState.currentPage == index
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(if (isSelected) 24.dp else 8.dp)
                    .background(
                        color = if (isSelected) OnboardingIconColor else PagerIndicatorInactive,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}