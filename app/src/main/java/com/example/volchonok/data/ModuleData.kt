package com.example.volchonok.data

import com.example.volchonok.interfaces.ILesson

data class ModuleData(
    val name: String,
    val description: String,
    val lessonNotes: List<ILesson>,
    val lessonTests: List<ILesson>,
)
