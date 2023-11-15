package com.example.volchonok.data

data class UserData(
    val id: Int,
    val login: String,
    val firstname: String,
    val surname: String,
    val email: String,
    val coins: Int
) {
    constructor(): this(0, "", "", "", "", 0)
}
