package com.example.volchonok.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.vidgets.StylizedTextInput
import com.example.volchonok.screens.vidgets.TopAppBar
import com.example.volchonok.screens.vidgets.cards.CourseProgressCard

class ProfileScreen(
    private val userData: UserData,
    private val coursesList: Iterable<CourseData>,
    private val onBackClick: () -> Unit
) {
    @Composable
    fun Create() {
        Column(
            Modifier
                .padding(start = 30.dp, end = 30.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar()
            UserInfoRow()
            CourseProgressCard(coursesList).Create()
            YourDataTexts()
            TextInputs()
        }
    }

    @Composable
    private fun TextInputs() {
        StylizedTextInput("Лепинских Максим Игоревич", stringResource(id = R.string.fio)).Create()
        StylizedTextInput("89501234567", stringResource(id = R.string.phone)).Create()
        StylizedTextInput("example@gmail.com", stringResource(id = R.string.mail)).Create()
        StylizedTextInput("Екатеринбург", stringResource(id = R.string.address)).Create()
        StylizedTextInput("1", stringResource(id = R.string.grade), isLast = true).Create()
    }

    @Composable
    private fun YourDataTexts() {
        Text(
            modifier = Modifier.padding(top = 30.dp),
            text = stringResource(id = R.string.your_data),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(modifier = Modifier.padding(top = 5.dp)) {
            Text(
                text = "${stringResource(id = R.string.fill_data)} ",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(id = R.string.five_coins),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

    }

    @Composable
    private fun TopAppBar() {
        Row(Modifier.padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { onBackClick() },
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "arrow_back_icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = stringResource(id = R.string.profile),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }


    @Composable
    private fun UserInfoRow() {
        Row(Modifier.padding(top = 30.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = userData.avatarId),
                contentDescription = "avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(64.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Column(Modifier.padding(start = 15.dp)) {
                Text(
                    text = userData.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "${stringResource(id = R.string.level)} ${userData.level}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}