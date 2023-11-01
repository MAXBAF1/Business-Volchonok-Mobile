package com.example.volchonok

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import com.example.volchonok.data.AnswerData
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.TestData
import com.example.volchonok.data.ModuleData
import com.example.volchonok.data.NoteData
import com.example.volchonok.data.QuestionData
import com.example.volchonok.data.ReviewData
import com.example.volchonok.data.UserData
import com.example.volchonok.navigation.Navigation
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
                        NoteData(
                            "Урок 1",
                            "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!",
                            "30",
                            true,
                            "Learn the basics of the language: make new friends, plan a family di"
                        ), NoteData("Урок 2", "Описание", "30", false, "ghggh")
                    ),
                    listOf(
                        TestData(
                            "Тест 1 (урок 1)",
                            "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!",
                            "30",
                            true,
                            listOf(
                                QuestionData(
                                    "Вопрос 1", listOf(
                                        AnswerData("Ответ 1", false),
                                        AnswerData("Ответ 2", true),
                                        AnswerData("Ответ 3", false)
                                    )
                                ), QuestionData(
                                    "Вопрос 2", listOf(
                                        AnswerData("Ответ 4", false),
                                        AnswerData("Ответ 5", true),
                                        AnswerData("Ответ 6", true)
                                    )
                                ), QuestionData(
                                    "Вопрос 3", listOf(
                                        AnswerData("Ответ 7", false),
                                        AnswerData("Ответ 8", true),
                                        AnswerData("Ответ 9", false)
                                    )
                                )
                            )
                        ), TestData(
                            "Тест 2 (урок 2)", "Описание", "30", false, listOf(
                                QuestionData(
                                    "Tool  yang dapat digunakan untuk memanipulasi dua objek atau lebih atau lebih Pada Adobe Illustrator  disebut ...",
                                    listOf(
                                        AnswerData("Ответ 1", false),
                                        AnswerData("Ответ 2", true),
                                        AnswerData("Ответ 3", false)
                                    )
                                )
                            )
                        )
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

