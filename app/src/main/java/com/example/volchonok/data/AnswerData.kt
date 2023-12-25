package com.example.volchonok.data

data class AnswerData(
    val id: Int,
    val text: String,
    val isCorrect: Boolean,
    val explanation: String,
    var wasChooseByUser: Boolean
)