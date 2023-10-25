package com.example.volchonok.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.volchonok.data.LessonData
import com.example.volchonok.data.ModuleData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.vidgets.InfoHeader
import com.example.volchonok.screens.vidgets.TopAppBar

class TestScreen(
    private val userData: UserData,
    private val testData: LessonData,
    private val onBackClick: () -> Unit,
    private val toProfile: () -> Unit,
) {
    @Composable
    fun Create() {
        Column {
            TopAppBar(userData, toProfile, true, onBackClick).Create()
            InfoHeader(title = testData.name, description = testData.description)
        }
    }
}