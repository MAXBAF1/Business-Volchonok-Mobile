package com.example.volchonok.screens

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.RemoteInfoStorage
import com.example.volchonok.RemoteInfoStorage.getCoursesData
import com.example.volchonok.RemoteInfoStorage.getUserData
import com.example.volchonok.RemoteInfoStorage.setUserData
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.ModuleData
import com.example.volchonok.data.UserData
import com.example.volchonok.enums.CourseDataAccessLevel
import com.example.volchonok.screens.vidgets.cards.ModuleCard
import com.example.volchonok.screens.vidgets.cards.ReviewCard
import com.example.volchonok.screens.vidgets.others.Greeting
import com.example.volchonok.screens.vidgets.others.TopAppBar
import com.example.volchonok.services.CheckUserToken
import com.example.volchonok.services.UserInfoService

class CourseInfoScreen(
    private var courseData: CourseData,
    private val toLessonsScreen: (ModuleData) -> Unit,
    private val toProfile: () -> Unit
) {
    private var userData: UserData? = null

    @Composable
    fun Create() {
        val context = LocalContext.current
        userData = getUserData()
        if (userData == null) {
            setUserData(UserInfoService(context).execute().get())
        }

        Column(
            Modifier.fillMaxSize()
        ) {
            TopAppBar(userData, toProfile).Create()

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Column(Modifier.padding(30.dp, 0.dp)) {
                    Greeting("${userData?.surname} ${userData?.firstname}")
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
        val descriptionParts = courseData.description.split("#_#")
        Text(
            text = stringResource(id = R.string.about),
            modifier = Modifier.padding(top = 30.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = descriptionParts[0],
            modifier = Modifier.padding(top = 15.dp),
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = stringResource(id = R.string.why_you),
            modifier = Modifier.padding(top = 30.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = if (descriptionParts.size > 1) descriptionParts[1] else "",
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