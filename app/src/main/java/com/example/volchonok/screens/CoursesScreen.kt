package com.example.volchonok.screens

import android.util.Pair
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.enums.CourseDataAccessLevel
import com.example.volchonok.screens.vidgets.cards.CourseCard
import com.example.volchonok.screens.vidgets.others.Greeting
import com.example.volchonok.screens.vidgets.others.TopAppBar
import com.example.volchonok.services.CourseService
import com.example.volchonok.services.UserInfoService

class CoursesScreen(
    private val toCourseInfoScreen: (CourseData) -> Unit,
    private val toProfile: () -> Unit
) {
    private lateinit var userData: UserData
    private lateinit var coursesList: Iterable<CourseData>

    @Composable
    fun Create() {
        userData = UserInfoService(LocalContext.current).execute().get()
        coursesList = CourseService(LocalContext.current).execute(Pair(CourseDataAccessLevel.ONLY_COURSES_DATA, ArrayList<CourseData>())).get()
        Column {
            TopAppBar(userData, toProfile).Create()
            Column(
                Modifier
                    .padding(start = 30.dp, top = 0.dp, end = 30.dp, bottom = 15.dp)
                    .fillMaxSize()
            ) {
                Greeting("${userData.surname} ${userData.firstname}")
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