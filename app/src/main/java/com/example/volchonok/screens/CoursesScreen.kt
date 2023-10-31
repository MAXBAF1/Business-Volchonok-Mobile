package com.example.volchonok.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.vidgets.cards.CourseCard
import com.example.volchonok.screens.vidgets.others.Greeting
import com.example.volchonok.screens.vidgets.others.TopAppBar

class CoursesScreen(
    private val userData: UserData,
    private val coursesList: Iterable<CourseData>,
    private val toCourseInfoScreen: (CourseData) -> Unit,
    private val toProfile: () -> Unit
) {
    @Composable
    fun Create() {
        Column {
            TopAppBar(userData, toProfile).Create()
            Column(
                Modifier
                    .padding(start = 30.dp, top = 0.dp, end = 30.dp, bottom = 15.dp)
                    .fillMaxSize()
            ) {
                Greeting(userData.name)
                CoursesList(coursesList)
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