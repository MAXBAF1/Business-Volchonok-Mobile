package com.example.volchonok.data

data class ModuleData(
    val name: String,
    val lessonNotes: List<LessonData>,
    val lessonTests: List<LessonData>,
)
