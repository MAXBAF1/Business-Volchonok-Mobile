package com.example.volchonok.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.volchonok.data.NoteData
import com.example.volchonok.data.TestData
import com.example.volchonok.data.UserData
import com.example.volchonok.interfaces.ILesson
import com.example.volchonok.screens.vidgets.others.TopAppBar
import com.example.volchonok.screens.vidgets.others.InfoHeader

class LessonScreen(
    private val userData: UserData,
    private val lessonData: ILesson,
    private val onBackClick: () -> Unit,
    private val toProfile: () -> Unit,
) {
    private val showTestResultsScreen: MutableState<Boolean> = mutableStateOf(false)
    private var answers: Iterable<Iterable<Boolean>>? = null

    @Composable
    fun Create() {
        Column {
            TopAppBar(userData, toProfile, true, onBackClick).Create()
            InfoHeader(title = lessonData.name, description = lessonData.description)

            if (showTestResultsScreen.value){
                TestResultsScreen((lessonData as TestData).questions, answers!!).Create()
            }
            else if (lessonData is TestData) TestScreen(lessonData) {
                showTestResultsScreen.value = true
                answers = it
            }.Create()
            else if (lessonData is NoteData) NoteScreen(lessonData).Create()
        }
    }
}