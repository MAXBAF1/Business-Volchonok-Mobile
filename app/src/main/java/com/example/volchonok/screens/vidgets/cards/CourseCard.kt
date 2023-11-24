package com.example.volchonok.screens.vidgets.cards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.CourseData

class CourseCard(
    private val courseData: CourseData, private val toCourseInfoScreen: (CourseData) -> Unit
) {
    @Composable
    fun Add() {
        ElevatedCard(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            modifier = Modifier
                .padding(top = 15.dp)
                .fillMaxWidth()
                .height(185.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Box {
                val ovalColor = MaterialTheme.colorScheme.onPrimaryContainer
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    drawOval(
                        color = ovalColor,
                        topLeft = Offset(x = 195.dp.toPx(), y = -80.dp.toPx()),
                        size = Size(width = 560.dp.toPx(), height = 430.dp.toPx()),
                    )
                }
                Image(
                    modifier = Modifier
                        .size(280.dp)
                        .offset(x = 130.dp),
                    painter = painterResource(id = R.drawable.mic_wolf),
                    contentDescription = "mic_wolf image",
                    alignment = Alignment.TopEnd,
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(20.dp)) {
                    AddNameAndDescription()
                    AddSignUpBtn()
                }
            }
        }
    }

    @Composable
    private fun AddNameAndDescription() {
        Text(
            text = courseData.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = courseData.description,
            modifier = Modifier
                .padding(top = 10.dp)
                .widthIn(max = 160.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
        )
    }

    @Composable
    private fun AddSignUpBtn() {
        Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
            Button(
                onClick = { toCourseInfoScreen(courseData) },
                modifier = Modifier.padding(top = 30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(id = R.string.continue_text),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                    //modifier = Modifier.padding(28.dp, 12.dp),
                )
            }
        }
    }
}