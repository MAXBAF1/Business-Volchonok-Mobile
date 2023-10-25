package com.example.volchonok.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.LessonData
import com.example.volchonok.data.ModuleData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.vidgets.CompletedLessonsCntText
import com.example.volchonok.screens.vidgets.InfoHeader
import com.example.volchonok.screens.vidgets.TabRow
import com.example.volchonok.screens.vidgets.TopAppBar
import com.example.volchonok.screens.vidgets.cards.LessonCard

class LessonsScreen(
    private val userData: UserData,
    private val moduleData: ModuleData,
    private val onBackClick: () -> Unit,
    private val toProfile: () -> Unit,
    private val toNoteScreen: (LessonData) -> Unit,
    private val toTestScreen: (LessonData) -> Unit,
) {
    @Composable
    fun Create() {
        Column {
            TopAppBar(userData, toProfile, true, onBackClick).Create()
            val completedLessonCnt = moduleData.lessonNotes.count { it.isCompleted }
            val lessonsCtnText = "${completedLessonCnt}/${moduleData.lessonNotes.size} ${
                stringResource(id = R.string.lessons_cnt)
            }"
            InfoHeader(moduleData.name, moduleData.description, lessonsCtnText)
            TabLayout()
        }
    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TabLayout() {
        val tabList =
            listOf(stringResource(id = R.string.notes), stringResource(id = R.string.tests))
        val pagerState = rememberPagerState { tabList.size }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(20.dp))
        ) {
            TabRow(pagerState, tabList).Create()
            HorizontalPager(pagerState)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun HorizontalPager(pagerState: PagerState) {
        HorizontalPager(state = pagerState) { index ->
            val list = when (index) {
                0 -> moduleData.lessonNotes
                1 -> moduleData.lessonTests
                else -> moduleData.lessonNotes
            }
            LessonsList(list)
        }
    }

    @Composable
    private fun LessonsList(list: List<LessonData>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, top = 15.dp, end = 30.dp)
        ) {
            itemsIndexed(list) { i, lesson ->
                when (i) {
                    0 -> LessonCard(lesson, toNoteScreen, toTestScreen, true).Create()
                    list.size - 1 -> LessonCard(
                        lesson, toNoteScreen, toTestScreen, isLast = true
                    ).Create()
                    else -> LessonCard(lesson, toNoteScreen, toTestScreen).Create()
                }
            }
        }
    }
}