package com.example.volchonok.screens.vidgets.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.LessonData
import com.example.volchonok.data.ModuleData
import com.example.volchonok.screens.vidgets.CompletedLessonsCntText

class ModuleCard(
    private val moduleData: ModuleData,
    private val toLessonsScreen: (ModuleData) -> Unit
) {

    @Composable
    fun Add() {
        var isExpanded by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            shape = RoundedCornerShape(25.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(Modifier) {
                ModuleRow { isExpanded = !isExpanded }
                if (isExpanded && moduleData.lessonNotes.isNotEmpty()) {
                    LessonsList()
                }
            }

        }
    }

    @Composable
    private fun ModuleRow(onExpand: () -> Unit) {
        val cardColor = if (moduleData.lessonNotes.count { it.isCompleted } > 0) {
            MaterialTheme.colorScheme.primary
        } else MaterialTheme.colorScheme.secondary
        val interactionSource = remember { MutableInteractionSource() }

        Row(
            Modifier
                .background(cardColor)
                .clickable(
                    onClick = { onExpand() },
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = MaterialTheme.colorScheme.onPrimaryContainer),
                )
                .padding(15.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = moduleData.name,
                modifier = Modifier,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
            ) {
                Row {
                    CompletedLessonsCntText(moduleData)
                    InfoCircle()
                }
            }
        }
    }

    @Composable
    private fun LessonsList() {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.onPrimary)
                .clickable { toLessonsScreen(moduleData) }
                .padding(15.dp, 0.dp, 15.dp, 15.dp)
        ) {
            moduleData.lessonNotes.forEach { LessonInfo(it) }
        }
    }

    @Composable
    private fun InfoCircle() {
        Card(
            modifier = Modifier.padding(start = 10.dp).size(24.dp),
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


    @Composable
    private fun LessonInfo(lessonData: LessonData) {
        val checkMarkColor = if (lessonData.isCompleted) {
            MaterialTheme.colorScheme.primary
        } else MaterialTheme.colorScheme.secondary
        Row(
            modifier = Modifier.padding(top = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(checkMarkColor, CircleShape)
                    .padding(4.dp),
            ) {
                Image(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(id = R.drawable.ic_check_mark),
                    contentDescription = "check_mark_icon"
                )
            }
            Text(
                text = lessonData.name,
                modifier = Modifier.padding(start = 12.dp),
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Text(
            text = lessonData.description,
            modifier = Modifier.padding(top = 10.dp),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

