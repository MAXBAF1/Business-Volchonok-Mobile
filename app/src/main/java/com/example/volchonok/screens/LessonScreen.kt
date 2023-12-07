package com.example.volchonok.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.volchonok.RemoteInfoStorage
import com.example.volchonok.data.NoteData
import com.example.volchonok.data.TestData
import com.example.volchonok.enums.CourseDataAccessLevel
import com.example.volchonok.enums.LessonScreenType
import com.example.volchonok.interfaces.ILesson
import com.example.volchonok.screens.vidgets.others.InfoHeader
import com.example.volchonok.screens.vidgets.others.TopAppBar
import com.example.volchonok.utils.ShowToast

class LessonScreen(
    private val lessonData: ILesson,
    private val onBackClick: () -> Unit,
    private val toProfile: () -> Unit,
) {
    private var currentLessonScreen: MutableState<LessonScreenType> = mutableStateOf(
        if (lessonData is TestData && lessonData.isCompleted) LessonScreenType.TestResultsScreen
        else when (lessonData) {
            is TestData -> LessonScreenType.TestScreen
            is NoteData -> LessonScreenType.NoteScreen
            else -> LessonScreenType.TestScreen
        }
    )

    @Composable
    fun Create() {
        Column {
            TopAppBar(
                toProfile = toProfile, isLessonScreen = true, onBackClick = onBackClick
            ).Create()
            InfoHeader(title = lessonData.name, description = lessonData.description)

            when (currentLessonScreen.value) {
                LessonScreenType.TestResultsScreen -> {
                    if (RemoteInfoStorage.checkCourseDataLevel(CourseDataAccessLevel.TESTS_DATA)) {
                        TestResultsScreen(lessonData as TestData, onBackClick) {
                            currentLessonScreen.value = LessonScreenType.TestScreen
                        }.Create()
                    } else {
                        ShowToast()
                    }
                }

                LessonScreenType.TestScreen -> {
                    if (RemoteInfoStorage.checkCourseDataLevel(CourseDataAccessLevel.TESTS_DATA)) {
                        TestScreen(lessonData as TestData) {
                            currentLessonScreen.value = LessonScreenType.TestResultsScreen
                        }.Create()
                    } else {
                        ShowToast()
                    }
                }

                LessonScreenType.NoteScreen -> NoteScreen(
                    (lessonData as NoteData), onBackClick
                ).Create()
            }
        }
    }
}