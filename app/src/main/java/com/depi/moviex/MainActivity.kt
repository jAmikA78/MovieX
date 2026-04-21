package com.depi.moviex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.depi.moviex.ui.theme.MovieXTheme
import com.depi.moviex.ui.theme.screens.auth.LoginScreen
import com.depi.moviex.ui.theme.screens.auth.SignUpScreen
import com.depi.moviex.ui.theme.screens.home.HomeScreen
import com.depi.moviex.ui.theme.screens.moviedetail.MovieDetailScreen
import com.depi.moviex.ui.theme.screens.onboarding.OnboardingScreen
import com.depi.moviex.ui.theme.screens.settings.SettingsScreen
import com.depi.moviex.ui.theme.screens.splash.SplashScreen
import com.depi.moviex.ui.theme.screens.cast.CastScreen
import com.depi.moviex.ui.theme.screens.cast_member.CastMemberScreen
import com.depi.moviex.ui.theme.screens.settings.DevelopersScreen
import com.depi.moviex.ui.theme.screens.settings.SupportScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(true) }

            MovieXTheme(darkTheme = isDarkMode) {
                AppNavigation(
                    isDarkMode = isDarkMode,
                    onThemeChange = { isDarkMode = it }
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("onboarding") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }

        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGuestLogin = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate("login")
                },
                onGuestLogin = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
            )
        }

        composable("home") {
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate("movie_detail/$movieId")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                },
                onSearchClick = {
                    navController.navigate("search_screen")
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                isDarkMode = isDarkMode,
                onThemeChange = onThemeChange,
                onSignOut = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                },
                onSupportClick = {
                    navController.navigate("support")
                },
                onDevelopersClick = {
                    navController.navigate("developers")
                }
            )
        }

        composable("support") {
            SupportScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("developers") {
            DevelopersScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = "movie_detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            MovieDetailScreen(
                onBackClick = { navController.popBackStack() },
                onCastClick = { movieId, movieTitle ->
                    navController.navigate("cast/$movieId?movieTitle=$movieTitle")
                },
                onCastMemberClick = { personId ->
                    navController.navigate("cast_member/$personId")
                }
            )
        }

        composable(
            route = "cast/{movieId}?movieTitle={movieTitle}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType },
                navArgument("movieTitle") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val movieTitle = backStackEntry.arguments?.getString("movieTitle") ?: ""
            CastScreen(
                movieId = movieId,
                movieTitle = movieTitle,
                onBackClick = { navController.popBackStack() },
                onCastMemberClick = { personId ->
                    navController.navigate("cast_member/$personId")
                }
            )
        }

        composable(
            route = "cast_member/{personId}",
            arguments = listOf(
                navArgument("personId") { type = NavType.IntType }
            )
        ) {
            CastMemberScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("search_screen") {
            com.depi.moviex.ui.theme.screens.home.SearchScreen(
                onMovieClick = { movieId ->
                    navController.navigate("movie_detail/$movieId")
                }
            )
        }
    }
}