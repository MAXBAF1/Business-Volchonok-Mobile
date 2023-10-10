package com.example.volchonok.data

data class LessonData(
    val name: String,
    val description: String,
    val durationTime: String,
    val isCompleted: Boolean = false
)
