package com.example.volchonok.screens.vidgets.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.CourseData
import com.example.volchonok.screens.vidgets.others.CompletedLessonsCntText
import kotlin.math.roundToInt

class CourseProgressCard(private val coursesList: List<CourseData>) {
    private var courseProgress = 0.0f

    @Composable
    fun Create() {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            itemsIndexed(coursesList) { i, course ->
                CreateCard(
                    if (coursesList.size == 1) Modifier.fillParentMaxWidth() else Modifier.width(300.dp),
                    course,
                    i == 0,
                    i == coursesList.size - 1
                )
            }
        }
    }

    @Composable
    private fun CreateCard(
        modifier: Modifier,
        course: CourseData,
        isFirst: Boolean = false,
        isLast: Boolean = false
    ) {
        Card(
            modifier = modifier
                .padding(start = if (isFirst) 30.dp else 15.dp, end = if (isLast) 30.dp else 0.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(Modifier.padding(15.dp, 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = course.name,
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    val lessonsCtnText =
                        "${getCompletedModulesCnt(course)}/${course.modules.size} ${
                            stringResource(id = R.string.modules_cnt)
                        }"
                    CompletedLessonsCntText(text = lessonsCtnText)
                }
                Text(
                    text = "${getProgressPercentage(course)}%",
                    modifier = Modifier.padding(top = 30.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                CustomLinearProgressIndicator(courseProgress)
            }
        }
    }

    private fun getCompletedModulesCnt(course: CourseData): Int {
        var cnt = 0
        course.modules.forEach { moduleData ->
            if (moduleData.lessonNotes.count { it.isCompleted } == moduleData.lessonNotes.size || moduleData.lessonTests.count { it.isCompleted } == moduleData.lessonTests.size) {
                cnt++
            }
        }

        return cnt
    }

    private fun getProgressPercentage(course: CourseData): Int {
        val allLessonsCount = course.modules.sumOf {
            it.lessonNotes.size + it.lessonTests.size
        }
        val completedLessonsCount = course.modules.sumOf { moduleData ->
            moduleData.lessonNotes.count { it.isCompleted } + moduleData.lessonTests.count { it.isCompleted }
        }
        courseProgress = if (allLessonsCount != 0) {
            completedLessonsCount.toFloat() / allLessonsCount
        } else 0F

        return (courseProgress * 100).roundToInt()
    }

    @Composable
    private fun CustomLinearProgressIndicator(progress: Float) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .height(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
            )
        }
    }
}