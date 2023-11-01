package com.example.volchonok.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.screens.vidgets.others.DefaultButton
import com.example.volchonok.screens.vidgets.others.StylizedTextInput
import java.io.File
import java.io.FileWriter


class LoginScreen(
    private val toCoursesScreen: () -> Unit,
    private val getLoginResult: (loginText: String, passwordText: String) -> Double
) {
    private var usernameText: MutableState<String>? = null
    private var passwordText: MutableState<String>? = null
    private var tryLogin: MutableState<Boolean>? = null
    private var errorText: MutableState<String>? = null

    @Composable
    fun Create() {
        tryLogin = remember { mutableStateOf(false) }
        errorText = remember { mutableStateOf("") }
        Column(
            Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f), contentAlignment = Alignment.Center
            ) { Logo() }
            Column {
                MakeErrorText(errorText!!.value)
                if (tryLogin!!.value) {
                    CheckData()
                    tryLogin?.value = false
                }
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                    TextsInputs()
                    StartButton()
                }

            }
        }
    }

    @Composable
    private fun CheckData() {
        val usernameText = usernameText?.value
        val passwordText = passwordText?.value
        if (usernameText.isNullOrEmpty() || passwordText.isNullOrEmpty()) return

        when (getLoginResult(usernameText, passwordText)) {
            200.0 -> toCoursesScreen()
            -1000.0 -> errorText?.value = stringResource(id = R.string.incorrect)
            -2000.0 -> errorText?.value = stringResource(id = R.string.unknown_error)
        }
    }

    @Composable
    private fun MakeErrorText(message: String) {
        Text(
            text = message,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
    }

    @Composable
    private fun Logo() {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(70.dp, 67.dp),
                    painter = painterResource(id = R.drawable.ic_logo_wolf),
                    contentDescription = "wolf_icon",
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.business_wolf),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

    @Composable
    private fun TextsInputs() {
        Column(Modifier.padding(bottom = 30.dp)) {
            val login = StylizedTextInput("login", stringResource(id = R.string.login))
            val password =
                StylizedTextInput("password", stringResource(id = R.string.password), true)
            login.Create()
            password.Create()
            usernameText = login.text
            passwordText = password.text
        }
    }

    @Composable
    private fun StartButton() {
        var enabled by remember { mutableStateOf(false) }

        enabled = usernameText!!.value.isNotEmpty() && passwordText!!.value.isNotEmpty()
        DefaultButton(enabled, stringResource(id = R.string.log_in).uppercase()) {
            tryLogin?.value = true
        }
    }
}