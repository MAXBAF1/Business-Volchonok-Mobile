package com.example.volchonok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.CourseInfoScreen
import com.example.volchonok.screens.CoursesScreen
import com.example.volchonok.ui.theme.VolchonokTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Тестовые данные
        val userData = UserData(R.drawable.wolf_icon, 5)
        val coursesList = arrayListOf(
            CourseData("Название курса 1", "Описание 3"),
            CourseData("Название курса 2", "Описание 4")
        )


        setContent {
            val navController = rememberNavController()
            var selectedCourse: CourseData? = null

            VolchonokTheme {
                Surface {
                    NavHost(
                        navController = navController, startDestination = COURSES_SCREEN_ROUTE
                    ) {
                        composable(COURSES_SCREEN_ROUTE) {
                            CoursesScreen(userData = userData, coursesList = coursesList) {
                                selectedCourse = it
                                navController.navigate(COURSE_INFO_SCREEN_ROUTE)
                            }.Create()
                        }
                        composable(COURSE_INFO_SCREEN_ROUTE) {
                            selectedCourse?.let {
                                CourseInfoScreen(
                                    userData = userData, courseData = it
                                ).Create()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val COURSES_SCREEN_ROUTE = "COURSES_SCREEN"
        const val COURSE_INFO_SCREEN_ROUTE = "COURSE_INFO_SCREEN"
    }
}

