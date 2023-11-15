package com.example.volchonok.data

import com.example.volchonok.interfaces.ILesson

data class NoteData(
    var id: Int,
    override val name: String, //
    override val description: String, //
    override val durationTime: String, //
    override val isCompleted: Boolean,
    val text: String //
) : ILesson
