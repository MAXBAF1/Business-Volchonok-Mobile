package com.example.volchonok.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.vidgets.CourseCard
import com.example.volchonok.screens.vidgets.Greeting
import com.example.volchonok.screens.vidgets.TopAppBar

class CoursesScreen(
    private val userData: UserData,
    private val coursesList: Iterable<CourseData>,
    private val toCourseInfoScreen: (CourseData) -> Unit
) {
    @Composable
    fun Create() {
        Column(
            Modifier
                .padding(30.dp, 15.dp)
                .fillMaxSize()
        ) {
            TopAppBar(userData).Create()
            Greeting()
            CoursesList(coursesList)
        }
    }

    @Composable
    private fun CoursesList(coursesList: Iterable<CourseData>) {
        Column(modifier = Modifier.padding(top = 20.dp)) {
            coursesList.forEach {
                CourseCard(courseData = it, toCourseInfoScreen).Add()
            }
        }
    }
}