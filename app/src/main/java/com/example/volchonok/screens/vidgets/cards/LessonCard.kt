package com.example.volchonok.screens.vidgets.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.interfaces.ILesson

class LessonCard(
    private val lessonData: ILesson,
    private val toLessonScreen: (ILesson) -> Unit,
    private val isFirst: Boolean = false,
    private val isLast: Boolean = false
) {
    private var cardColor: Color? = null

    @Composable
    fun Create() {
        cardColor = if (lessonData.isCompleted) {
            MaterialTheme.colorScheme.primary
        } else MaterialTheme.colorScheme.onBackground

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isFirst) 0.dp else 15.dp, bottom = if (isLast) 15.dp else 0.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable { toLessonScreen(lessonData) },
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, cardColor!!),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(Modifier.padding(15.dp)) {
                NameRow()
                Text(
                    modifier = Modifier.padding(top = 15.dp),
                    text = lessonData.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = cardColor!!
                )
                Text(
                    modifier = Modifier.padding(top = 15.dp),
                    text = "${lessonData.durationTime} ${stringResource(id = R.string.min)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (lessonData.isCompleted) cardColor!! else MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

    @Composable
    private fun NameRow() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = lessonData.name,
                style = MaterialTheme.typography.titleSmall,
                color = cardColor!!
            )
            if (lessonData.isCompleted) {
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer, CircleShape
                        )
                        .padding(10.dp, 5.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.completed),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}