package com.depi.moviex.ui.theme.screens.cast_member

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depi.moviex.cast_member.domain.models.CastMember
import com.depi.moviex.data.local.extensions.toMovie
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.ui.theme.components.BackButton
import com.depi.moviex.ui.theme.components.ErrorText
import com.depi.moviex.ui.theme.components.ExpandableText
import com.depi.moviex.ui.theme.components.LoadingIndicator
import com.depi.moviex.ui.theme.components.SectionTitle
import com.depi.moviex.ui.theme.screens.home.components.MovieCoverImage
import com.depi.moviex.ui.theme.screens.favorites.FavoriteViewModel
import com.depi.moviex.utils.K
import kotlinx.coroutines.launch

@Composable
fun CastMemberScreen(
    modifier: Modifier = Modifier,
    castMemberViewModel: CastMemberViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onMovieClick: (Int, String) -> Unit = { _, _ -> }
) {
    val state by castMemberViewModel.castMemberState.collectAsStateWithLifecycle()

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
            state.castMember != null -> {
                CastMemberContent(
                    castMember = state.castMember!!,
                    onBackClick = onBackClick,
                    onMovieClick = onMovieClick,
                    favoriteViewModel = favoriteViewModel
                )
            }
        }
    }
}

@Composable
private fun CastMemberContent(
    castMember: CastMember,
    onBackClick: () -> Unit,
    onMovieClick: (Int, String) -> Unit = { _, _ -> },
    favoriteViewModel: FavoriteViewModel
) {
    var isBiographyExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(castMember.profilePath?.let { "${K.BASE_IMAGE_URL}$it" })
                    .crossfade(true)
                    .build(),
                contentDescription = castMember.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.3f),
                                androidx.compose.ui.graphics.Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )

            BackButton(onClick = onBackClick)

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = castMember.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = castMember.knownForDepartment,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryRed
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoItem(
                    label = "Birthday",
                    value = castMember.birthday
                )
                InfoItem(
                    label = "Place of Birth",
                    value = castMember.placeOfBirth
                )
                if (castMember.deathday != null) {
                    InfoItem(
                        label = "Death day",
                        value = castMember.deathday!!
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle(text = "Biography", large = false, bottomSpacer = false)
            Spacer(modifier = Modifier.height(8.dp))
            ExpandableText(
                text = castMember.biography,
                emptyMessage = "No biography available."
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(text = "Known For", large = false, bottomSpacer = false)
            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(castMember.knownFor) { knownFor ->
                val movie = knownFor.toMovie()
                val isInFavorite by favoriteViewModel.isInFavorite(movie.id)
                    .collectAsStateWithLifecycle(initialValue = false)
                val scope = rememberCoroutineScope()
                MovieCoverImage(
                    movie = movie,
                    onMovieClick = onMovieClick,
                    isInWatchlist = isInFavorite,
                    onHeartClick = {
                        scope.launch {
                            favoriteViewModel.addToFavorite(movie)
                        }
                    },
                    onRemoveFromFavorite = { movieToRemove ->
                        scope.launch {
                            favoriteViewModel.removeFromFavorite(movieToRemove)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Text(
            text = value.ifEmpty { "Unknown" },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}
