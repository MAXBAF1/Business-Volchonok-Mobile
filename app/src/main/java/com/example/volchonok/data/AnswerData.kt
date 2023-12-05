package com.example.volchonok.data

data class AnswerData(
    val text: String,
    val isCorrect: Boolean,
    var wasChooseByUser: Boolean
)