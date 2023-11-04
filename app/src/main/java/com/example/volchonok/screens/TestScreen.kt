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
import androidx.compose.runtime.snapshots.SnapshotStateList
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
    private val toResultsScreen: (Iterable<Iterable<Boolean>>) -> Unit
) {
    private val answers: ArrayList<SnapshotStateList<Boolean>> = arrayListOf()
    private var currAnswers = mutableStateListOf<Boolean>()
    private var isBtnEnabled = mutableStateOf(false)
    private var itsMultipleAnswersQuestion = mutableStateOf(false)

    @Composable
    fun Create() {
        val questionNumber = remember { mutableIntStateOf(0) }
        val currQuestion = testData.questions[questionNumber.intValue]
        itsMultipleAnswersQuestion.value = currQuestion.answers.count { it.isCorrect } > 1
        val chooseAnswerText = if (itsMultipleAnswersQuestion.value) {
            R.string.choose_answers
        } else R.string.choose_answer

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
                text = stringResource(id = chooseAnswerText),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            AnswersGroup(currQuestion)
            AnswerButton(questionNumber)
        }
    }

    @Composable
    private fun AnswersGroup(currQuestion: QuestionData) {
        val answersGroup = if (itsMultipleAnswersQuestion.value) {
            CheckBoxAnswersGroup(currQuestion.answers, isBtnEnabled)
        } else RadioAnswersGroup(currQuestion.answers, isBtnEnabled)
        answersGroup.Create()
        currAnswers = answersGroup.getAnswers()
    }

    @Composable
    private fun AnswerButton(questionNumber: MutableIntState) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            DefaultButton(
                enabled = isBtnEnabled.value,
                text = stringResource(id = R.string.answer).uppercase()
            ) {
                if (questionNumber.intValue < testData.questions.size) {
                    answers.add(currAnswers)
                    isBtnEnabled.value = false

                    if (questionNumber.intValue + 1 == testData.questions.size) {
                        toResultsScreen(answers)
                    } else questionNumber.intValue++
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