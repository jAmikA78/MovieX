package com.depi.moviex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depi.moviex.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    LaunchedEffect(key1 = true) {
        delay(1500) // 1.5 ثانية
        onTimeout()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF13131D)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.moviex_logo),
                contentDescription = "MovieX Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 30.dp)
            )

            Text(
                text = "MovieX",
                color = Color(0xFFE54E3C),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Discover. Track. Never miss\na movie",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 40.dp)
            )
        }
    }
}