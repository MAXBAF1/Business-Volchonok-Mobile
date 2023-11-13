package com.example.volchonok.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.rememberNavController
import com.example.volchonok.data.NoteData
import com.example.volchonok.data.TestData
import com.example.volchonok.data.UserData
import com.example.volchonok.enums.LessonScreenType
import com.example.volchonok.interfaces.ILesson
import com.example.volchonok.screens.vidgets.others.TopAppBar
import com.example.volchonok.screens.vidgets.others.InfoHeader

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
        Column {
            TopAppBar(
                toProfile = toProfile, isLessonScreen = true, onBackClick = onBackClick
            ).Create()
            InfoHeader(title = lessonData.name, description = lessonData.description)

            when (currentLessonScreen.value) {
                LessonScreenType.TestResultsScreen -> {
                    TestResultsScreen((lessonData as TestData).questions, answers!!, onBackClick) {
                        currentLessonScreen.value = LessonScreenType.TestScreen
                    }.Create()
                }

                LessonScreenType.TestScreen -> {
                    TestScreen(lessonData as TestData) {
                        currentLessonScreen.value = LessonScreenType.TestResultsScreen
                        answers = it
                    }.Create()
                }

                LessonScreenType.NoteScreen -> NoteScreen(lessonData as NoteData).Create()
            }
        }
    }
}