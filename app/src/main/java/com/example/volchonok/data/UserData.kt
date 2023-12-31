package com.example.volchonok.data

data class UserData(
    val id: Int,
    val login: String,
    var phone: String,
    val firstname: String,
    val surname: String,
    val middlename: String,
    var avatar: Int,
    val level: Int,
    var email: String,
    val address: String,
    val class_grade: String,
    val coins: Int
) {
    constructor() : this(1,
        "Логин не указан",
        "Телефон не указан",
        "Имя не указано",
        "",
        "",
        0,
        0,
        "Почта не указана",
        "Адрес не указан",
        "Класс не указан",
        0)
}
