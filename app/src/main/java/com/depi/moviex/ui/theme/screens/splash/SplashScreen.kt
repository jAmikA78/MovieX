package com.depi.moviex.ui.theme.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.*
import com.depi.moviex.R
import kotlinx.coroutines.delay
import androidx.compose.ui.res.stringResource

@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.intro_animation_moviex)
    )

    val progress by animateLottieCompositionAsState(composition) // بيراقب القيديو لغايه ما يخلص

    if (progress == 1f) {
        LaunchedEffect(Unit) {
            delay(4000)
            onTimeout()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF13131D)),
        contentAlignment = Alignment.Center
    ) {

        if (progress < 1f) {
            LottieAnimation(
                composition = composition,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
        else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.moviex_logo),
                    contentDescription = stringResource(R.string.splash_logo_description),
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 30.dp)
                )

                Text(
                    text = stringResource(R.string.app_name),
                    color = Color(0xFFE54E3C),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = stringResource(R.string.splash_tagline),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(horizontal = 40.dp)
                )
            }
        }
    }
}