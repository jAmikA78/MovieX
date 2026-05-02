package com.depi.moviex.ui.theme.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.depi.moviex.ui.theme.screens.home.CategoryItem
import com.depi.moviex.ui.theme.screens.watchlist.WatchlistViewModel

@Composable
fun CategoryRows(
    categories: List<CategoryItem>,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (String) -> Unit,
    watchlistViewModel: WatchlistViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        categories.forEach { category ->
            if (category.movies.isNotEmpty()) {
                CategoryRow(
                    title = category.name,
                    movies = category.movies,
                    onMovieClick = onMovieClick,
                    onSeeAllClick = onSeeAllClick,
                    watchlistViewModel = watchlistViewModel
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
