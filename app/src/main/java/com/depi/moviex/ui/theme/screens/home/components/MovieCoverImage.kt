package com.depi.moviex.ui.theme.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.utils.K
import androidx.compose.ui.res.stringResource
import com.depi.moviex.R

@Composable
fun MovieCoverImage(
    modifier: Modifier = Modifier,
    movie: Movie,
    onMovieClick: (Int, String) -> Unit,
    isInWatchlist: Boolean = false,
    onHeartClick: () -> Unit = {},
    onRemoveFromWatchlist: (Movie) -> Unit = {}
) {
    var showRemoveDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(width = 150.dp, height = 250.dp)
            .padding(8.dp)
            .clickable { onMovieClick(movie.id, movie.mediaType) }
    ) {
        val imgRequest = ImageRequest.Builder(LocalContext.current)
            .data("${K.BASE_IMAGE_URL}${movie.posterPath}")
            .crossfade(true)
            .build()

        AsyncImage(
            model = imgRequest,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(8.dp))
                .shadow(elevation = 4.dp),
            contentScale = ContentScale.Crop
        )

        Card(
            shape = CircleShape,
            colors = CardDefaults.cardColors(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .clickable {
                    if (isInWatchlist) {
                        showRemoveDialog = true
                    } else {
                        onHeartClick()
                    }
                }
        ) {
            Icon(
                imageVector = if (isInWatchlist) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = stringResource(R.string.toggle_watchlist),
                tint = if (isInWatchlist) PrimaryRed else Color.White,
                modifier = Modifier.padding(4.dp)
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color.Black.copy(.8f),
            contentColor = Color.White,
            shape = RoundedCornerShape(
                bottomEnd = 30.dp,
                bottomStart = 30.dp,
            )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
//                Nedding to be handeld in UI
//                Text(text = movie.title, maxLines = 1)
            }
        }
    }

    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text(stringResource(R.string.remove_from_watchlist)) },
            text = { Text(stringResource(R.string.remove_confirm_text, movie.title)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemoveFromWatchlist(movie)
                        showRemoveDialog = false
                    }
                ) {
                    Text(stringResource(R.string.remove), color = PrimaryRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
