package com.example.volchonok.data

data class UserData(
    val id: Int,
    val login: String,
    val firstname: String,
    val surname: String,
    val middlename: String,
    val avatar: String,
    val level: Int,
    val email: String,
    val address: String,
    val class_grade: String,
    val coins: Int
) {
    constructor() : this(1, "", "", "", "", "", 0, "", "", "", 0)
}
