package com.example.volchonok.screens.vidgets.others

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.volchonok.data.AnswerData
import com.example.volchonok.interfaces.IAnswersGroup

class CheckBoxAnswersGroup(
    override val list: List<AnswerData>, override val isBtnEnabled: MutableState<Boolean>
) : IAnswersGroup {
    private val answers = (List(list.size) { false }).toMutableStateList()
    override fun getAnswers(): SnapshotStateList<Boolean> = answers

    @Composable
    override fun Create() {
        Column(
            Modifier
                .selectableGroup()
                .padding(top = 15.dp)
        ) {
            list.forEachIndexed { i, answer ->
                CheckBoxRow(answer, i)
            }
        }
    }

    @Composable
    private fun CheckBoxRow(
        answer: AnswerData, index: Int
    ) {
        val primary = MaterialTheme.colorScheme.primary
        val secondary = MaterialTheme.colorScheme.secondary
        val checkedState = answers[index]
        val color = if (checkedState) primary else secondary
        Row(
            Modifier
                .padding(top = if (index == 0) 0.dp else 10.dp)
                .clip(CircleShape)
                .fillMaxWidth()
                .selectable(selected = checkedState, onClick = {
                    answers[index] = !checkedState
                    isBtnEnabled.value = answers.count { it } > 0
                })
                .border(1.dp, color, CircleShape)
                .padding(15.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checkedState, onCheckedChange = null, colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.secondary
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