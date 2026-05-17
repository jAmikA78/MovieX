package com.depi.moviex.ui.theme.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.depi.moviex.auth.domain.models.LoginState
import com.depi.moviex.ui.theme.screens.auth.viewModel.LoginViewModel
import androidx.compose.ui.res.stringResource
import com.depi.moviex.R

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        onLoginSuccess = {},
        onGuestLogin = {},
        onSignUpClick = {},
        loginViewModel = hiltViewModel()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGuestLogin: () -> Unit,
    onSignUpClick: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> onLoginSuccess()
            is LoginState.Error -> { /* Error handled in UI */ }
            else -> { /* Handle other states */ }
        }
    }

    val loginButtonGradient = Brush.linearGradient(
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
                text = stringResource(R.string.login_title),
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // --Username--
            MovieXTextField(
                value = username,
                onValueChange = { username = it },
                label = stringResource(R.string.label_name),
                placeholder = stringResource(R.string.hint_username),
                icon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.height(20.dp))

            // --Password--
            MovieXTextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.label_password),
                placeholder = stringResource(R.string.hint_password),
                icon = Icons.Default.Lock,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error message
            if (loginState is LoginState.Error) {
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Loading indicator
            if (loginState is LoginState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            //  زرار LOGIN الأساسي (بالـ Gradient)
            Button(
                onClick = { loginViewModel.login(username, password) },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                enabled = loginState !is LoginState.Loading
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(loginButtonGradient, shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.btn_login_caps),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.or_signup_using),
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedButton(
                onClick = {
                    loginViewModel.loginAsGuest()
                    onGuestLogin()
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(2.dp, Color(0xFFE54E3C))
            ) {
                Text(
                    text = stringResource(R.string.continue_as_guest),
                    color = Color(0xFFE54E3C),//guest color
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        TextButton(
            onClick = onSignUpClick,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.btn_signup_caps),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SocialIcon(iconResId: Int) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(Color.White, shape = CircleShape) // عشان تظهر واضحة
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieXTextField(
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
            else VisualTransformation.None
        )
    }
}