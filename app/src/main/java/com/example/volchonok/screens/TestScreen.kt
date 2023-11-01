package com.example.volchonok.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.QuestionData
import com.example.volchonok.data.TestData
import com.example.volchonok.screens.vidgets.others.CheckBoxAnswersGroup
import com.example.volchonok.screens.vidgets.others.DefaultButton
import com.example.volchonok.screens.vidgets.others.RadioAnswersGroup

class TestScreen(
    private val testData: TestData,
) {
    private val answers: ArrayList<Array<MutableState<Boolean>>> = arrayListOf()
    private var currAnswers = mutableStateListOf<Boolean>()

    @Composable
    fun Create() {
        val questionNumber = remember { mutableIntStateOf(0) }
        val currQuestion = testData.questions[questionNumber.intValue]
        for (i in 1..currQuestion.answers.size)
            currAnswers.add(false)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompletedQuestionsProgress(questionNumber.intValue + 1, testData.questions.size)
            Text(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(),
                text = currQuestion.text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.choose_answer),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            AnswersGroup(currQuestion)

            val test = remember { mutableIntStateOf(currAnswers.count { it } )}
            LaunchedEffect(test) {
                Log.d("MyLog", test.intValue.toString())
            }
            val isEnabled = remember { mutableStateOf(currAnswers.count { it } > 0)}

            AnswerButton(isEnabled.value, questionNumber)
        }
    }

    @Composable
    private fun AnswersGroup(currQuestion: QuestionData) {
        val answersGroup = if (currQuestion.answers.count { it.isCorrect } > 1) {
            CheckBoxAnswersGroup(currAnswers, currQuestion.answers)
        } else RadioAnswersGroup(currAnswers, currQuestion.answers)
        answersGroup.Create()
    }

    @Composable
    private fun AnswerButton(isEnabled: Boolean, questionNumber: MutableIntState) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            DefaultButton(
                enabled = true,
                text = stringResource(id = R.string.answer).uppercase()
            ) {
                if (questionNumber.intValue < testData.questions.size) {
                    //answers.add(currAnswers)
                    questionNumber.intValue++
                }
                if (questionNumber.intValue == testData.questions.size) {
                    // todo toResultsScreen
                }
            }
        }
    }

    @Composable
    private fun CompletedQuestionsProgress(currentNumber: Int, totalCnt: Int) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .padding(30.dp, 5.dp)
        ) {
            Text(
                text = "$currentNumber/$totalCnt",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}