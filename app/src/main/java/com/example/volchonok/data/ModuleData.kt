package com.example.volchonok.data

import com.example.volchonok.interfaces.ILesson

data class ModuleData(
    var id: Int,
    val name: String,
    val description: String,
    val lessonNotes: List<NoteData>,
    val lessonTests: List<TestData>,
)
