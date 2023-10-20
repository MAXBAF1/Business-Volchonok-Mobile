package com.example.volchonok.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.volchonok.R

class WelcomeScreen(private val toLoginScreen: () -> Unit) {
    @Composable
    fun Create() {
        Column(
            Modifier
                .padding(30.dp)
                .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(top = 45.dp),
                    text = stringResource(id = R.string.welcome),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    text = stringResource(id = R.string.tap_to_start),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Image(
                modifier = Modifier.size(390.dp, 456.dp),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.party_wolf),
                contentDescription = "party_wolf"
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                onClick = { toLoginScreen() },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(id = R.string.start).uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}