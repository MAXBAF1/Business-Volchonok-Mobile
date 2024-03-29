package com.example.volchonok.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.volchonok.RemoteInfoStorage
import com.example.volchonok.RemoteInfoStorage.getCoursesData
import com.example.volchonok.RemoteInfoStorage.getUserData
import com.example.volchonok.RemoteInfoStorage.setUserData
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.enums.CourseDataAccessLevel
import com.example.volchonok.screens.vidgets.cards.CourseCard
import com.example.volchonok.screens.vidgets.others.Greeting
import com.example.volchonok.screens.vidgets.others.TopAppBar
import com.example.volchonok.services.CheckUserToken
import com.example.volchonok.services.UserInfoService

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