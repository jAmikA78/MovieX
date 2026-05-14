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
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.models.MovieCategory
import com.depi.moviex.ui.theme.screens.home.components.CategoryRows
import com.depi.moviex.ui.theme.screens.home.components.ErrorSection
import com.depi.moviex.ui.theme.screens.home.components.FeaturedBanner
import com.depi.moviex.ui.theme.screens.home.components.HeaderSection
import com.depi.moviex.ui.theme.screens.home.components.MovieCoverImage
import com.depi.moviex.ui.theme.screens.home.components.SearchBar
import com.depi.moviex.ui.theme.screens.favorites.FavoriteViewModel
import androidx.compose.ui.res.stringResource
import com.depi.moviex.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    onMovieClick: (id: Int, mediaType: String) -> Unit = { _, _ -> },
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
                        CategoryItem(stringResource(R.string.category_trending), MovieCategory.DISCOVER.name, state.discoverMovies),
                        CategoryItem(stringResource(R.string.category_most_watched), MovieCategory.TRENDING.name, state.trendingMovies),
                        CategoryItem(stringResource(R.string.category_tv_shows), MovieCategory.TV_SHOWS.name, state.tvShows),
                        CategoryItem(stringResource(R.string.category_action), MovieCategory.ACTION.name, state.actionMovies),
                        CategoryItem(stringResource(R.string.category_drama), MovieCategory.DRAMA.name, state.dramaMovies),
                        CategoryItem(stringResource(R.string.category_comedy), MovieCategory.COMEDY.name, state.comedyMovies),
                        CategoryItem(stringResource(R.string.category_animation), MovieCategory.ANIMATION.name, state.animationMovies),
                        CategoryItem(stringResource(R.string.category_top_rated), MovieCategory.TOP_RATED.name, state.topRatedMovies),
                        CategoryItem(stringResource(R.string.category_documentaries), MovieCategory.DOCUMENTARIES.name, state.documentariesMovies),
                        CategoryItem(stringResource(R.string.category_horror), MovieCategory.HORROR.name, state.horrorMovies),
                        CategoryItem(stringResource(R.string.category_family_kids), MovieCategory.FAMILY_KIDS.name, state.familyKidsMovies),
                        CategoryItem(stringResource(R.string.category_war), MovieCategory.WAR.name, state.warMovies),
                        CategoryItem(stringResource(R.string.category_crime), MovieCategory.CRIME.name, state.crimeMovies),
                        CategoryItem(stringResource(R.string.category_egyptian_movies), MovieCategory.EGYPTIAN_MOVIES.name, state.egyptianMovies),
                        CategoryItem(stringResource(R.string.category_egyptian_tv), MovieCategory.EGYPTIAN_TV.name, state.egyptianTv),
                        CategoryItem(stringResource(R.string.category_korean_tv), MovieCategory.KOREAN_TV.name, state.koreanTv),
                        CategoryItem(stringResource(R.string.category_upcoming), MovieCategory.UPCOMING.name, state.upcomingMovies)
                    ),
                    onMovieClick = onMovieClick,
                    onSeeAllClick = onSeeAllClick,
                    favoriteViewModel = favoriteViewModel,
                    onNotifyClick = { movie -> homeViewModel.toggleReminder(movie) },
                    reminderMovieIds = state.reminderMovieIds,
                )
            }
        }
    }
}

