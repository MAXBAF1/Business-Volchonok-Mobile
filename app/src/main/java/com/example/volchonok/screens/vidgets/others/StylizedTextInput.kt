package com.example.volchonok.screens.vidgets.others

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.volchonok.R

class StylizedTextInput(
    private val hint: String,
    private val titleText: String = "",
    private val isPasswordField: Boolean = false,
    private val isEnabled: Boolean = true,
    private val isLast: Boolean = false,
    private var inputText: String = "",
    private var wasDataChanged: MutableState<Boolean> = mutableStateOf(false),
    private val isEmpty: MutableState<Boolean>? = null,
    private val updateData: ((String) -> Unit)? = null
) {
    var text: MutableState<String>? = null

    @Composable
    fun Create() {
        if (titleText != "") CreateTitle()
        CreateTextField()
    }

    @Composable
    private fun CreateTitle() {
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = titleText,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CreateTextField() {
        text = remember { mutableStateOf(inputText) }
        if (isEmpty != null) isEmpty.value = text!!.value.isEmpty()
        val passwordVisible = remember { mutableStateOf(false) }
        val onBackgroundColor = MaterialTheme.colorScheme.onBackground
        val secondaryColor = MaterialTheme.colorScheme.outlineVariant
        var borderColor by remember { mutableStateOf(secondaryColor) }

        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = if (isLast) 30.dp else 0.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .onFocusChanged {
                borderColor = when {
                    it.isFocused -> onBackgroundColor
                    else -> secondaryColor
                }
            },
            value = text!!.value,
            onValueChange = {
                text!!.value = it
                updateData?.let { function -> function(it) }
                wasDataChanged.value = true
            },
            placeholder = {
                Text(
                    text = hint, style = MaterialTheme.typography.labelSmall, color = secondaryColor
                )
            },
            enabled = isEnabled,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            visualTransformation = if (passwordVisible.value || !isPasswordField) {
                VisualTransformation.None
            } else PasswordVisualTransformation(),
            trailingIcon = {
                if (isPasswordField) {
                    CreatePasswordIcon(passwordVisible)
                } else if (isEnabled) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.ic_edit_field),
                        contentDescription = "ic_edit_field",
                        tint = borderColor
                    )
                }
            })
        if (text!!.value.isNotEmpty()) borderColor = MaterialTheme.colorScheme.primary
    }

    @Composable
    private fun CreatePasswordIcon(passwordVisible: MutableState<Boolean>) {
        val image = if (passwordVisible.value) Icons.Filled.Visibility
        else Icons.Filled.VisibilityOff

        val description = if (passwordVisible.value) {
            stringResource(R.string.hide_password)
        } else stringResource(R.string.show_password)

        IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
            Icon(imageVector = image, description)
        }
    }
}
