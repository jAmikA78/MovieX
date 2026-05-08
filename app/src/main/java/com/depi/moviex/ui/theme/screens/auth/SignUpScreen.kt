package com.depi.moviex.ui.theme.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.depi.moviex.ui.theme.screens.auth.viewModel.SignUpState
import com.depi.moviex.ui.theme.screens.auth.viewModel.SignUpViewModel
import androidx.compose.ui.res.stringResource
import com.depi.moviex.R

@Preview
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen(
        onSignUpSuccess = {},
        onLoginClick = {},
        onGuestLogin = {},
        signUpViewModel = hiltViewModel()
    )
}

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    onGuestLogin: () -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val signUpState by signUpViewModel.signUpState.collectAsStateWithLifecycle()

    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignUpState.MessageShown -> {
                onSignUpSuccess()
                signUpViewModel.resetState()
            }
            else -> { }
        }
    }

    val signUpButtonGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFE54E3C),
            Color(0xFF8A30B1)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF13131D))
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().align(Alignment.Center)
        ) {
            Text(
                text = stringResource(R.string.btn_signup),
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            AuthTextField(
                value = username,
                onValueChange = { username = it },
                label = stringResource(R.string.label_name),
                placeholder = stringResource(R.string.hint_choose_username),
                icon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.label_email),
                placeholder = stringResource(R.string.hint_email),
                icon = Icons.Default.Email
            )
            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.label_password),
                placeholder = stringResource(R.string.hint_create_password),
                icon = Icons.Default.Lock,
                isPassword = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = stringResource(R.string.label_confirm_password),
                placeholder = stringResource(R.string.hint_confirm_password),
                icon = Icons.Default.Lock,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TMDB does not support signup via API
            Text(
                text = stringResource(R.string.signup_api_disclaimer),
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = { signUpViewModel.onSignUpClick() },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(signUpButtonGradient, shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.btn_signup_caps),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        TextButton(
            onClick = {
                signUpViewModel.onGuestLogin()
                onGuestLogin()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.already_have_account_login),
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = placeholder, color = Color.White.copy(alpha = 0.5f))
            },
            leadingIcon = {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White.copy(alpha = 0.5f))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF232331),
                unfocusedContainerColor = Color(0xFF232331),
                focusedBorderColor = Color(0xFFE54E3C),
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color(0xFFE54E3C)
            ),
            shape = RoundedCornerShape(10.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation()
            else VisualTransformation.None,
            singleLine = true
        )
    }
}
