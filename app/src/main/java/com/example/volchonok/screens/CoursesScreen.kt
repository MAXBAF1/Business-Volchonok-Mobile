package com.example.volchonok.screens

import android.content.Context
import android.util.Log
import kotlin.Pair
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.volchonok.RemoteInfoStorage.*
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.TestData
import com.example.volchonok.data.UserData
import com.example.volchonok.enums.CourseDataAccessLevel
import com.example.volchonok.screens.vidgets.cards.CourseCard
import com.example.volchonok.screens.vidgets.others.Greeting
import com.example.volchonok.screens.vidgets.others.TopAppBar
import com.example.volchonok.services.CompletedAnswersService
import com.example.volchonok.services.UserInfoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoursesScreen(
    private val toCourseInfoScreen: (CourseData) -> Unit,
    private val toProfile: () -> Unit
) {
    private var userData: UserData? = null
    private lateinit var courses: MutableList<CourseData>

    @Composable
    fun Create() {
        val context = LocalContext.current

        userData = getUserData()
        if (userData == null) {
            setUserData(UserInfoService(context).execute().get())
        }
        courses = remember {
            getCoursesData(context, CourseDataAccessLevel.ONLY_COURSES_DATA)
        }

        if (!checkCourseDataLevel(CourseDataAccessLevel.MODULES_DATA))
            LaunchedEffect(Unit) {
                launch {
                    withContext(Dispatchers.IO) {
                        val start = System.currentTimeMillis()
                        getCoursesData(context, CourseDataAccessLevel.MODULES_DATA)
                        Log.d("TAG", "Create: modules was downloaded")
                        getCoursesData(context, CourseDataAccessLevel.NOTES_DATA)
                        Log.d("TAG", "Create: notes was downloaded")
                        getCoursesData(context, CourseDataAccessLevel.TESTS_DATA)
                        Log.d("TAG", "Create: tests was downloaded")
                        val data = getCoursesData(context, CourseDataAccessLevel.QUESTIONS_DATA)
                        Log.d("TAG", "Create: questions was downloaded")
                        loadCompletedAnswers(data, context)
                        Log.d(
                            "TAG",
                            "All data was downloaded in ${(System.currentTimeMillis() - start) / 1000.0} s"
                        )
                    }
                }
            }

        Column {
            TopAppBar(userData, toProfile).Create()
            Column(
                Modifier
                    .padding(start = 30.dp, top = 0.dp, end = 30.dp, bottom = 15.dp)
                    .fillMaxSize()
            ) {
                Greeting("${userData?.surname} ${userData?.firstname}")
                CoursesList(courses)
            }
        }
    }

    @Composable
    private fun CoursesList(coursesList: Iterable<CourseData>) {
        Column(modifier = Modifier.padding(top = 15.dp)) {
            coursesList.forEach {
                CourseCard(courseData = it, toCourseInfoScreen).Add()
            }
        }
    }

    private fun loadCompletedAnswers(data: List<CourseData>, context: Context) {
        val tests = mutableMapOf<Int, MutableMap<Int, List<Int>>>()
        val completedTests = mutableListOf<TestData>()
        val completedAnswersId = mutableListOf<Int>()

        // беру все вопросы из РЕШЁННЫХ тестов
        data.forEach { course ->
            course.modules.forEach { module ->
                completedTests.addAll(module.lessonTests
                    .filter { it.isCompleted }
                    .map { it as TestData })
            }
        }

        // сливаю в мапу [id вопроса; ответы]
        completedTests.forEach {test ->
            test.questions.forEach { question ->
                val answersId = question.answers.map { answer -> answer.id }
                    tests[test.id]?.set(question.id, answersId)
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