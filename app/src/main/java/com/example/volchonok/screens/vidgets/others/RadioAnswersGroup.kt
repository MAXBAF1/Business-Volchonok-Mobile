package com.example.volchonok.screens.vidgets.others

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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.volchonok.data.AnswerData
import com.example.volchonok.interfaces.IAnswersGroup

class RadioAnswersGroup(
    override val list: List<AnswerData>, override val isBtnEnabled: MutableState<Boolean>
) : IAnswersGroup {
    private val answers = (List(list.size) { false }).toMutableStateList()
    //override fun getAnswers(): SnapshotStateList<Boolean> = answers

    @Composable
    override fun Create() {
        Column(
            Modifier
                .selectableGroup()
                .padding(top = 15.dp)
        ) {
            list.forEachIndexed { i, answer ->
                RadioRow(answer, i)
            }
        }
    }


    @Composable
    private fun RadioRow(answer: AnswerData, index: Int) {
        val primary = MaterialTheme.colorScheme.primary
        val secondary = MaterialTheme.colorScheme.outline
        val isSelected = answers[index]
        val color = if (isSelected) primary else secondary
        Row(
            Modifier
                .padding(top = if (index == 0) 0.dp else 10.dp)
                .clip(CircleShape)
                .fillMaxWidth()
                .selectable(selected = isSelected, onClick = {
                    List(answers.size) { i ->
                        list[i].wasChooseByUser = false
                        answers[i] = false
                    }
                    list[index].wasChooseByUser = true
                    answers[index] = true
                    isBtnEnabled.value = true
                })
                .border(1.dp, color, CircleShape)
                .padding(15.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected, onClick = null, colors = RadioButtonDefaults.colors(
                    selectedColor = primary,
                    unselectedColor = secondary
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