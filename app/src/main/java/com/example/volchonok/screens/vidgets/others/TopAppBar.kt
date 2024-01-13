package com.example.volchonok.screens.vidgets.others

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.volchonok.R
import com.example.volchonok.RemoteInfoStorage
import com.example.volchonok.RemoteInfoStorage.getUserData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.ProfileScreen
import com.example.volchonok.services.CheckUserToken
import com.example.volchonok.services.UserInfoService
import com.example.volchonok.utils.bottomBorder

class TopAppBar(
    private var userData: UserData? = getUserData(),
    private val toProfile: () -> Unit,
    private val isLessonScreen: Boolean = false,
    private val onBackClick: () -> Unit = {},
) {
    private var backgroundColor: Color? = null

    @Composable
    fun Create() {
        val context = LocalContext.current
        if (userData == null) {
            userData = UserInfoService(context).execute().get()
        }

        backgroundColor = if (isLessonScreen) {
            MaterialTheme.colorScheme.primary
        } else MaterialTheme.colorScheme.background
        Row(
            Modifier
                .fillMaxWidth()
                .background(backgroundColor!!)
                .padding(
                    start = if (isLessonScreen) 20.dp else 30.dp,
                    top = 15.dp,
                    end = 30.dp,
                    bottom = 15.dp
                )                ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLessonScreen) CreateArrowBack()
            else CreateCoins()
            CreateAvatar()
        }
    }

    @Composable
    private fun CreateArrowBack() {
        Button(
            onClick = { onBackClick() },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(36.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor!!,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "arrow_back_icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
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
                    painter = painterResource(id = R.drawable.coin),
                    contentDescription = "coin",
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    userData?.coins.toString(),
                    Modifier.padding(15.dp, 0.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    @Composable
    private fun CreateAvatar() {
        val borderColor = if (isLessonScreen) {
            MaterialTheme.colorScheme.onPrimary
        } else MaterialTheme.colorScheme.primary

        Image(
            painter = painterResource(id = ProfileScreen.avatars[userData?.avatar?.toInt() ?: 0]),
            contentDescription = "avatar",
            modifier = Modifier
                .clip(CircleShape)
                .size(36.dp)
                .border(2.dp, borderColor, CircleShape)
                .clickable { toProfile() })

    }
}