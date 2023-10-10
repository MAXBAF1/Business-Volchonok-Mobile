package com.example.volchonok.screens.vidgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.data.UserData

class TopAppBar(private val userData: UserData) {
    @Composable
    fun Create() {
        Row(Modifier.fillMaxWidth().padding(bottom = 15.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            CreateCoins()
            CreateAvatar()
        }
    }


    @Composable
    private fun CreateCoins() {
        Card(shape = RoundedCornerShape(Int.MAX_VALUE.dp, 10.dp, 10.dp, Int.MAX_VALUE.dp)) {
            Row(
                Modifier.background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wolf_icon),
                    contentDescription = "wolf_icon",
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    userData.coins.toString(),
                    Modifier.padding(15.dp, 0.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    @Composable
    private fun CreateAvatar() {
        Image(
            painter = painterResource(id = userData.avatarId),
            contentDescription = "avatar",
            modifier = Modifier
                .size(36.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
    }
}