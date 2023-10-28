package com.example.volchonok.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
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
    @Composable
    fun Create() {
        Column {
            TopAppBar(userData, toProfile, true, onBackClick).Create()
            InfoHeader(title = lessonData.name, description = lessonData.description)
            if (lessonData is TestData) TestScreen(lessonData).Create()
            else if (lessonData is NoteData) NoteScreen(lessonData).Create()
        }
    }
}