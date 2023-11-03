package com.example.volchonok.interfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.volchonok.data.AnswerData

interface IAnswersGroup {
    val list: List<AnswerData>
    val isBtnEnabled: MutableState<Boolean>

    @Composable
    fun Create()

    fun getAnswers(): SnapshotStateList<Boolean>
}