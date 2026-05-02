package com.depi.moviex.ui.theme.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.ui.theme.screens.watchlist.WatchlistViewModel

@Composable
fun CategoryRow(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (String) -> Unit,
    watchlistViewModel: WatchlistViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "See All",
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryRed,
                modifier = Modifier.clickable { onSeeAllClick(title) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movies) { movie ->
                val isInWatchlist by watchlistViewModel.isInWatchlist(movie.id).collectAsStateWithLifecycle(initialValue = false)
                val scope = rememberCoroutineScope()
                MovieCoverImage(
                    movie = movie,
                    onMovieClick = onMovieClick,
                    isInWatchlist = isInWatchlist,
                    onHeartClick = {
                        scope.launch {
                            if (isInWatchlist) {
                                watchlistViewModel.removeFromWatchlist(movie)
                            } else {
                                watchlistViewModel.addToWatchlist(movie, title)
                            }
                        }
                    }
                )
            }
        }
    }
}
