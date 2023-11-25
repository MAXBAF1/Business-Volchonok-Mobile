package com.example.volchonok.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.ModuleData
import com.example.volchonok.data.UserData
import com.example.volchonok.interfaces.ILesson
import com.example.volchonok.screens.CourseInfoScreen
import com.example.volchonok.screens.CoursesScreen
import com.example.volchonok.screens.LessonScreen
import com.example.volchonok.screens.LessonsScreen
import com.example.volchonok.screens.LoginScreen
import com.example.volchonok.screens.NetworkErrorScreen
import com.example.volchonok.screens.ProfileScreen
import com.example.volchonok.screens.SplashScreen
import com.example.volchonok.screens.WelcomeScreen
import com.example.volchonok.services.LoginService

class Navigation {
    private var navController: NavHostController? = null
    private var selectedCourse: CourseData? = null
    private var selectedModule: ModuleData? = null
    private var selectedLesson: ILesson? = null

    @Composable
    fun Create() {
        navController = rememberNavController()

        NavHost(
            navController = navController!!, startDestination = SPLASH_SCREEN_ROUTE
        ) {
            composable(NETWORK_ERROR_SCREEN_ROUTE) { CreateNetworkErrorScreen() }
            composable(SPLASH_SCREEN_ROUTE) { CreateSplashScreen() }
            composable(WELCOME_SCREEN_ROUTE) { CreateWelcomeScreen() }
            composable(LOGIN_SCREEN_ROUTE) { CreateLoginScreen() }
            composable(COURSES_SCREEN_ROUTE) { CreateCoursesScreen() }
            composable(COURSE_INFO_SCREEN_ROUTE) { CreateCourseInfoScreen() }
            composable(LESSONS_SCREEN_ROUTE) { CreateLessonsScreen() }
            composable(LESSON_SCREEN_ROUTE) { CreateLessonScreen() }
            composable(PROFILE_SCREEN_ROUTE) { CreateProfileScreen() }
        }
    }

    @Composable
    private fun CreateNetworkErrorScreen() {
        NetworkErrorScreen().Create()
    }

    @Composable
    private fun CreateSplashScreen() {
        SplashScreen(toNetworkErrorScreen = {
            navController!!.navigate(NETWORK_ERROR_SCREEN_ROUTE)
        }, toWelcomeScreen = {
            navController!!.navigate(WELCOME_SCREEN_ROUTE)
        }, toCoursesScreen = {
            navController!!.navigate(COURSES_SCREEN_ROUTE)
        }).Create()
    }

    @Composable
    private fun CreateWelcomeScreen() {
        WelcomeScreen(toLoginScreen = {
            navController!!.navigate(LOGIN_SCREEN_ROUTE)
        }).Create()
    }

    @Composable
    private fun CreateLoginScreen() {
        val ctx = LocalContext.current

        LoginScreen(toCoursesScreen = { navController!!.navigate(COURSES_SCREEN_ROUTE) },
            getLoginResult = { loginText, passwordText ->
                val e = LoginService(ctx).execute(loginText, passwordText).get()
                return@LoginScreen e
            }).Create()
    }

    @Composable
    private fun CreateCoursesScreen() {
        Log.d("MyLog", "CreateCoursesScreen")
        CoursesScreen(toCourseInfoScreen = {
            selectedCourse = it
            navController!!.navigate(COURSE_INFO_SCREEN_ROUTE)
        }, toProfile = { navController!!.navigate(PROFILE_SCREEN_ROUTE) }).Create()
    }

    @Composable
    private fun CreateCourseInfoScreen() {
        selectedCourse?.let { courseData ->
            CourseInfoScreen(courseData = courseData, toLessonsScreen = {
                selectedModule = it
                navController!!.navigate(LESSONS_SCREEN_ROUTE)
            }, toProfile = { navController!!.navigate(PROFILE_SCREEN_ROUTE) }).Create()
        }
    }

    @Composable
    private fun CreateLessonsScreen() {
        selectedModule?.let { moduleData ->
            LessonsScreen(moduleData = moduleData,
                onBackClick = { navController!!.popBackStack() },
                toProfile = { navController!!.navigate(PROFILE_SCREEN_ROUTE) },
                toLessonScreen = {
                    selectedLesson = it
                    navController!!.navigate(LESSON_SCREEN_ROUTE)
                }).Create()
        }
    }

    @Composable
    private fun CreateLessonScreen() {
        selectedLesson?.let {
            LessonScreen(
                lessonData = it,
                onBackClick = { navController!!.popBackStack() },
                toProfile = { navController!!.navigate(PROFILE_SCREEN_ROUTE) },
            ).Create()
        }
    }

    @Composable
    private fun CreateProfileScreen() {
        ProfileScreen(onBackClick = { navController!!.popBackStack() }).Create()
    }

    companion object {
        const val NETWORK_ERROR_SCREEN_ROUTE = "NETWORK_ERROR"
        const val SPLASH_SCREEN_ROUTE = "SPLASH_SCREEN"
        const val WELCOME_SCREEN_ROUTE = "WELCOME_SCREEN"
        const val LOGIN_SCREEN_ROUTE = "LOGIN_SCREEN"
        const val COURSES_SCREEN_ROUTE = "COURSES_SCREEN"
        const val COURSE_INFO_SCREEN_ROUTE = "COURSE_INFO_SCREEN"
        const val LESSONS_SCREEN_ROUTE = "LESSONS_SCREEN_ROUTE"
        const val LESSON_SCREEN_ROUTE = "LESSON_SCREEN_ROUTE"
        const val PROFILE_SCREEN_ROUTE = "PROFILE_SCREEN_ROUTE"
    }
}