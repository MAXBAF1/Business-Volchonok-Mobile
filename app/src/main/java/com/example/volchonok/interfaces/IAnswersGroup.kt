package com.example.volchonok.interfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.volchonok.data.AnswerData

interface IAnswersGroup {
    val answers: SnapshotStateList<Boolean>
    val list: List<AnswerData>

    @Composable
    fun Create()
}