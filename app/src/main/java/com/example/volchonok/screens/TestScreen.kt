package com.example.volchonok.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.TestData

class TestScreen(
    private val testData: TestData,
) {

    @Composable
    fun Create() {
        val questionNumber = 1
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompletedQuestionsProgress(questionNumber, testData.questions.size)
            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = testData.questions[questionNumber - 1].text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier.padding(top = 30.dp).fillMaxWidth(),
                text = stringResource(id = R.string.choose_answer),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

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