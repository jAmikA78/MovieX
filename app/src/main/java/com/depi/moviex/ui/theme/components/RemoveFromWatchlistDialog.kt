package com.depi.moviex.ui.theme.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.depi.moviex.ui.theme.PrimaryRed

@Composable
fun RemoveFromWatchlistDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Remove from Watchlist") },
        text = { Text("Are you sure you want to remove \"$title\" from your watchlist?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Remove", color = PrimaryRed)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
