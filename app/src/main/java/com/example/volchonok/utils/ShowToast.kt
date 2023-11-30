package com.example.volchonok.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShowToast(text: String = "Данные грузятся!") {
    Toast.makeText(LocalContext.current, text, Toast.LENGTH_SHORT).show()
}