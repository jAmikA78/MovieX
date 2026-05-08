package com.depi.moviex.ui.theme.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.depi.moviex.ui.theme.PrimaryRed
import androidx.compose.ui.res.stringResource
import com.depi.moviex.R

@Composable
fun LoginRequiredDialog(
    onDismiss: () -> Unit,
    onLoginClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.login_required)) },
        text = { Text(stringResource(R.string.login_to_add_favorites)) },
        confirmButton = {
            TextButton(onClick = onLoginClick) {
                Text(stringResource(R.string.btn_login_caps), color = PrimaryRed)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
