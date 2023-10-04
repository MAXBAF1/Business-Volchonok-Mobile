package com.example.volchonok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.volchonok.ui.theme.VolchonokTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StatusBar(5, R.drawable.main_icon)
        }
    }
}

@Composable
private fun StatusBar(scores: Int, avatarId: Int) {
    val mainColor = colorResource(id = R.color.main)
    Row(
        Modifier
            .padding(30.dp, 15.dp)
            .fillMaxWidth(),
        Arrangement.SpaceBetween,
    ) {
        Card(shape = RoundedCornerShape(10000.dp, 10.dp, 10.dp, 10000.dp)) {
            Row(
                Modifier.background(mainColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.main_icon),
                    contentDescription = "wolf_icon",
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    scores.toString(), Modifier.padding(15.dp, 0.dp), color = Color.White
                )
            }

        }

        Image(
            painter = painterResource(id = avatarId),
            contentDescription = "avatar",
            modifier = Modifier
                .size(36.dp)
                .border(2.dp, mainColor, CircleShape)
        )
    }
}