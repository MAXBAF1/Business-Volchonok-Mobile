package com.example.volchonok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.CoursesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoursesScreen().Init(
                userData = UserData(R.drawable.wolf_icon, 5),
                arrayListOf(
                    CourseData("Название курса 1", "Описание 3"),
                    CourseData("Название курса 2", "Описание 4")
                )
            )

        }
    }
}

