package com.example.volchonok.data

import com.example.volchonok.interfaces.ILesson

data class NoteData(
    override var id: Int,
    override val name: String, //
    override val description: String, //
    override val durationTime: String, //
    override var isCompleted: Boolean,
    val messages: List<MessageData> //
) : ILesson
