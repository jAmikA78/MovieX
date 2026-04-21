package com.depi.moviex.ui.theme.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.depi.moviex.ui.theme.screens.auth.viewModel.LoginViewModel
import com.depi.moviex.ui.theme.PrimaryRed

@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onSignOut: () -> Unit,
    onBack: () -> Unit,
    onSupportClick: () -> Unit,
    onDevelopersClick: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Settings",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            SettingsItem(
                icon = Icons.Default.Settings,
                title = "Dark Mode",
                subtitle = "Enable dark theme",
                trailing = {
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onThemeChange(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PrimaryRed,
                            checkedTrackColor = PrimaryRed.copy(alpha = 0.5f)
                        )
                    )
                }
            )

            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Manage notification preferences",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Default.Info,
                title = "About",
                subtitle = "App version 1.0.0",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Default.Email,
                title = "Support",
                subtitle = "Contact us for help or feedback",
                onClick = { onSupportClick() }

            )

            SettingsItem(
                icon = Icons.Default.Person,
                title = "Developers",
                subtitle = "Meet the team behind MovieX",
                onClick = { onDevelopersClick() }
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            SettingsItem(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                title = "Sign Out",
                subtitle = "Sign out of your account",
                titleColor = PrimaryRed,
                onClick = { showLogoutDialog = true }
            )
            Spacer(modifier = Modifier.height(25.dp))
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign Out") },
            text = { Text("Are you sure you want to sign out?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        loginViewModel.logout()
                        showLogoutDialog = false
                        onSignOut()
                    }
                ) {
                    Text("Sign Out", color = PrimaryRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = { },
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (titleColor == PrimaryRed) PrimaryRed else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                color = titleColor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }

        trailing?.invoke()
    }
}
