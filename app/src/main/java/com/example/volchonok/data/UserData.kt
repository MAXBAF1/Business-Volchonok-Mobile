package com.example.volchonok.data

data class UserData(
    val id: Int,
    val login: String,
    val firstname: String,
    val surname: String,
    val middlename: String,
    val avatar: Int,
    val coins: Int,
    val level: String
)
