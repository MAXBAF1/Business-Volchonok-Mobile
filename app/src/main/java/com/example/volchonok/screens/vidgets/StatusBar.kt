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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volchonok.R
import com.example.volchonok.data.UserData

class StatusBar(private val userData: UserData) {

    @Composable
    fun Create() {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            CreateCoins()
            CreateAvatar()
        }
    }


    @Composable
    private fun CreateCoins() {
        Card(shape = RoundedCornerShape(10000.dp, 10.dp, 10.dp, 10000.dp)) {
            Row(
                Modifier.background(colorResource(id = R.color.main)),
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
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.enthalpy298_regular))
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
                .border(2.dp, colorResource(id = R.color.main), CircleShape)
        )
    }
}