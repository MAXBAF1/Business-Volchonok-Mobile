package com.example.volchonok.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.NoteData
import com.example.volchonok.enums.MessageType
import com.example.volchonok.screens.vidgets.others.DefaultButton

class NoteScreen(
    private val noteData: NoteData,
    private val onCompleteBtn: () -> Unit,
) {
    private val note = NoteData(
        0, "Лекция", "Описание", "30", false, listOf(
            "Let’s get lunch! How about pizza? \uD83C\uDF55",
            "That sounds great! I’m in. What time works for you?",
            "Let’s say 12pm if it’s fine with you?",
            "Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the langLearn the basics of the language: make new friends, plan a family dinner, go shopping and much more!Learn the basics of the language: make new friends, plan a family dinner, go shopping and much more!мLearn the basics of the lang"
        )
    )
    private val messageStates = List(note.text.size) { mutableStateOf(false) }
    private val lineLength = 200.dp

    @Composable
    fun Create() {
        var sendMessageIndex by remember { mutableIntStateOf(0) }
        Column(Modifier.padding(start = 30.dp, end = 30.dp)) {
            LazyColumn(modifier = Modifier) {
                itemsIndexed(note.text) { i, text ->
                    if (messageStates[i].value) {
                        if (i % 2 == 0) {
                            Message(text, MessageType.Student, i == 0)
                        } else Message(text, MessageType.Wolf)
                    }
                }
            }
            if (sendMessageIndex < note.text.size) {
                SendMessageBtn(note.text[sendMessageIndex]) {
                    messageStates[sendMessageIndex++].value = true
                    if (sendMessageIndex < note.text.size)
                        messageStates[sendMessageIndex++].value = true
                }
            } else CompleteBtn()
        }
    }

    @Composable
    private fun Message(text: String, messageType: MessageType, isFirst: Boolean = false) {
        val alignment: Alignment
        val backgroundColor: Color
        val textColor: Color

        if (messageType == MessageType.Wolf) {
            alignment = Alignment.CenterStart
            backgroundColor = MaterialTheme.colorScheme.onBackground
            textColor = MaterialTheme.colorScheme.background
        } else {
            alignment = Alignment.CenterEnd
            backgroundColor = MaterialTheme.colorScheme.primary
            textColor = MaterialTheme.colorScheme.onPrimary
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isFirst) 30.dp else 20.dp),
            contentAlignment = alignment
        ) {
            Box(
                modifier = Modifier
                    .background(backgroundColor, RoundedCornerShape(20.dp))
                    .padding(10.dp), contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = text,
                    modifier = Modifier.widthIn(max = lineLength),
                    style = MaterialTheme.typography.labelLarge,
                    color = textColor
                )
            }
        }
    }

    @Composable
    private fun SendMessageBtn(text: String, onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
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

    @Composable
    private fun CompleteBtn() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            DefaultButton(
                text = stringResource(id = R.string.complete).uppercase(), onClick = onCompleteBtn
            )
        }
    }
}