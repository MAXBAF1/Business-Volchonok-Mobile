package com.example.volchonok.screens.vidgets.others

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R

@Composable
fun Greeting(userName: String) {
    Text(
        text = "${stringResource(id = R.string.course_greeting)} $userName",
        modifier = Modifier.padding(top = 15.dp),
        style = MaterialTheme.typography.titleMedium,
    )
    Text(
        text = stringResource(id = R.string.what_learn),
        modifier = Modifier.padding(top = 10.dp),
        style = MaterialTheme.typography.labelSmall,
    )
}