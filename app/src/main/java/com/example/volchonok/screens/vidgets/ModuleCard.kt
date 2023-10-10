package com.example.volchonok.screens.vidgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.ModuleData

class ModuleCard(private val moduleData: ModuleData) {
    private val completedLessonCnt = moduleData.lessonNotes.count { it.isCompleted }

    @Composable
    fun Add() {
        val cardColor = if (completedLessonCnt > 0) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondary

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = moduleData.name,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Row {
                        LessonsCntText()
                        InfoCircle()
                    }
                }
            }
        }
    }

    @Composable
    private fun LessonsCntText() {
        Card(
            modifier = Modifier
                .sizeIn(24.dp)
                .padding(end = 10.dp),
            shape = RoundedCornerShape(100),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
        ) {
            val lessonsCtnText =
                "$completedLessonCnt/${moduleData.lessonNotes.size} ${
                    stringResource(id = R.string.lessons_cnt)
                }"
            Text(
                text = lessonsCtnText,
                modifier = Modifier.padding(10.dp, 5.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    private fun InfoCircle() {
        Card(
            modifier = Modifier.size(24.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
        ) {
            Text(
                text = "i",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}