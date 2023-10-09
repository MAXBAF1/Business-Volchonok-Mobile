package com.example.volchonok.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.vidgets.StatusBar

class CourseInfoScreen(private val userData: UserData, private val courseData: CourseData) {
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
        }
    }

}