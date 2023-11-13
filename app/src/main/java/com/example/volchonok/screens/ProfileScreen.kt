package com.example.volchonok.screens

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
import com.example.volchonok.R
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.vidgets.cards.CourseProgressCard
import com.example.volchonok.screens.vidgets.others.StylizedTextInput
import com.example.volchonok.services.UserInfoService
import org.xmlpull.v1.XmlPullParser

class ProfileScreen(
    private val coursesList: Iterable<CourseData>, private val onBackClick: () -> Unit
) {
    private lateinit var userData: UserData

    @Composable
    fun Create() {
        userData = UserInfoService(LocalContext.current).execute().get()

        Column(modifier = Modifier.padding(start = 20.dp, end = 30.dp)) {
            TopAppBar()
            Column(
                Modifier
                    .padding(start = 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                UserInfoRow()
                CourseProgressCard(coursesList).Create()
                YourDataTexts()
                TextInputs()
            }
        }
    }

    @Composable
    private fun TextInputs() {
        StylizedTextInput("Лепинских Максим Игоревич", stringResource(id = R.string.fio), isEnabled = false).Create()
        StylizedTextInput("89501234567", stringResource(id = R.string.phone)).Create()
        StylizedTextInput("example@gmail.com", stringResource(id = R.string.mail)).Create()
        StylizedTextInput("Екатеринбург", stringResource(id = R.string.address), isEnabled = false).Create()
        StylizedTextInput("1", stringResource(id = R.string.grade), isEnabled = false, isLast = true).Create()
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
        Row(
            Modifier.padding(top = 15.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
        Row(Modifier.padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically) {
            Avatar()
            Column(Modifier.padding(start = 15.dp)) {
                Text(
                    text = userData.name.toString(),
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

    @Composable
    private fun Avatar() {
        val defaultAvatar = ImageBitmap.imageResource(R.drawable.wolf_icon)
        var bitmap: Bitmap? by remember { mutableStateOf(defaultAvatar.asAndroidBitmap()) }
        val context = LocalContext.current

        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                val source = uri?.let { ImageDecoder.createSource(context.contentResolver, it) }
                bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
            }

        val editIconSize = 66
        val editIcon = AppCompatResources.getDrawable(context, R.drawable.ic_edit)!!
            .toBitmap(editIconSize, editIconSize).asImageBitmap()

        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(64.dp)
                    .clickable { launcher.launch("image/*") }
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            color = Color(0f, 0f, 0f, 0.4f)
                        )
                        val iconSize = 24.dp.toPx()

                        drawImage(
                            image = editIcon,
                            topLeft = Offset(
                                (size.width - iconSize) / 2, (size.height - iconSize) / 2
                            ),
                        )
                    },
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }
    }
}