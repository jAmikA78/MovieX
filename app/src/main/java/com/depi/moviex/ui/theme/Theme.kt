package com.depi.moviex.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// Program colors
val BackgroundDark = Color(0xFF13131D)
val BackgroundLight = Color(0xFFF5F5F7)
val SurfaceDark = Color(0xFF1E1E2E)
val SurfaceLight = Color(0xFFFFFFFF)

val PrimaryRed = Color(0xFFE54E3C)
val OnboardingIconColor = Color(0xFFF78F78)
val TextColorWhite = Color(0xFFFFFFFF)
val TextColorBlack = Color(0xFF13131D)
val PagerIndicatorInactive = Color(0xFF53535F)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryRed,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2B2B3D),
    onSurfaceVariant = Color.Gray
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryRed,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = TextColorBlack,
    onSurface = TextColorBlack,
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color.DarkGray
)

@Composable
fun MovieXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}