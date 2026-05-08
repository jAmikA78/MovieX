package com.depi.moviex.ui.theme.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depi.moviex.ui.theme.PrimaryRed
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.depi.moviex.R

@Composable
fun SupportScreen(onBack: () -> Unit) {
    val textColor = MaterialTheme.colorScheme.onBackground
    val subTextColor = Color.Gray

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = textColor,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBack() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.support),
                    color = textColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            Text(
                text = stringResource(R.string.support_heading),
                color = PrimaryRed,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.support_body),
                color = subTextColor,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            SupportSection(
                title = stringResource(R.string.support_email_title),
                detail = stringResource(R.string.support_email),
                textColor = textColor
            )

            SupportSection(
                title = stringResource(R.string.support_response_title),
                detail = stringResource(R.string.support_response_body),
                textColor = textColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.support_contact_title),
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            val bulletPoints = listOf(
                stringResource(R.string.support_bug),
                stringResource(R.string.support_account),
                stringResource(R.string.support_suggestions),
                stringResource(R.string.support_general)
            )

            bulletPoints.forEach { point ->
                Row(
                    modifier = Modifier.padding(top = 12.dp, start = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "•", color = PrimaryRed, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = point,
                        color = subTextColor,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SupportSection(title: String, detail: String, textColor: Color) {
    val context = LocalContext.current
    val emailSubject = stringResource(R.string.support_email_subject)
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = title,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = detail,
            color = PrimaryRed,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,

            style = MaterialTheme.typography.bodyMedium.copy(
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
            ),
                    modifier = Modifier.clickable {
                if (detail.contains("@")) {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:$detail")
                        putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                    }
                    try {
                        context.startActivity(intent)
                      } catch (e: Exception) {
                    }
                }
            }
        )
    }
}