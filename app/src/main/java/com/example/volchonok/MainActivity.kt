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
    private val userData = UserData(1, "petr_ivanov", "Пётр Иванов", R.drawable.wolf_icon, 5, "Almost Fluent")
    private val shortText = "Learn the basics of the language: make new friends, plan a family di"
    private val mediumText =
        "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!"
    private val explanationText =
        "Объяснения ответа и почему он верный и тд .... ake new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language"
    private val coursesList = arrayListOf(
        CourseData(
            "Название курса 1",
            listOf(
                ModuleData(
                    "Модуль 1",
                    mediumText,
                    listOf(
                        NoteData("Урок 1", mediumText, "30", true, shortText),
                        NoteData("Урок 2", mediumText, "30", false, shortText)
                    ),
                    listOf(
                        TestData(
                            "Тест 1 (урок 1)",
                            mediumText,
                            "30",
                            true,
                            listOf(
                                QuestionData(
                                    "Вопрос 1", listOf(
                                        AnswerData("Ответ 1", false, explanationText),
                                        AnswerData("Ответ 2", true, explanationText),
                                        AnswerData("Ответ 3", false, explanationText)
                                    )
                                ), QuestionData(
                                    "Вопрос 2", listOf(
                                        AnswerData("Ответ 4", false, explanationText),
                                        AnswerData("Ответ 5", true, explanationText),
                                        AnswerData("Ответ 6", true, explanationText)
                                    )
                                ), QuestionData(
                                    "Вопрос 3", listOf(
                                        AnswerData("Ответ 7", false, explanationText),
                                        AnswerData("Ответ 8", true, explanationText),
                                        AnswerData("Ответ 9", false, explanationText)
                                    )
                                )
                            )
                        ), TestData(
                            "Тест 2 (урок 2)", mediumText, "30", false,
                            listOf(
                                QuestionData(
                                    mediumText,
                                    listOf(
                                        AnswerData("Ответ 1", false, explanationText),
                                        AnswerData("Ответ 2", true, explanationText),
                                        AnswerData("Ответ 3", false, explanationText)
                                    )
                                )
                            )
                        )
                    ),
                ), ModuleData("Модуль 2", mediumText, emptyList(), emptyList())
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

