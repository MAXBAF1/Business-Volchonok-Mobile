package com.example.volchonok.screens

import android.content.Context
import android.widget.ScrollView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.ModuleData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.vidgets.Greeting
import com.example.volchonok.screens.vidgets.ModuleCard
import com.example.volchonok.screens.vidgets.ReviewCard
import com.example.volchonok.screens.vidgets.TopAppBar

class CourseInfoScreen(
    private val userData: UserData,
    private val courseData: CourseData
) {
    @Composable
    fun Create() {
        Column(
            Modifier
                .padding(30.dp, 15.dp)
                .fillMaxSize()
        ) {
            TopAppBar(userData).Create()
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Greeting()
                ModulesList()
                Description()
                Reviews()
            }
        }
    }

    @Composable
    fun ModulesList() {
        Column(modifier = Modifier.padding(top = 15.dp)) {
            courseData.modules.forEach { ModuleCard(it).Add() }
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
            text = stringResource(id = R.string.reviews),
            modifier = Modifier.padding(top = 30.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        LazyRow() {
            itemsIndexed(courseData.reviews) {_, item ->
                ReviewCard(item).Add()
            }
        }
    }
}