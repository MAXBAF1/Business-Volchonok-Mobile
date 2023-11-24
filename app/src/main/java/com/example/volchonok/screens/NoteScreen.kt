package com.example.volchonok.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.MessageData
import com.example.volchonok.data.NoteData
import com.example.volchonok.enums.AuthorType
import com.example.volchonok.enums.MessageType
import com.example.volchonok.screens.vidgets.others.DefaultButton
import kotlinx.coroutines.delay

class NoteScreen(
    private val noteData: NoteData,
    private val onCompleteBtn: () -> Unit,
) {
    private val note = NoteData( // Тестовые данные
        0, "Лекция", "Описание", "30", false, listOf(
            MessageData(
                "Let’s get lunch! How about pizza? \uD83C\uDF55",
                AuthorType.Student,
                MessageType.Text,
                ""
            ),
            MessageData(
                "That sounds great! I’m in. What time works for you?",
                AuthorType.Wolf,
                MessageType.Text,
                ""
            ),
            MessageData(
                "Let’s say 12pm if it’s fine with you?", AuthorType.Student, MessageType.Text, ""
            ),
            MessageData(
                "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the lang",
                AuthorType.Wolf,
                MessageType.Text,
                ""
            ),
        )
    )


    private val messageStates = List(note.messages.size) { mutableStateOf(false) }
    private var sendBtnShowed = mutableStateOf(true)
    private var completeBtnShowed = mutableStateOf(false)
    private var lineLength = 0.dp

    @Composable
    fun Create() {
        val screenWidth = LocalConfiguration.current.screenWidthDp
        lineLength = (screenWidth * 0.75).dp

        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            MessageList(modifier = Modifier.weight(1f))
            SendMessageOrCompleteBtn()
        }
    }

    @Composable
    private fun MessageList(modifier: Modifier) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            note.messages.forEachIndexed { i, message ->
                if (messageStates[i].value) {
                    val isLast = i == messageStates.size - 1
                    when (message.author) {
                        AuthorType.Student -> Message(message, i == 0, isLast)
                        AuthorType.Wolf -> {
                            var showText by remember { mutableStateOf(false) }

                            LaunchedEffect(showText) {
                                delay(1000)
                                showText = true
                            }
                            if (showText) {
                                Message(message, i == 0, isLast)
                                if (isLast) completeBtnShowed.value = true
                                else sendBtnShowed.value = true
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SendMessageOrCompleteBtn() {
        var sendMessageIndex by remember { mutableIntStateOf(0) }
        if (sendMessageIndex < note.messages.size && sendBtnShowed.value) {
            SendMessageBtn(note.messages[sendMessageIndex].text) {
                sendBtnShowed.value = false
                messageStates[sendMessageIndex++].value = true
                if (sendMessageIndex < note.messages.size) {
                    messageStates[sendMessageIndex++].value = true
                }
            }
        } else if (completeBtnShowed.value) {
            DefaultButton(
                text = stringResource(id = R.string.complete).uppercase(), onClick = onCompleteBtn
            )
        }
    }

    @Composable
    private fun Message(message: MessageData, isFirst: Boolean = false, isLast: Boolean = false) {
        val alignment: Alignment
        val backgroundShape: Shape
        val backgroundColor: Color
        val textColor: Color

        if (message.author == AuthorType.Student) {
            alignment = Alignment.CenterEnd
            backgroundShape = RoundedCornerShape(20.dp, 20.dp, 2.dp, 20.dp)
            backgroundColor = MaterialTheme.colorScheme.primary
            textColor = MaterialTheme.colorScheme.onPrimary
        } else {
            alignment = Alignment.CenterStart
            backgroundShape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 2.dp)
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
            textColor = MaterialTheme.colorScheme.onSecondaryContainer
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isFirst) 30.dp else 20.dp, bottom = if (isLast) 30.dp else 0.dp),
            contentAlignment = alignment
        ) {
            Box(
                modifier = Modifier
                    .background(backgroundColor, backgroundShape)
                    .padding(10.dp)
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.widthIn(max = lineLength),
                    style = MaterialTheme.typography.labelLarge,
                    color = textColor
                )
            }
        }
    }

    @Composable
    private fun SendMessageBtn(text: String, onClick: () -> Unit) {
        OutlinedButton(
            modifier = Modifier,
            onClick = onClick,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
        ) {
            Text(
                text = text,
                modifier = Modifier.widthIn(max = lineLength),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}