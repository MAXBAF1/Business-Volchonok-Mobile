package com.example.volchonok.screens

import android.util.Log
import kotlin.Pair
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.volchonok.RemoteInfoStorage.*
import com.example.volchonok.data.AnswerData
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.NoteData
import com.example.volchonok.data.TestData
import com.example.volchonok.data.UserData
import com.example.volchonok.enums.CourseDataAccessLevel
import com.example.volchonok.screens.vidgets.cards.CourseCard
import com.example.volchonok.screens.vidgets.others.Greeting
import com.example.volchonok.screens.vidgets.others.TopAppBar
import com.example.volchonok.services.ChooseAnswerService
import com.example.volchonok.services.CompleteCourseService
import com.example.volchonok.services.CompletedAnswersService
import com.example.volchonok.services.UserInfoService
import com.example.volchonok.services.enums.ServiceStringValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
                        Log.d(
                            "TAG",
                            "All data was downloaded in ${(System.currentTimeMillis() - start) / 1000.0} s"
                        )



                        val tests = mutableMapOf<Int, MutableMap<Int, List<Int>>>()
                        // беру все вопросы из РЕШЁННЫХ тестов
                        // сливаю в мапу [id вопроса; ответы]
                        data.forEach { course ->
                            course.modules.forEach { module ->
                                module.lessonTests
                                    .filter { it.isCompleted }
                                    .forEach {
                                        val test = it as TestData
                                        test.questions.forEach { question ->
                                            if (tests.containsKey(test.id))
                                                tests[test.id]!![question.id] = question.answers.map { answer -> answer.id }
                                            else
                                                tests[test.id] =
                                                    mutableMapOf(Pair(question.id, question.answers.map { answer -> answer.id }))
                                        }
                                    }
                            }
                        }

                        // в лоб проверяю, какие ответы есть в бд (решённые вопросы по id теста)
                        val answersId = mutableListOf<Int>()

                        tests.forEach { test ->
                            answersId.addAll(CompletedAnswersService(context).execute(test.key).get())
                        }


                        // в найденные ответы ставлю wasChoosedByUser

                        data.forEach { course ->
                            course.modules.forEach { module ->
                                module.lessonTests
                                    .filter { it.isCompleted }
                                    .forEach {
                                        (it as TestData).questions.forEach { question ->
                                            question.answers.forEach { answer ->
                                                if (answersId.contains(answer.id)) {
                                                    answer.wasChooseByUser = true
                                                }
                                            }
                                        }
                                    }
                            }
                        }

                        Log.d("TAG", "all completed! ")
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
}