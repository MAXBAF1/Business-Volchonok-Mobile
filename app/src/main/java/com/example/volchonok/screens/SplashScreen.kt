package com.example.volchonok.screens

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import com.example.volchonok.R
import com.example.volchonok.utils.isInternetAvailable
import kotlinx.coroutines.delay

class SplashScreen(
    private val toNetworkErrorScreen: () -> Unit,
    private val toWelcomeScreen: () -> Unit,
    private val toCoursesScreen: () -> Unit,
) {
    @Composable
    fun Create() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(0.25f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier
                        .rotate(-6.87f)
                        .offset(x = (-5).dp, y = (-5).dp),
                    text = stringResource(id = R.string.business_school),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(id = R.string.business_wolf),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.background
                )
            }
            Image(
                modifier = Modifier
                    .weight(0.75f)
                    .size(220.dp, 400.dp)
                    .padding(top = 50.dp),
                painter = painterResource(id = R.drawable.suit_wolf),
                contentDescription = "suit_wolf",
                alignment = Alignment.TopCenter
            )
        }

        var toNextScreen by remember { mutableStateOf(false) }
        if (isInternetAvailable(LocalContext.current)) {
            LaunchedEffect(toNextScreen) {
                delay(3000)
                toNextScreen = true
            }

            if (toNextScreen) toWelcomeScreen()
        } else toNetworkErrorScreen()
    }
}