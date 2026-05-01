package com.depi.moviex.ui.theme.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.ui.theme.screens.home.components.CategoryRows
import com.depi.moviex.ui.theme.screens.home.components.ErrorSection
import com.depi.moviex.ui.theme.screens.home.components.FeaturedBanner
import com.depi.moviex.ui.theme.screens.home.components.HeaderSection
import com.depi.moviex.ui.theme.screens.home.components.SearchBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (id: Int) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSeeAllClick: (String) -> Unit = {}
) {
    val state by homeViewModel.homeState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            HeaderSection(message = state.message)
        }

        SearchBar(onClick = onSearchClick)

        FeaturedBanner(
            featuredMovies = state.trendingMovies.take(20),
            onMovieClick = onMovieClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryRed)
                }
            }
            state.error != null -> {
                ErrorSection(error = state.error!!)
            }
            else -> {
                CategoryRows(
                    categories = listOf(
                        CategoryItem("Trending now", state.discoverMovies),
                        CategoryItem("Most Watched", state.trendingMovies),
                        CategoryItem("TV Shows", state.tvShows),
                        CategoryItem("Action", state.actionMovies),
                        CategoryItem("Drama", state.dramaMovies),
                        CategoryItem("Comedy", state.comedyMovies)
                    ),
                    onMovieClick = onMovieClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
        }
    }
}

