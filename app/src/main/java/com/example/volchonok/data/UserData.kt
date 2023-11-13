package com.example.volchonok.data

data class UserData(
    val id: Int,
    val login: String,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val avatar: Int,
    val coins: Int,
    val level: String?
)
