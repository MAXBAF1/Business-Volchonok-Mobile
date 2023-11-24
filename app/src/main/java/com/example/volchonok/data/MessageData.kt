package com.example.volchonok.data

import com.example.volchonok.enums.AuthorType
import com.example.volchonok.enums.MessageType

data class MessageData(
    val text: String,
    val author: AuthorType,
    val type: MessageType,
    val url: String // хз какой тип мб Url или Uri
)
