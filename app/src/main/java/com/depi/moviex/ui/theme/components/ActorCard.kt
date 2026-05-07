package com.depi.moviex.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.utils.K

@Composable
fun ActorCard(
    name: String,
    profilePath: String?,
    role: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    compact: Boolean = true
) {
    val imageSize = if (compact) 70.dp else 80.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .then(if (compact) Modifier.width(80.dp) else Modifier.fillMaxWidth())
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profilePath?.let { "${K.BASE_IMAGE_URL}$it" })
                .crossfade(true)
                .build(),
            contentDescription = name,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(if (compact) 4.dp else 8.dp))
        Text(
            text = name,
            style = if (compact) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelMedium,
            fontWeight = if (compact) FontWeight.Normal else FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = if (compact) 2 else 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        if (!compact) {
            Spacer(modifier = Modifier.height(2.dp))
        }
        Text(
            text = role,
            style = MaterialTheme.typography.labelSmall,
            color = PrimaryRed,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}
