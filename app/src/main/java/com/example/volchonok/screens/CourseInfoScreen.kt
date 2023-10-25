package com.example.volchonok.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.ModuleData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.vidgets.Greeting
import com.example.volchonok.screens.vidgets.cards.ModuleCard
import com.example.volchonok.screens.vidgets.cards.ReviewCard
import com.example.volchonok.screens.vidgets.TopAppBar

class CourseInfoScreen(
    private val userData: UserData,
    private val courseData: CourseData,
    private val toLessonsScreen: (ModuleData) -> Unit,
    private val toProfile: () -> Unit
) {
    @Composable
    fun Create() {
        Column(
            Modifier.fillMaxSize()
        ) {
            TopAppBar(userData, toProfile).Create()

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Column(Modifier.padding(30.dp, 0.dp)) {
                    Greeting(userData.name)
                    ModulesList()
                    Description()
                }
                Column(Modifier.padding(bottom = 15.dp, top = 30.dp)) {
                    Reviews()
                }
            }
        }
    }

    @Composable
    fun ModulesList() {
        Column(modifier = Modifier.padding(top = 15.dp)) {
            courseData.modules.forEach { ModuleCard(it, toLessonsScreen).Add() }
        }
    }

    @Composable
    private fun Description() {
        Text(
            text = stringResource(id = R.string.about),
            modifier = Modifier.padding(top = 30.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = courseData.description,
            modifier = Modifier.padding(top = 15.dp),
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = stringResource(id = R.string.why_you),
            modifier = Modifier.padding(top = 30.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = courseData.whyYouDescription,
            modifier = Modifier.padding(top = 15.dp),
            style = MaterialTheme.typography.labelSmall,
        )
    }

    @Composable
    private fun Reviews() {
        Text(
            modifier = Modifier.padding(start = 30.dp),
            text = stringResource(id = R.string.reviews),
            style = MaterialTheme.typography.titleMedium,
        )
        LazyRow(modifier = Modifier.padding(top = 15.dp)) {
            itemsIndexed(courseData.reviews) { i, item ->
                if (i == 0) ReviewCard(item, true).Add()
                else ReviewCard(item).Add()
            }
        }
    }
}