package com.depi.moviex

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.depi.moviex.common.MediaType
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.CompositionLocalProvider
import androidx.annotation.StringRes
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.depi.moviex.auth.domain.repository.AuthRepository
import com.depi.moviex.ui.theme.MovieXTheme
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.ui.theme.screens.auth.LoginScreen
import com.depi.moviex.ui.theme.screens.auth.SignUpScreen
import com.depi.moviex.ui.theme.screens.cast.CastScreen
import com.depi.moviex.ui.theme.screens.cast_member.CastMemberScreen
import com.depi.moviex.ui.theme.screens.home.HomeScreen
import com.depi.moviex.ui.theme.screens.home.SearchScreen
import com.depi.moviex.ui.theme.screens.home.components.SeeAllScreen
import com.depi.moviex.ui.theme.screens.moviedetail.MovieDetailScreen
import com.depi.moviex.ui.theme.screens.onboarding.OnboardingScreen
import com.depi.moviex.ui.theme.screens.profile.ProfileScreen
import com.depi.moviex.ui.theme.screens.splash.SplashScreen
import com.depi.moviex.ui.theme.screens.favorites.FavoriteScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.appcompat.app.AppCompatActivity
import com.depi.moviex.ui.theme.screens.settings.DevelopersScreen
import com.depi.moviex.ui.theme.screens.settings.SupportScreen

val LocalIsGuest = staticCompositionLocalOf { false }
val LocalOnLoginClick = staticCompositionLocalOf<() -> Unit> { {} }


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(true) }

            MovieXTheme(darkTheme = isDarkMode) {
                AppNavigation(
                    isDarkMode = isDarkMode,
                    onThemeChange = { isDarkMode = it },
                    authRepository = authRepository
                )
    }
    }
}
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, @StringRes val labelResId: Int) {
    object Home : BottomNavItem("home", Icons.Default.Home, R.string.home_tab)
    object Favorites : BottomNavItem("favorites", Icons.AutoMirrored.Filled.List, R.string.favorites_title)
    object Profile : BottomNavItem("profile", Icons.Default.Person, R.string.profile)
}

val bottomNavItems = listOf(BottomNavItem.Home, BottomNavItem.Favorites, BottomNavItem.Profile)

@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit,
    authRepository: AuthRepository
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf("home", "favorites", "profile")

    val startDestination = remember {
        if (authRepository.isLoggedIn() || authRepository.isGuest()) "home" else "splash"
    }

    val context = LocalContext.current
    val isGuest = authRepository.isGuest()
    val onLoginClick = {
        navController.navigate("login") {
            popUpTo("home") { inclusive = true }
        }
    }

    BackHandler(enabled = showBottomBar) {
        (context as? ComponentActivity)?.finish()
    }

    CompositionLocalProvider(
        LocalIsGuest provides isGuest,
        LocalOnLoginClick provides onLoginClick
    ) {
        Scaffold(
            bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo("home") {
                                        saveState = true
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = stringResource(item.labelResId)) },
                            label = { Text(stringResource(item.labelResId)) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryRed,
                                selectedTextColor = PrimaryRed,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = Color.Transparent
                            )
                        )
        }
    }
} }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
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
                    onMovieClick = { movieId, mediaType ->
                        navController.navigate("movie_detail/$mediaType/$movieId")
                    },
                    onSettingsClick = {
                        navController.navigate("settings")
                    },
                    onSearchClick = {
                        navController.navigate("search_screen")
                    },
                    onSeeAllClick = { category ->
                        navController.navigate("all_movies/$category")
                    }
                )
            }

            composable("favorites") {
                FavoriteScreen(
                    onMovieClick = { movieId, mediaType ->
                        navController.navigate("movie_detail/$mediaType/$movieId")
                    }
                )
            }

            composable("profile") {
                ProfileScreen(
                    isDarkMode = isDarkMode,
                    onThemeChange = onThemeChange,
                    username = authRepository.getRegisteredUsername(),
                    isGuest = authRepository.isGuest(),
                    navController = navController,
                    onSignOut = {
                        authRepository.logout()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onLoginClick = {
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }

            composable("settings") {
                com.depi.moviex.ui.theme.screens.settings.SettingsScreen(
                    isDarkMode = isDarkMode,
                    onThemeChange = onThemeChange,
                    onSignOut = {
                        authRepository.logout()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onBack = { navController.popBackStack() },
                    onSupportClick = { navController.navigate("support") },
                    onDevelopersClick = { navController.navigate("developers") }
                )
            }
            composable("support") {
                SupportScreen(onBack = { navController.popBackStack() })
            }
            composable("developers") {
                DevelopersScreen(onBack = { navController.popBackStack() })
            }

            composable(
                route = "movie_detail/{mediaType}/{movieId}",
                arguments = listOf(
                    navArgument("mediaType") { type = NavType.StringType },
                    navArgument("movieId") { type = NavType.IntType }
                )
            ) {
                MovieDetailScreen(
                    onBackClick = { navController.popBackStack() },
                    onCastClick = { movieId, movieTitle, mediaType ->
                        navController.navigate("cast/${mediaType.value}/$movieId?movieTitle=$movieTitle")
                    },
                    onCastMemberClick = { personId ->
                        navController.navigate("cast_member/$personId")
                    }
                )
            }

            composable(
                route = "cast/{mediaType}/{movieId}?movieTitle={movieTitle}",
                arguments = listOf(
                    navArgument("mediaType") { type = NavType.StringType },
                    navArgument("movieId") { type = NavType.IntType },
                    navArgument("movieTitle") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = ""
                    }
                )
            ) { backStackEntry ->
                val mediaType = MediaType.fromValue(backStackEntry.arguments?.getString("mediaType") ?: "movie")
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                val movieTitle = backStackEntry.arguments?.getString("movieTitle") ?: ""
                CastScreen(
                    mediaType = mediaType,
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
                    onBackClick = { navController.popBackStack() },
                    onMovieClick = { movieId, mediaType ->
                        navController.navigate("movie_detail/$mediaType/$movieId")
                    }
                )
            }

            composable("search_screen") {
                SearchScreen(
                    onMovieClick = { movieId, mediaType ->
                        navController.navigate("movie_detail/$mediaType/$movieId")
                    }
                )
            }

            composable(
                route = "all_movies/{categoryTitle}",
                arguments = listOf(
                    navArgument("categoryTitle") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val categoryTitle = backStackEntry.arguments?.getString("categoryTitle") ?: ""
                SeeAllScreen(
                    categoryTitle = categoryTitle,
                    onBackClick = { navController.popBackStack() },
                    onMovieClick = { movieId, mediaType ->
                        navController.navigate("movie_detail/$mediaType/$movieId")
                    }
                )
            }
        }
    }
    }
}
