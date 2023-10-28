package com.example.volchonok.screens.vidgets.others

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.volchonok.data.AnswerData

class RadioAnswerGroup(private val list: List<AnswerData>) {
    var selectedAnswer: MutableState<AnswerData?> = mutableStateOf(null)

    @Composable
    fun Create() {
        val nullAnswer: AnswerData? = null
        val (selectedAnswer, onOptionSelected) = remember { mutableStateOf(nullAnswer) }
        this.selectedAnswer.value = selectedAnswer

        Column(
            Modifier
                .selectableGroup()
                .padding(top = 15.dp)
        ) {
            list.forEachIndexed { i, answer ->
                RadioRow(answer, selectedAnswer, onOptionSelected, i == 0)
            }
        }
    }

    @Composable
    private fun RadioRow(
        answer: AnswerData,
        selectedAnswer: AnswerData?,
        onOptionSelected: (AnswerData) -> Unit,
        isFirst: Boolean
    ) {
        val primary = MaterialTheme.colorScheme.primary
        val secondary = MaterialTheme.colorScheme.secondary
        val color = if (answer == selectedAnswer) primary else secondary
        Row(
            Modifier
                .padding(top = if (isFirst) 0.dp else 10.dp)
                .clip(CircleShape)
                .fillMaxWidth()
                .selectable(
                    selected = (answer == selectedAnswer),
                    onClick = { onOptionSelected(answer) })
                .border(1.dp, color, CircleShape)
                .padding(15.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (answer == selectedAnswer),
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.secondary
                )
            )
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = answer.text,
                style = MaterialTheme.typography.titleSmall,
                color = color
            )
        }
    }
}