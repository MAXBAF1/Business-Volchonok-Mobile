package com.example.volchonok.screens

import android.content.Context
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
import com.example.volchonok.RemoteInfoStorage
import com.example.volchonok.RemoteInfoStorage.getUserData
import com.example.volchonok.RemoteInfoStorage.setUserData
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.TestData
import com.example.volchonok.enums.CourseDataAccessLevel
import com.example.volchonok.services.CompletedAnswersService
import com.example.volchonok.services.UserInfoService
import com.example.volchonok.utils.isInternetAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreen(
    private val toNetworkErrorScreen: () -> Unit,
    private val toWelcomeScreen: () -> Unit,
    private val toCoursesScreen: () -> Unit,
) {
    @Composable
    fun Create() {
        if (!isInternetAvailable(LocalContext.current)) toNetworkErrorScreen()

        var isAnimationStart by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            isAnimationStart = true
        }
        val imageHeight = animateDpAsState(
            targetValue = if (isAnimationStart) 500.dp else 0.dp,
            animationSpec = keyframes {
                durationMillis = 3000
            },
            finishedListener = {
                if (getUserData() != null) toCoursesScreen()
                else toWelcomeScreen()
            },
            label = ""
        )

        val context = LocalContext.current
        setUserData(UserInfoService(context).execute().get())

        if (getUserData() != null) {
            LaunchedEffect(Int) {
                launch {
                    withContext(Dispatchers.IO) {
                        val start = System.currentTimeMillis()
                        RemoteInfoStorage.getCoursesData(
                            context,
                            CourseDataAccessLevel.ONLY_COURSES_DATA
                        )
                        RemoteInfoStorage.getCoursesData(
                            context,
                            CourseDataAccessLevel.MODULES_DATA
                        )
                        Log.d(
                            "TAG",
                            "[splash] Download time: ${(System.currentTimeMillis() - start) / 1000.0} s"
                        )
                    }
                }
            }
        }
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

    private fun loadCompletedAnswers(data: List<CourseData>, context: Context) {
        val tests = mutableMapOf<Int, MutableMap<Int, MutableList<Int>>>()
        val completedTests = mutableListOf<TestData>()
        val completedAnswersId = mutableListOf<Int>()

        // беру все вопросы из РЕШЁННЫХ тестов
        data.forEach { course ->
            course.modules.forEach { module ->
                completedTests.addAll(
                    module.lessonTests
                        .filter { it.isCompleted }
                        .map { it as TestData }
                )
            }
        }

        // сливаю в мапу [id вопроса; ответы]
        completedTests.forEach { test ->
            test.questions.forEach { question ->
                val answersId = question.answers.map { it.id }.toMutableList()

                if (tests.containsKey(test.id))
                    tests[test.id]?.put(question.id, answersId)
                else
                    tests[test.id] = mutableMapOf(Pair(question.id, answersId))

            }
        }


        // в лоб проверяю, какие ответы есть в бд (решённые вопросы по id теста)
        tests.forEach { test ->
            completedAnswersId.addAll(CompletedAnswersService(context).execute(test.key).get())
        }

        // в найденные ответы ставлю wasChoosedByUser
        completedTests.forEach { test ->
            test.questions.forEach { question ->
                question.answers.forEach { answer ->
                    answer.wasChooseByUser = completedAnswersId.contains(answer.id)
                }
            }
        }
    }
}