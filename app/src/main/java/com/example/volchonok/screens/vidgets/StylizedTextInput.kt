package com.example.volchonok.screens.vidgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.volchonok.R

class StylizedTextInput(
    private val hint: String,
    private val titleText: String = "",
    private val addTitle: Boolean = true,
    private val isLast: Boolean = false
) {
    @Composable
    fun Create() {
        if (addTitle) CreateTitle()
        CreateTextField()
    }

    @Composable
    fun CreateTitle() {
        Text(
            modifier = Modifier.padding(top = 30.dp),
            text = titleText,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CreateTextField() {
        var text by remember { mutableStateOf("") }
        val onBackgroundColor = MaterialTheme.colorScheme.onBackground
        val secondaryColor = MaterialTheme.colorScheme.secondary
        var borderColor by remember { mutableStateOf(secondaryColor) }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = if (isLast) 15.dp else 0.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, borderColor, RoundedCornerShape(20.dp))
                .onFocusChanged {
                    borderColor = when {
                        it.isFocused -> onBackgroundColor
                        else -> secondaryColor
                    }
                },
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text(
                    text = hint, style = MaterialTheme.typography.labelSmall, color = secondaryColor
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
        )
        if (text.isNotEmpty()) borderColor = MaterialTheme.colorScheme.primary
    }
}
