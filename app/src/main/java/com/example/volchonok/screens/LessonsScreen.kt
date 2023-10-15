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
import com.example.volchonok.screens.vidgets.TabRow
import com.example.volchonok.screens.vidgets.TopAppBar
import com.example.volchonok.screens.vidgets.cards.LessonCard

class LessonsScreen(
    private val userData: UserData,
    private val moduleData: ModuleData,
    private val onBackClick: () -> Unit
) {
    @Composable
    fun Create() {
        Column {
            TopAppBar(userData, true, onBackClick).Create()
            ModuleInfo()
            TabLayout()
        }
    }

    @Composable
    private fun ModuleInfo() {
        Card(
            shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(Modifier.padding(30.dp, 15.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = moduleData.name,
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    CompletedLessonsCntText(moduleData = moduleData)
                }
                Text(
                    text = moduleData.description,
                    modifier = Modifier.padding(top = 15.dp),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
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
                .padding(start = 30.dp, top = 30.dp, end = 30.dp)
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
                .padding(top = 15.dp)
        ) {
            itemsIndexed(list) { i, lesson ->
                when (i) {
                    0 -> LessonCard(lesson, true).Create()
                    list.size - 1 -> LessonCard(lesson, isLast = true).Create()
                    else -> LessonCard(lesson).Create()
                }
            }
        }
    }
}