package com.depi.moviex.ui.theme.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.ui.theme.screens.home.CategoryItem
import com.depi.moviex.ui.theme.screens.favorites.FavoriteViewModel

@Composable
fun CategoryRows(
    categories: List<CategoryItem>,
    onMovieClick: (Int, String) -> Unit,
    onSeeAllClick: (String) -> Unit,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onNotifyClick: ((Movie) -> Unit)? = null,
    reminderMovieIds: Set<Int> = emptySet(),
) {
    Column(modifier = modifier) {
        categories.forEach { category ->
            if (category.movies.isNotEmpty()) {
                CategoryRow(
                    title = category.name,
                    key = category.key,
                    movies = category.movies,
                    onMovieClick = onMovieClick,
                    onSeeAllClick = onSeeAllClick,
                    favoriteViewModel = favoriteViewModel,
                    onNotifyClick = onNotifyClick,
                    reminderMovieIds = reminderMovieIds,
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
