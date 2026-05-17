package com.depi.moviex.ui.theme.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    large: Boolean = true,
    bottomSpacer: Boolean = true
) {
    Text(
        text = text,
        style = if (large) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
    if (bottomSpacer) {
        Spacer(modifier = Modifier.height(12.dp))
    }
}
