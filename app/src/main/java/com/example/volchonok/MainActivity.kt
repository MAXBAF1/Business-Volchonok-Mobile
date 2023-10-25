package com.example.volchonok

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.LessonData
import com.example.volchonok.data.ModuleData
import com.example.volchonok.data.ReviewData
import com.example.volchonok.data.UserData
import com.example.volchonok.enums.LessonType
import com.example.volchonok.navigation.Navigation
import com.example.volchonok.screens.CourseInfoScreen
import com.example.volchonok.screens.CoursesScreen
import com.example.volchonok.screens.LessonsScreen
import com.example.volchonok.screens.ProfileScreen
import com.example.volchonok.ui.theme.VolchonokTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VolchonokTheme {
                Scaffold {
                    Navigation(userData, coursesList).Create()
                }
            }
        }
    }


    // Тестовые данные
    private val userData = UserData("Пётр Иванов", R.drawable.wolf_icon, 5, "Almost Fluent")
    private val coursesList = arrayListOf(
        CourseData(
            "Название курса 1",
            listOf(
                ModuleData(
                    "Модуль 1",
                    "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!",
                    listOf(
                        LessonData(
                            "Урок 1",
                            "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!",
                            "30",
                            LessonType.Note,
                            true
                        ), LessonData("Урок 2", "Описание", "30", LessonType.Note)
                    ),
                    listOf(
                        LessonData(
                            "Тест 1 (урок 1)",
                            "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!",
                            "30",
                            LessonType.Test,
                            true
                        ), LessonData("Тест 2 (урок 2)", "Описание", "30", LessonType.Test)
                    ),
                ), ModuleData("Модуль 2", "", emptyList(), emptyList())
            ),
            "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the lang",
            "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the lang",
            listOf(
                ReviewData(
                    userData,
                    "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!"
                ), ReviewData(
                    userData,
                    "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!"
                ), ReviewData(
                    userData,
                    "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!"
                )
            )

        ),
    )

}

