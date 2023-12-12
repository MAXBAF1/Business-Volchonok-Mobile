package com.example.volchonok.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.utils.isInternetAvailable

class SplashScreen(
    private val toNetworkErrorScreen: () -> Unit,
    private val toWelcomeScreen: () -> Unit,
    private val toCoursesScreen: () -> Unit, // TODO переход на эран курса, если уже залогинен
) {
    @Composable
    fun Create() {
        if (!isInternetAvailable(LocalContext.current)) toNetworkErrorScreen()

        var isAnimationStart by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            isAnimationStart = true
        }
        val imageHeight = animateDpAsState(targetValue = if (isAnimationStart) 500.dp else 0.dp,
            animationSpec = keyframes {
                durationMillis = 3000
            },
            finishedListener = { toWelcomeScreen() },
            label = ""
        )
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
            Box(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .weight(0.75f)
            ) {
                Image(
                    modifier = Modifier.size(220.dp, imageHeight.value),
                    painter = painterResource(id = R.drawable.suit_wolf),
                    contentDescription = "suit_wolf",
                    alignment = Alignment.TopCenter,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}