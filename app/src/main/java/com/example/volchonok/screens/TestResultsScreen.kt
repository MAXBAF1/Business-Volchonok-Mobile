package com.example.volchonok.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.AnswerData
import com.example.volchonok.data.QuestionData
import com.example.volchonok.data.TestData
import com.example.volchonok.enums.ButtonType
import com.example.volchonok.screens.vidgets.others.DefaultButton
import com.example.volchonok.services.*
import java.util.Collections

class TestResultsScreen(
    private val testData: TestData,
    private val onCompleteBtn: () -> Unit,
    private val onRepeatBtn: () -> Unit,
) {
    @Composable
    fun Create() {
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, top = 0.dp, end = 30.dp, bottom = 30.dp),
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    modifier = Modifier.padding(top = 30.dp),
                    text = stringResource(id = R.string.lets_check_answers),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                testData.questions.forEachIndexed { i, question ->
                    QuestionSection(question, i == testData.questions.size - 1)
                }
            }
            DefaultButton(
                stringResource(id = R.string.complete).uppercase(),
                Modifier,
                true,
                onClick = {
                    onCompleteBtn.invoke()

                    testData.isCompleted = true

                    CheckUserToken(context).execute().get()
                    //TODO если тут возвращается Double.NaN,
                    // то нужно переместить пользователя на страницу логина
                    // если прилетает 200, то всё ок

                    ChooseAnswerService(context).execute(
                            mapOf(Pair(testData.id, Collections.emptyList())),
                            testData.questions.associate { q ->
                                Pair(
                                    q.id,
                                    q.answers
                                        .filter { a -> a.wasChooseByUser }
                                        .map { a -> a.id }
                                )
                            }
                        ).get()
                }
            )
            DefaultButton(
                stringResource(id = R.string.repeat_again).uppercase(),
                Modifier.padding(top = 10.dp),
                true,
                ButtonType.Outlined
            ) {
                onRepeatBtn.invoke()

                testData.isCompleted = true

                ChooseAnswerService(context).execute(
                    mapOf(Pair(testData.id, Collections.emptyList())),
                    testData.questions.associate { q ->
                        Pair(
                            q.id,
                            q.answers
                                .filter { a -> a.wasChooseByUser }
                                .map { a -> a.id }
                        )
                    }
                ).get()

                testData.questions.forEach { it.answers.forEach { a -> a.wasChooseByUser = false } }
            }
        }
    }

    @Composable
    private fun QuestionSection(question: QuestionData, isEnd: Boolean = false) {
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = question.text,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        for (j in question.answers.indices) {
            if (question.answers[j].wasChooseByUser) {
                Answer(question.answers[j], question.answers[j].isCorrect && isEnd)
            }
        }
        for (j in question.answers.indices) {
            if (question.answers[j].isCorrect && !question.answers[j].wasChooseByUser) {
                Answer(question.answers[j], isEnd, true)
            }
        }
    }

    @Composable
    private fun Answer(
        answerData: AnswerData, isEnd: Boolean = false, itsAdditional: Boolean = false
    ) {
        val color = if (answerData.isCorrect) {
            MaterialTheme.colorScheme.primary
        } else MaterialTheme.colorScheme.error
        val answerColor =
            if (itsAdditional) MaterialTheme.colorScheme.onBackground else color
        val icon = if (answerData.isCorrect) {
            R.drawable.ic_correct_answer
        } else R.drawable.ic_wrong_answer
        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(id = icon),
                contentDescription = "ic_correct_answer",
                tint = answerColor
            )
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = answerData.text,
                style = MaterialTheme.typography.labelSmall,
                color = answerColor
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 8.dp, bottom = if (isEnd) 20.dp else 0.dp)
                .border(1.dp, color, RoundedCornerShape(20.dp))
                .padding(10.dp, 15.dp)
        ) {
            Text(
                modifier = Modifier,
                text = answerData.explanation,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

}