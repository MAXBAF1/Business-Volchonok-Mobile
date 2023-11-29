package com.example.volchonok.screens

import android.util.Log
import android.util.Pair
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.volchonok.RemoteInfoStorage
import com.example.volchonok.RemoteInfoStorage.getCoursesData
import com.example.volchonok.data.NoteData
import com.example.volchonok.data.TestData
import com.example.volchonok.data.UserData
import com.example.volchonok.enums.CourseDataAccessLevel
import com.example.volchonok.enums.LessonScreenType
import com.example.volchonok.interfaces.ILesson
import com.example.volchonok.screens.vidgets.others.TopAppBar
import com.example.volchonok.screens.vidgets.others.InfoHeader
import com.example.volchonok.services.CourseService

class LessonScreen(
    private val lessonData: ILesson,
    private val onBackClick: () -> Unit,
    private val toProfile: () -> Unit,
) {
    private var currentLessonScreen: MutableState<LessonScreenType> = mutableStateOf(
        when (lessonData) {
            is TestData -> LessonScreenType.TestScreen
            is NoteData -> LessonScreenType.NoteScreen
            else -> LessonScreenType.TestScreen
        }
    )
    private var answers: Iterable<Iterable<Boolean>>? = null

    @Composable
    fun Create() {
        answers = (getCoursesData(
            LocalContext.current,
            CourseDataAccessLevel.QUESTIONS_DATA
        )[0].modules[0].lessonTests[0] as TestData)
            .questions
            .map { q -> q.answers.map { it.isCorrect } }

        Column {
            TopAppBar(
                toProfile = toProfile, isLessonScreen = true, onBackClick = onBackClick
            ).Create()
            InfoHeader(title = lessonData.name, description = lessonData.description)

            when (currentLessonScreen.value) {
                LessonScreenType.TestResultsScreen -> {
                    if (RemoteInfoStorage.checkCourseDataLevel(CourseDataAccessLevel.TESTS_DATA)) {
                        TestResultsScreen(
                            (lessonData as TestData).questions,
                            answers!!,
                            onBackClick
                        ) {
                            currentLessonScreen.value = LessonScreenType.TestScreen
                        }.Create()
                    } else {
                        Log.d("TAG", "Данные грузятся!")
                        //TODO: @Max разобраться, как делать вывод на экран сообщение, что данные подгружаются
                    }
                }

                LessonScreenType.TestScreen -> {
                    if (RemoteInfoStorage.checkCourseDataLevel(CourseDataAccessLevel.TESTS_DATA)) {
                        TestScreen(lessonData as TestData) {
                            currentLessonScreen.value = LessonScreenType.TestResultsScreen
                            answers = it
                        }.Create()
                    } else {
                        Log.d("TAG", "Данные грузятся!")
                        //TODO: @Max разобраться, как делать вывод на экран сообщение, что данные подгружаются
                    }
                }

                LessonScreenType.NoteScreen -> NoteScreen(
                    lessonData as NoteData, onBackClick
                ).Create()
            }
        }
    }
}