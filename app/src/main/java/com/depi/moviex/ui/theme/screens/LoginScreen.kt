package com.depi.moviex.ui.theme.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depi.moviex.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGuestLogin: () -> Unit,
    onSignUpClick: () -> Unit
)
{
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                text = "Login",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // --Username--
            MovieXTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                placeholder = "Type your username",
                icon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.height(20.dp))

            // --Password--
            MovieXTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Type your password",
                icon = Icons.Default.Lock,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(30.dp))

            //  زرار LOGIN الأساسي (بالـ Gradient)
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(loginButtonGradient, shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "LOGIN",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Or Sign Up Using",
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedButton(
                onClick = onGuestLogin,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFE54E3C), Color(0xFF8A30B1).copy(alpha = 0.5f))
                    )
                )
            ) {
                Text(
                    text = "CONTINUE AS GUEST",
                    color = Color(0xFFE54E3C), // لون مميز للضيف
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 8. Sign Up رابط أسفل الشاشة
        TextButton(
            onClick = onSignUpClick,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        ) {
            Text(
                text = "SIGN UP",
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