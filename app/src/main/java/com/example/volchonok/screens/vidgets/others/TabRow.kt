package com.example.volchonok.screens.vidgets.others

import androidx.compose.animation.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
class TabRow(private val pagerState: PagerState, private val tabList: List<String>) {
    private var coroutineScope: CoroutineScope? = null

    @Composable
    fun Create() {
        coroutineScope = rememberCoroutineScope()
        val tabIndex = pagerState.currentPage
        Box(modifier = Modifier.padding(start = 30.dp, top = 30.dp, end = 30.dp)) {
            TabRow(modifier = Modifier
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .padding(6.dp),
                containerColor = MaterialTheme.colorScheme.background,
                selectedTabIndex = tabIndex,
                indicator = { },
                divider = { }) {
                tabList.forEachIndexed { index, text ->
                    Tab(pagerState, index, text)
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun Tab(pagerState: PagerState, index: Int, text: String) {
        val selected = MaterialTheme.colorScheme.primary
        val unselected = MaterialTheme.colorScheme.background
        val containerColor = remember { Animatable(selected) }
        LaunchedEffect(key1 = pagerState.currentPage == index) {
            containerColor.animateTo(if (pagerState.currentPage == index) selected else unselected)
        }
        Tab(
            modifier = Modifier
                .clip(CircleShape)
                .height(40.dp)
                .background(containerColor.value, CircleShape),
            selected = pagerState.currentPage == index,
            onClick = {
                coroutineScope?.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            text = { Text(text = text, style = MaterialTheme.typography.labelSmall) },
            selectedContentColor = MaterialTheme.colorScheme.onPrimary,
            unselectedContentColor = selected
        )
    }
}