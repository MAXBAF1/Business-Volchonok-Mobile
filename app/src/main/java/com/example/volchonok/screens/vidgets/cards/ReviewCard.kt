package com.example.volchonok.screens.vidgets.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.data.ReviewData
import com.example.volchonok.data.UserData

class ReviewCard(private val reviewData: ReviewData) {
    @Composable
    fun Add() {
        Card(
            modifier = Modifier.padding(end = 15.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(32.dp).clip(CircleShape),
                        painter = painterResource(id = reviewData.userData.avatarId),
                        contentDescription = "avatar"
                    )
                    Text(
                        text = reviewData.userData.name,
                        modifier = Modifier.padding(start = 15.dp),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = reviewData.message,
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .widthIn(max = 250.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}