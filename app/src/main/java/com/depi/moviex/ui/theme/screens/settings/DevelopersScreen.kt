package com.depi.moviex.ui.theme.screens.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depi.moviex.ui.theme.PrimaryRed
import androidx.compose.ui.res.stringResource
import com.depi.moviex.R
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale


data class Developer(
    val name: String,
    val role: String,
    val githubUrl: String,
    val linkedinUrl: String,
    val imageRes: Int
)

fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {

    }
}

@Composable
fun DevelopersScreen(onBack: () -> Unit) {
    val developers = listOf(
        Developer(stringResource(R.string.dev_ahmed_name),
            stringResource(R.string.dev_ahmed_role),
            "https://github.com/ahmedalktatny5-creator/Ahmed-Alktatney",
            "https://www.linkedin.com/in/ahmed-alktatney-0a5983357/",
            com.depi.moviex.R.drawable.dev_ahmed),

        Developer(stringResource(R.string.dev_ahmed2_name),
            stringResource(R.string.dev_ahmed2_role),
            "github.com/jAmikA78",
            "https://www.linkedin.com/in/-ahmed-ibrahim-abd-elghany/",
            com.depi.moviex.R.drawable.dev_jamika)
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 40.dp, start = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = stringResource(R.string.developers_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(developers) { dev ->
                DeveloperCard(dev)
            }
        }
    }
}

@Composable
fun DeveloperCard(developer: Developer) {
    val context = androidx.compose.ui.platform.LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2E))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, PrimaryRed, CircleShape)
            ) { Image(
                painter = painterResource(id = developer.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = developer.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = developer.role, color = PrimaryRed, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(8.dp))


                Row {

                    Icon(
                        painter = painterResource(id = com.depi.moviex.R.drawable.ic_github),
                        contentDescription = stringResource(R.string.github),
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { openUrl(context, developer.githubUrl) }
                    )

                    Spacer(modifier = Modifier.width(16.dp))


                    Icon(
                        painter = painterResource(id = com.depi.moviex.R.drawable.ic_linkedin),
                        contentDescription = stringResource(R.string.linkedin),
                        tint = Color(0xFF0A66C2),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { openUrl(context, developer.linkedinUrl) }
                    )
                }
            }
        }
    }
}