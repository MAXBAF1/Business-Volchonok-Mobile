package com.example.volchonok.data

import com.example.volchonok.enums.LessonType

data class
LessonData(
    val name: String,
    val description: String,
    val durationTime: String,
    val lessonType: LessonType,
    val isCompleted: Boolean = false
)
