package com.example.volchonok.data

data class QuestionData(
    val text: String,
    val answers: List<AnswerData>,
    val rightAnswerExplanation: String //объяснение правильного ответа на вопрос
)

