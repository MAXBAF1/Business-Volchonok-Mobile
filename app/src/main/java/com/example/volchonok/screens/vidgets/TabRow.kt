package com.example.volchonok.screens.vidgets

import androidx.compose.animation.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
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
                .padding(6.dp)
                .clip(RoundedCornerShape(20.dp)),
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
        val primary = MaterialTheme.colorScheme.primary
        val onPrimary = MaterialTheme.colorScheme.onPrimary
        val backgroundColor = remember { Animatable(primary) }
        LaunchedEffect(key1 = pagerState.currentPage == index) {
            backgroundColor.animateTo(if (pagerState.currentPage == index) primary else onPrimary)
        }
        Tab(
            modifier = Modifier
                .height(40.dp)
                .background(
                    backgroundColor.value, RoundedCornerShape(100)
                )
                .border(0.dp, Color.Transparent, CircleShape),
            selected = pagerState.currentPage == index,
            onClick = {
                coroutineScope?.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            text = { Text(text = text, style = MaterialTheme.typography.labelSmall) },
            selectedContentColor = onPrimary,
            unselectedContentColor = MaterialTheme.colorScheme.secondary
        )
    }
}