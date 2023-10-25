package com.example.volchonok.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.LessonData
import com.example.volchonok.data.ModuleData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.CourseInfoScreen
import com.example.volchonok.screens.CoursesScreen
import com.example.volchonok.screens.LessonsScreen
import com.example.volchonok.screens.LoginScreen
import com.example.volchonok.screens.NoteScreen
import com.example.volchonok.screens.ProfileScreen
import com.example.volchonok.screens.TestScreen
import com.example.volchonok.screens.WelcomeScreen

class Navigation(private val userData: UserData, private val coursesList: ArrayList<CourseData>) {
    private var navController: NavHostController? = null
    private var selectedCourse: CourseData? = null
    private var selectedModule: ModuleData? = null
    private var selectedLesson: LessonData? = null

    @Composable
    fun Create() {
        navController = rememberNavController()

        NavHost(
            navController = navController!!, startDestination = WELCOME_SCREEN_ROUTE
        ) {
            composable(WELCOME_SCREEN_ROUTE) { CreateWelcomeScreen() }
            composable(LOGIN_SCREEN_ROUTE) { CreateLoginScreen() }
            composable(COURSES_SCREEN_ROUTE) { CreateCoursesScreen() }
            composable(COURSE_INFO_SCREEN_ROUTE) { CreateCourseInfoScreen() }
            composable(LESSONS_SCREEN_ROUTE) { CreateLessonsScreen() }
            composable(NOTE_SCREEN_ROUTE) { CreateNoteScreen() }
            composable(TEST_SCREEN_ROUTE) { CreateTestScreen() }
            composable(PROFILE_SCREEN_ROUTE) { CreateProfileScreen() }
        }
    }

    @Composable
    private fun CreateWelcomeScreen() {
        WelcomeScreen(toLoginScreen = {
            navController!!.navigate(LOGIN_SCREEN_ROUTE)
        }).Create()
    }

    @Composable
    private fun CreateLoginScreen() {
        LoginScreen(toCoursesScreen = {
            navController!!.navigate(COURSES_SCREEN_ROUTE)
        }).Create()
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
        selectedModule?.let { moduleData ->
            LessonsScreen(userData = userData,
                moduleData = moduleData,
                onBackClick = { navController!!.popBackStack() },
                toProfile = { navController!!.navigate(PROFILE_SCREEN_ROUTE) },
                toNoteScreen = {
                    selectedLesson = it
                    navController!!.navigate(NOTE_SCREEN_ROUTE)
                },
                toTestScreen = {
                    selectedLesson = it
                    navController!!.navigate(TEST_SCREEN_ROUTE)
                }).Create()
        }
    }

    @Composable
    private fun CreateNoteScreen() {
        selectedLesson?.let {
            NoteScreen(
                userData = userData,
                testData = it,
                onBackClick = { navController!!.popBackStack() },
                toProfile = { navController!!.navigate(PROFILE_SCREEN_ROUTE) },
            ).Create()
        }
    }

    @Composable
    private fun CreateTestScreen() {
        selectedLesson?.let {
            TestScreen(
                userData = userData,
                testData = it,
                onBackClick = { navController!!.popBackStack() },
                toProfile = { navController!!.navigate(PROFILE_SCREEN_ROUTE) },
            ).Create()
        }
    }

    @Composable
    private fun CreateProfileScreen() {
        ProfileScreen(userData,
            coursesList,
            onBackClick = { navController!!.popBackStack() }).Create()
    }

    companion object {
        const val WELCOME_SCREEN_ROUTE = "WELCOME_SCREEN"
        const val LOGIN_SCREEN_ROUTE = "LOGIN_SCREEN"
        const val COURSES_SCREEN_ROUTE = "COURSES_SCREEN"
        const val COURSE_INFO_SCREEN_ROUTE = "COURSE_INFO_SCREEN"
        const val LESSONS_SCREEN_ROUTE = "LESSONS_SCREEN_ROUTE"
        const val NOTE_SCREEN_ROUTE = "NOTE_SCREEN_ROUTE"
        const val TEST_SCREEN_ROUTE = "TEST_SCREEN_ROUTE"
        const val PROFILE_SCREEN_ROUTE = "PROFILE_SCREEN_ROUTE"
    }
}