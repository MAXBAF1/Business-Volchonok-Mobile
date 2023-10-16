package com.example.volchonok.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.ModuleData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.CourseInfoScreen
import com.example.volchonok.screens.CoursesScreen
import com.example.volchonok.screens.LessonsScreen
import com.example.volchonok.screens.ProfileScreen

class Navigation(private val userData: UserData, private val coursesList: ArrayList<CourseData>) {
    private var navController: NavHostController? = null
    private var selectedCourse: CourseData? = null
    private var selectedModule: ModuleData? = null

    @Composable
    fun Create() {
        navController = rememberNavController()

        NavHost(
            navController = navController!!, startDestination = COURSES_SCREEN_ROUTE
        ) {
            composable(COURSES_SCREEN_ROUTE) { CreateCoursesScreen() }
            composable(COURSE_INFO_SCREEN_ROUTE) { CreateCourseInfoScreen() }
            composable(LESSONS_SCREEN_ROUTE) { CreateLessonsScreen() }
            composable(PROFILE_SCREEN_ROUTE) { CreateProfileScreen() }
        }
    }

    @Composable
    private fun CreateCoursesScreen() {
        CoursesScreen(userData = userData, coursesList = coursesList, toCourseInfoScreen = {
            selectedCourse = it
            navController!!.navigate(COURSE_INFO_SCREEN_ROUTE)
        }, toProfile = { navController!!.navigate(PROFILE_SCREEN_ROUTE) }).Create()
    }

    @Composable
    private fun CreateCourseInfoScreen() {
        selectedCourse?.let { courseData ->
            CourseInfoScreen(userData = userData, courseData = courseData, toLessonsScreen = {
                selectedModule = it
                navController!!.navigate(LESSONS_SCREEN_ROUTE)
            }, toProfile = { navController!!.navigate(PROFILE_SCREEN_ROUTE) }).Create()
        }
    }

    @Composable
    private fun CreateLessonsScreen() {
        selectedModule?.let {
            LessonsScreen(
                userData = userData,
                moduleData = it,
                onBackClick = { navController!!.popBackStack() },
                toProfile = { navController!!.navigate(PROFILE_SCREEN_ROUTE) },
            ).Create()
        }
    }

    @Composable
    private fun CreateProfileScreen() {
        ProfileScreen(
            userData,
            coursesList,
            onBackClick = { navController!!.popBackStack() }).Create()
    }


    companion object {
        const val COURSES_SCREEN_ROUTE = "COURSES_SCREEN"
        const val COURSE_INFO_SCREEN_ROUTE = "COURSE_INFO_SCREEN"
        const val LESSONS_SCREEN_ROUTE = "LESSONS_SCREEN_ROUTE"
        const val PROFILE_SCREEN_ROUTE = "PROFILE_SCREEN_ROUTE"
    }
}