package com.example.volchonok.screens.vidgets.others

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.volchonok.enums.ButtonType

@Composable
fun DefaultButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonType: ButtonType = ButtonType.Filled,
    onClick: () -> Unit
) {
    val containerColor: Color
    val contentColor: Color

    if (buttonType == ButtonType.Filled) {
        containerColor = MaterialTheme.colorScheme.primary
        contentColor = MaterialTheme.colorScheme.onPrimary
    } else {
        containerColor = Color.Transparent
        contentColor = MaterialTheme.colorScheme.primary
    }

    Button(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp),
        onClick = { onClick() },
        shape = RoundedCornerShape(10.dp),
        enabled = enabled,
        border = BorderStroke(
            1.dp,
            if (buttonType == ButtonType.Filled) Color.Transparent else MaterialTheme.colorScheme.primary
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.background
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.titleSmall)
    }
}