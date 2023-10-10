package com.example.volchonok.data

data class CourseData(
    val name: String,
    val modules: List<ModuleData>,
    val description: String,
    val whyYouDescription: String,
    val reviews: List<ReviewData>
)
