package com.depi.moviex.ui.theme.screens.profile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.ui.theme.components.ConfirmDialog
import com.depi.moviex.ui.theme.components.MenuItemRow

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = true,
    onThemeChange: (Boolean) -> Unit = {},
    onSignOut: () -> Unit = {},
    username: String? = null,
    isGuest: Boolean = false,
    onLoginClick: () -> Unit = {},
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Profile",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 50.dp, bottom = 24.dp),
            )

            Box(
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(PrimaryRed)
                        .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = (username?.firstOrNull()?.uppercase() ?: "?"),
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isGuest) "Guest" else (username ?: "MovieX User"),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            if (!isGuest && username != null) {
                Text(
                    text = "@$username",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            MenuItemRow(
                icon = Icons.Default.Settings,
                title = "Dark Mode",
                subtitle = "Enable dark theme",
                trailing = {
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onThemeChange(it) },
                        colors =
                            SwitchDefaults.colors(
                                checkedThumbColor = PrimaryRed,
                                checkedTrackColor = PrimaryRed.copy(alpha = 0.5f),
                            ),
                    )
                },
            )

            MenuItemRow(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Manage notification preferences",
                onClick = { },
            )

            MenuItemRow(
                icon = Icons.Default.Email,
                title = "Support",
                subtitle = "Contact us for help or feedback",
                onClick = { },
            )

            MenuItemRow(
                icon = Icons.Default.Person,
                title = "Developers",
                subtitle = "Meet the team behind MovieX",
                onClick = { },
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isGuest) {
                MenuItemRow(
                    icon = Icons.Default.Person,
                    title = "Login",
                    subtitle = "Sign in to your account",
                    titleColor = PrimaryRed,
                    onClick = onLoginClick,
                )
            } else {
                MenuItemRow(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = "Sign Out",
                    subtitle = "Sign out of your account",
                    titleColor = PrimaryRed,
                    onClick = { showLogoutDialog = true },
                )
            }

            MenuItemRow(
                icon = Icons.Default.Info,
                title = "About",
                subtitle = "App version 1.0.0",
                onClick = { },
            )

            Spacer(modifier = Modifier.height(25.dp))
        }
    }

    if (showLogoutDialog) {
        ConfirmDialog(
            title = "Sign Out",
            text = "Are you sure you want to sign out?",
            confirmLabel = "Sign Out",
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                onSignOut()
            },
        )
    }
}
