package com.example.volchonok.data

data class CourseData(
    var name: String,
    var modules: List<ModuleData>,
    var description: String,
    var reviews: List<ReviewData>
)
