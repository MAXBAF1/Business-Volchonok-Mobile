package com.example.volchonok.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.MessageData
import com.example.volchonok.data.NoteData
import com.example.volchonok.enums.AuthorType
import com.example.volchonok.enums.MessageType
import com.example.volchonok.screens.vidgets.others.DefaultButton
import com.example.volchonok.screens.vidgets.others.YoutubeVideoPlayer
import com.example.volchonok.services.CompleteCourseService
import com.example.volchonok.services.enums.ServiceStringValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteScreen(
    private val note: NoteData,
    private val onCompleteBtn: () -> Boolean,
) {
    private val msgStates = mutableStateListOf<Boolean>()
    private var sendBtnShowed = mutableStateOf(false)

    @Composable
    fun Create() {
        note.messages.forEachIndexed { i, message ->
            if (i == 0 && message.author == AuthorType.WOLF) msgStates.add(true)
            else msgStates.add(false)
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
        LazyColumn(modifier = modifier) {
            itemsIndexed(note.messages) {i, message ->
                if (note.isCompleted || msgStates[i]) {

                    Message(message, i == 0, i == msgStates.size - 1)

                    val nextMsgIndex = msgStates.lastIndexOf(true) + 1
                    if (nextMsgIndex < note.messages.size) {
                        val nextMsgAuthor = note.messages[nextMsgIndex].author
                        sendBtnShowed.value = nextMsgAuthor == AuthorType.STUDENT
                        msgStates[nextMsgIndex] = nextMsgAuthor == AuthorType.WOLF
                    }
                }
            }
        }
    }

    @Composable
    private fun SendMessageOrCompleteBtn() {
        val ctx = LocalContext.current
        val rcs = rememberCoroutineScope()
        val nextMsgIndex = msgStates.lastIndexOf(true) + 1

        if (!note.isCompleted && sendBtnShowed.value) {
            SendMessageBtn(note.messages[nextMsgIndex].text) {
                sendBtnShowed.value = false
                msgStates[nextMsgIndex] = true
            }
        }

        if (note.isCompleted || nextMsgIndex >= note.messages.size) {
            DefaultButton(
                text = stringResource(id = if (note.isCompleted) R.string.read else R.string.complete).uppercase(),
                onClick = {
                    if (!onCompleteBtn.invoke()) return@DefaultButton

                    rcs.launch {
                        withContext(Dispatchers.IO) {
                            CompleteCourseService(
                                ServiceStringValue.COMPLETED_NOTES_REQUEST_ADDRESS,
                                ctx
                            ).execute(note.id).get()
                        }
                    }
                }
            )
        }
    }

    @Composable
    private fun Message(message: MessageData, isFirst: Boolean = false, isLast: Boolean = false) {
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
                    MessageType.VIDEO -> VideoMessage(message)
                    MessageType.PICTURE -> {}
                }
            }
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
    private fun VideoMessage(message: MessageData) {
        Column {
            YoutubeVideoPlayer(message.url)
            if (message.text.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = message.text,
                    style = MaterialTheme.typography.titleSmall
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
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}