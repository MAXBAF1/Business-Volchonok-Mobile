package com.example.volchonok.screens

import android.util.Log
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
import androidx.compose.runtime.State
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.RemoteInfoStorage.*
import com.example.volchonok.data.CourseData
import com.example.volchonok.enums.CourseDataAccessLevel
import com.example.volchonok.services.CheckUserToken
import com.example.volchonok.utils.isInternetAvailable
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreen(
    private val toNetworkErrorScreen: () -> Unit,
    private val toWelcomeScreen: () -> Unit,
    private val toCoursesScreen: () -> Unit,
    private val isDownloadStep: Boolean = false
) {
    private lateinit var imageHeightState: State<Dp>
    private var imageHeight = mutableStateOf(0.dp)
    private val imageMaxHeight = 500.dp

    @Composable
    fun Create() {
        if (!isInternetAvailable(LocalContext.current)) {
            toNetworkErrorScreen()
            return
        }

        if (!isDownloadStep) CreateAnimation()
        CreateUI()

        if (isDownloadStep) {
            DownloadAllData()
        } else {
            SetData()
        }
    }

    @Composable
    private fun SetData() {
        val context = LocalContext.current
        var isInvalidDataSaved by remember { mutableStateOf(false) }
        val srvc = CheckUserToken(context)
        val checkTokenCode = srvc.execute().get()

        if (checkTokenCode != 200.0 || isInvalidDataSaved) {
            toWelcomeScreen()
        } else {
            LaunchedEffect(Int) {

                launch {
                    withContext(Dispatchers.IO) {
                        val mapper = ObjectMapper().registerKotlinModule()
                        mapper.configure(
                            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false
                        )

                        setContext(context)

                        val sharedPreferences = getSharedPreferences()
                        val s = sharedPreferences.getString("UNIQUE_KEY", "")

                        try {
                            setCoursesData(mapper.readValue<List<CourseData>>(s!!))
                        } catch (e: MismatchedInputException) {
                            e.printStackTrace()
                            Log.e(
                                "TAG",
                                "=========================================================================++",
                                e
                            )
                            Log.d("TAG", sharedPreferences.all.toString())
                            isInvalidDataSaved = true
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun DownloadAllData() {
        var imageHeight by remember { mutableStateOf(0.dp) }
        this.imageHeight.value = imageHeight
        if (imageHeight == imageMaxHeight) toCoursesScreen()

        val oneHeightPart = imageMaxHeight / 5
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            launch {
                withContext(Dispatchers.IO) {
                    val start = System.currentTimeMillis()
                    getCoursesData(context, CourseDataAccessLevel.ONLY_COURSES_DATA)

                    imageHeight += oneHeightPart

                    getCoursesData(context, CourseDataAccessLevel.MODULES_DATA)

                    imageHeight += oneHeightPart

                    getCoursesData(context, CourseDataAccessLevel.NOTES_DATA)

                    imageHeight += oneHeightPart

                    getCoursesData(context, CourseDataAccessLevel.TESTS_DATA)

                    imageHeight += oneHeightPart

                    val data = getCoursesData(context, CourseDataAccessLevel.QUESTIONS_DATA)
                    imageHeight += oneHeightPart
                    Log.d(
                        "TAG",
                        "[login] Download time: ${(System.currentTimeMillis() - start) / 1000.0} s"
                    )

                    // сохраняем джэйсоном всё локально
                    setContext(context)
                    val sPref = getSharedPreferences()
                    Log.d("TAG", "data: " + ObjectMapper().registerKotlinModule().writeValueAsString(data[1].modules[0].lessonTests[0]))
                    sPref.edit().putString(
                        "UNIQUE_KEY", ObjectMapper().registerKotlinModule().writeValueAsString(data)
                    ).apply()
                }
            }
        }
    }

    @Composable
    private fun CreateAnimation() {
        var isAnimationStart by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            isAnimationStart = true
        }
        imageHeightState = animateDpAsState(
            targetValue = if (isAnimationStart) 500.dp else 0.dp,
            animationSpec = keyframes {
                durationMillis = 3000
            },
            finishedListener = { toCoursesScreen() },
            label = ""
        )
    }

    @Composable
    private fun CreateUI() {
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
                    .weight(if (isDownloadStep) 0.5f else 0.75f)
            ) {
                Image(
                    modifier = Modifier.size(
                        220.dp, if (isDownloadStep) imageHeight.value else imageHeightState.value
                    ),
                    painter = painterResource(id = R.drawable.suit_wolf),
                    contentDescription = "suit_wolf",
                    alignment = Alignment.TopCenter,
                    contentScale = ContentScale.FillWidth
                )
            }
            if (isDownloadStep) {
                Box(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .weight(0.25f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.first_download),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.background,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}