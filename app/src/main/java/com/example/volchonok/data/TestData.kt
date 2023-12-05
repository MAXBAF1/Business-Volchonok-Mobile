package com.example.volchonok.data

import com.example.volchonok.interfaces.ILesson

data class TestData(
    override var id: Int,
    override val name: String,
    override val description: String,
    override val durationTime: String,
    override var isCompleted: Boolean,
    val questions: List<QuestionData>
) : ILesson
