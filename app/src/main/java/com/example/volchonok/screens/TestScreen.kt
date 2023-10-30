package com.example.volchonok.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.AnswerData
import com.example.volchonok.data.TestData
import com.example.volchonok.screens.vidgets.others.DefaultButton
import com.example.volchonok.screens.vidgets.others.RadioAnswerGroup

class TestScreen(
    private val testData: TestData,
) {
    private val answers: ArrayList<AnswerData> = arrayListOf()

    @Composable
    fun Create() {
        var questionNumber by remember { mutableIntStateOf(0) }
        val currQuestion = testData.questions[questionNumber]
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompletedQuestionsProgress(questionNumber + 1, testData.questions.size)
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
            val radioAnswerGroup = RadioAnswerGroup(currQuestion.answers)
            radioAnswerGroup.Create()
            val answer = radioAnswerGroup.selectedAnswer.value
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
            ) {
                DefaultButton(
                    enabled = answer != null,
                    text = stringResource(id = R.string.answer).uppercase()
                ) {
                    if (questionNumber < testData.questions.size) {
                        answers.add(answer!!)
                        questionNumber++
                    }
                    if (questionNumber == testData.questions.size) {
                        // todo toResultsScreen
                    }
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