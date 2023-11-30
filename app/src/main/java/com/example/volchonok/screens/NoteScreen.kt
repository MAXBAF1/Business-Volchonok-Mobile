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
import androidx.compose.ui.draw.clip
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
import com.example.volchonok.screens.vidgets.others.YoutubeVideoPlayer
import kotlinx.coroutines.delay

class NoteScreen(
    private val noteData: NoteData,
    private val onCompleteBtn: () -> Unit,
) {
    private val note = NoteData( // Тестовые данные
        0, "Лекция", "Описание", "30", false, noteData.messages
//        listOf(
//            MessageData(
//                "Let’s get lunch! How about pizza? \uD83C\uDF55",
//                AuthorType.STUDENT,
//                MessageType.TEXT,
//                ""
//            ),
//            MessageData(
//                "That sounds great! I’m in. What time works for you?",
//                AuthorType.WOLF,
//                MessageType.TEXT,
//                ""
//            ),
//            MessageData(
//                "Let’s say 12pm if it’s fine with you?", AuthorType.STUDENT, MessageType.TEXT, ""
//            ),
//            MessageData(
//                "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the lang",
//                AuthorType.WOLF,
//                MessageType.TEXT,
//                ""
//            ),
//            MessageData(
//                "Jubilee Gardens", AuthorType.WOLF, MessageType.VIDEO, "Jrg9KxGNeJY"
//            ),
//        )

    )


    private val messageStates = List(note.messages.size) { mutableStateOf(false) }
    private var sendMessageIndex = mutableIntStateOf(0)
    private var sendBtnShowed = mutableStateOf(!note.isCompleted)
    private var completeBtnShowed = mutableStateOf(note.isCompleted)

    @Composable
    fun Create() {
        while (sendMessageIndex.intValue < note.messages.size && note.messages[sendMessageIndex.intValue].author == AuthorType.WOLF) {
            messageStates[sendMessageIndex.intValue++].value = true
        }

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
                if (note.isCompleted) {
                    MessageManager(message, i == 0, i == messageStates.size - 1)
                } else if (messageStates[i].value) {
                    val isLast = i == messageStates.size - 1
                    if (!isLast && note.messages[i + 1].author != AuthorType.STUDENT) {
                        sendBtnShowed.value = false
                    }

                    var showText by remember { mutableStateOf(false) }
                    if (i == 0 || message.author == AuthorType.STUDENT) {
                        MessageManager(message, i == 0, isLast)
                    } else {
                        LaunchedEffect(showText) {
                            delay(1000)
                            showText = true
                        }
                        if (showText) {
                            MessageManager(message, isLast = isLast)
                        }
                    }
                    if (showText) {
                        if (isLast) completeBtnShowed.value = true
                        else sendBtnShowed.value = note.messages[i + 1].author == AuthorType.STUDENT
                    }
                }
            }
        }
    }

    @Composable
    private fun MessageManager(
        message: MessageData, isFirst: Boolean = false, isLast: Boolean = false
    ) {
        val alignment: Alignment
        val backgroundShape: Shape
        val backgroundColor: Color

        if (message.author == AuthorType.STUDENT) {
            backgroundShape = RoundedCornerShape(20.dp, 20.dp, 2.dp, 20.dp)
            alignment = Alignment.CenterEnd
            backgroundColor = MaterialTheme.colorScheme.primary
        } else {
            alignment = Alignment.CenterStart
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
            backgroundShape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 2.dp)
        }
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val lineLength = (screenWidth * 0.75).dp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isFirst) 30.dp else 15.dp, bottom = if (isLast) 30.dp else 0.dp),
            contentAlignment = alignment
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = lineLength)
                    .clip(backgroundShape)
                    .background(backgroundColor)
            ) {
                when (message.type) {
                    MessageType.TEXT -> TextMessage(message)
                    MessageType.VIDEO -> Video(message)
                    MessageType.PICTURE -> {}
                }
            }
        }
    }

    @Composable
    private fun Video(message: MessageData) {
        Column {
            YoutubeVideoPlayer(message.url)
            Text(
                modifier = Modifier.padding(10.dp),
                text = message.text,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }

    @Composable
    private fun SendMessageOrCompleteBtn() {
        if (sendMessageIndex.intValue < note.messages.size && sendBtnShowed.value) {
            SendMessageBtn(note.messages[sendMessageIndex.intValue].text) {
                sendBtnShowed.value = false
                messageStates[sendMessageIndex.intValue++].value = true
            }
        } else if (completeBtnShowed.value) {
            DefaultButton(
                text = stringResource(id = if (note.isCompleted) R.string.read else R.string.complete).uppercase(),
                onClick = onCompleteBtn
            )
        }
    }

    @Composable
    private fun TextMessage(message: MessageData) {
        val textColor: Color = if (message.author == AuthorType.STUDENT) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSecondaryContainer
        }

        Text(
            modifier = Modifier.padding(10.dp),
            text = message.text,
            style = MaterialTheme.typography.labelLarge,
            color = textColor
        )
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
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}