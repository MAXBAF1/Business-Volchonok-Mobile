package com.example.volchonok.data

data class UserData(
    val id: Int,
    val login: String,
    val firstname: String,
    val surname: String,
    val middlename: String,
    var avatar: String,
    val level: Int,
    var phone: String,
    var email: String,
    val address: String,
    val class_grade: Int,
    val coins: Int
) {
    constructor() : this(1,
        "",
        "",
        "",
        "",
        "",
        0,
        "0",
        "Почта не указана",
        "Адрес не указан",
        0,
        0)
}
