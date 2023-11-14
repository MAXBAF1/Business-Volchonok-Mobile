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
                    Navigation().Create()
                }
            }
        }
    }
}

