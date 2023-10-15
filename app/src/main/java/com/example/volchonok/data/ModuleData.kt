package com.example.volchonok.data

data class ModuleData(
    val name: String,
    val description: String,
    val lessonNotes: List<LessonData>,
    val lessonTests: List<LessonData>,
)
