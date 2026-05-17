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
import com.depi.moviex.ui.theme.PrimaryRed
import com.depi.moviex.ui.theme.components.ConfirmDialog
import com.depi.moviex.ui.theme.components.MenuItemRow
import com.depi.moviex.ui.theme.screens.auth.viewModel.LoginViewModel
import androidx.core.os.LocaleListCompat
import androidx.compose.ui.res.stringResource
import com.depi.moviex.R
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.icons.filled.Language


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
                    text = stringResource(R.string.settings),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            MenuItemRow(
                icon = Icons.Default.Settings,
                title = stringResource(R.string.dark_mode),
                subtitle = stringResource(R.string.enable_dark_theme),
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

            MenuItemRow(
                icon = Icons.Default.Language,
                title = stringResource(R.string.arabic),
                subtitle = stringResource(R.string.arabic_subtitle),
                onClick = {
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("ar")
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            )


            MenuItemRow(
                icon = Icons.Default.Notifications,
                title = stringResource(R.string.notifications),
                subtitle = stringResource(R.string.notification_preferences),
                onClick = { }
            )

            MenuItemRow(
                icon = Icons.Default.Email,
                title = stringResource(R.string.support),
                subtitle = stringResource(R.string.contact_support),
                onClick = { onSupportClick() }

            )

            MenuItemRow(
                icon = Icons.Default.Person,
                title = stringResource(R.string.developers),
                subtitle = stringResource(R.string.meet_team),
                onClick = { onDevelopersClick() }
            )

            MenuItemRow(
                icon = Icons.Default.Info,
                title = stringResource(R.string.about),
                subtitle = stringResource(R.string.app_version),
                onClick = { }
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            MenuItemRow(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                title = stringResource(R.string.sign_out),
                subtitle = stringResource(R.string.sign_out_subtitle),
                titleColor = PrimaryRed,
                onClick = { showLogoutDialog = true }
            )
            Spacer(modifier = Modifier.height(25.dp))

        }
    }

    if (showLogoutDialog) {
        ConfirmDialog(
            title = stringResource(R.string.sign_out_confirm_title),
            text = stringResource(R.string.sign_out_confirm_text),
            confirmLabel = stringResource(R.string.sign_out_confirm_label),
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                loginViewModel.logout()
                showLogoutDialog = false
                onSignOut()
            }
        )
    }
}

