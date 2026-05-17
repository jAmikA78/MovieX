package com.depi.moviex.ui.theme.screens.cast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.depi.moviex.common.MediaType
import com.depi.moviex.movie_detail.domain.models.Cast
import com.depi.moviex.ui.theme.components.ActorCard
import com.depi.moviex.ui.theme.components.BackButton
import com.depi.moviex.ui.theme.components.ErrorText
import com.depi.moviex.ui.theme.components.LoadingIndicator
import androidx.compose.ui.res.stringResource
import com.depi.moviex.R

@Composable
fun CastScreen(
    modifier: Modifier = Modifier,
    castViewModel: CastViewModel = hiltViewModel(),
    mediaType: MediaType = MediaType.MOVIE,
    movieId: Int = 0,
    movieTitle: String = "",
    onBackClick: () -> Unit = {},
    onCastMemberClick: (Int) -> Unit = {}
) {
    val state by castViewModel.castState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.isLoading -> {
                LoadingIndicator()
            }
            state.error != null -> {
                ErrorText(message = state.error)
            }
            else -> {
                CastContent(
                    cast = state.cast,
                    movieTitle = movieTitle,
                    onBackClick = onBackClick,
                    onCastMemberClick = onCastMemberClick
                )
            }
        }
    }
}

@Composable
private fun CastContent(
    cast: List<Cast>,
    movieTitle: String,
    onBackClick: () -> Unit,
    onCastMemberClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            BackButton(
                onClick = onBackClick,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                iconTint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.cast_screen_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = movieTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        if (cast.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_cast_info),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(cast) { castMember ->
                    ActorCard(
                        name = castMember.name,
                        profilePath = castMember.profilePath,
                        role = castMember.character,
                        onClick = { onCastMemberClick(castMember.id) },
                        compact = false
                    )
                }
            }
        }
    }
}

