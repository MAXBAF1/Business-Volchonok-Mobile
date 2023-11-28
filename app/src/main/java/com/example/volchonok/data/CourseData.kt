package com.example.volchonok.data

data class CourseData(
    var id: Int,
    var name: String,
    var modules: List<ModuleData>,
    var description: String,
    var reviews: List<ReviewData>
) {
    constructor() : this(0, "", mutableListOf(), "", mutableListOf())
}
