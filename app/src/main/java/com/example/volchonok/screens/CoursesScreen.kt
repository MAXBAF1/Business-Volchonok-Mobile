package com.example.volchonok.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.vidgets.StatusBar

class CoursesScreen(
    private val userData: UserData,
    private val coursesList: Iterable<CourseData>,
    private val toCourseInfoScreen: (CourseData) -> Unit
) {
    @Composable
    fun Create() {
        Column(
            Modifier
                .padding(30.dp, 15.dp)
                .fillMaxSize()
        ) {
            StatusBar(userData).Create()
            Text(
                text = stringResource(id = R.string.course_greeting),
                modifier = Modifier.padding(top = 30.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(id = R.string.what_learn),
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.labelSmall,
            )
            CoursesList(coursesList)
        }
    }

    @Composable
    private fun CoursesList(coursesList: Iterable<CourseData>) {
        Column(modifier = Modifier.padding(top = 20.dp)) {
            coursesList.forEach {
                CourseCard(courseData = it)
            }
        }
    }

    @Composable
    private fun CourseCard(courseData: CourseData) {
        ElevatedCard(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.course_background),
                    contentDescription = "course_background",
                    modifier = Modifier.size(328.dp, 162.dp)
                )
                Column(modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
                    Text(
                        text = courseData.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = courseData.description,
                        modifier = Modifier.padding(top = 10.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Button(
                        onClick = { toCourseInfoScreen(courseData) },
                        modifier = Modifier.padding(top = 30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_up_btn),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium,
                            //modifier = Modifier.padding(28.dp, 12.dp),
                        )
                    }
                }
            }
        }
    }

}