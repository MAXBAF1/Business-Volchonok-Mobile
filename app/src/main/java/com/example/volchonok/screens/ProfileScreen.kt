package com.example.volchonok.screens

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.volchonok.R
import com.example.volchonok.RemoteInfoStorage.getCoursesData
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.enums.CourseDataAccessLevel
import com.example.volchonok.screens.vidgets.cards.CourseProgressCardList
import com.example.volchonok.screens.vidgets.others.DefaultButton
import com.example.volchonok.screens.vidgets.others.StylizedTextInput

class ProfileScreen(
    private val onBackClick: () -> Unit,
    private val userData: UserData,
    private var wasUserDataChanged: MutableState<Boolean>
) {
    private lateinit var coursesList: List<CourseData>

    private var showAvatarDialog = mutableStateOf(false)
    private var selectedAvatarNumber = userData.avatar
    private var tappedAvatarNumber = 0
    private val avatarsStates = List(avatars.size) { mutableStateOf(false) }

    @Composable
    fun Create() {
        val context = LocalContext.current
        wasUserDataChanged.value = false
        coursesList = getCoursesData(context, CourseDataAccessLevel.NOTES_DATA)

        Column {
            TopAppBar()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                UserInfoRow()
                CourseProgressCardList(coursesList).Create()
                YourDataTexts()
                TextInputs()
            }
        }
    }

    private var isPhoneEmpty: MutableState<Boolean> = mutableStateOf(false)
    private var isEmailEmpty: MutableState<Boolean> = mutableStateOf(false)

    @Composable
    private fun TextInputs() {
        Column(modifier = Modifier.padding(start = 30.dp, end = 30.dp)) {
            StylizedTextInput(
                "Фамилия Имя Отчество",
                stringResource(id = R.string.fio),
                isEnabled = false,
                inputText = listOf(
                    userData.surname, userData.firstname, userData.middlename
                ).joinToString(" ")
            ).Create()
            StylizedTextInput("89003330088",
                stringResource(id = R.string.phone),
                inputText = userData.phone,
                wasDataChanged = wasUserDataChanged,
                isEmpty = isPhoneEmpty,
                updateData = {
                    userData.phone = it
                }).Create()
            StylizedTextInput("example@gmail.com",
                stringResource(id = R.string.mail),
                inputText = userData.email,
                wasDataChanged = wasUserDataChanged,
                isEmpty = isEmailEmpty,
                updateData = {
                    userData.email = it
                }).Create()
            StylizedTextInput(
                "Екатеринбург",
                stringResource(id = R.string.address),
                isEnabled = false,
                inputText = userData.address
            ).Create()
            StylizedTextInput(
                "1",
                stringResource(id = R.string.grade),
                isEnabled = false,
                isLast = true,
                inputText = userData.class_grade.toString()
            ).Create()
        }
    }

    @Composable
    private fun YourDataTexts() {
        Column(modifier = Modifier.padding(start = 30.dp, top = 30.dp, end = 30.dp)) {
            Text(
                text = stringResource(id = R.string.your_data),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (isPhoneEmpty.value || isEmailEmpty.value) {
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
        }
    }

    @Composable
    private fun TopAppBar() {
        Row(
            Modifier.padding(start = 20.dp, top = 15.dp, bottom = 15.dp, end = 30.dp),
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
        Row(
            Modifier.padding(start = 30.dp, top = 15.dp, end = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar()
            Column(Modifier.padding(start = 15.dp)) {
                Text(
                    text = "${userData.surname} ${userData.firstname}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "${stringResource(id = R.string.level)} 0",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }

    @Composable
    private fun Avatar() {
        avatarsStates[selectedAvatarNumber.toInt()].value = true
        val editIconSize = 66
        val editIcon = AppCompatResources.getDrawable(LocalContext.current, R.drawable.ic_edit)!!
            .toBitmap(editIconSize, editIconSize).asImageBitmap()

        Image(painter = painterResource(id = avatars[selectedAvatarNumber.toInt()]),
            contentDescription = "avatar",
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable { showAvatarDialog.value = true }
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

        if (showAvatarDialog.value) AvatarDialog()
    }

    @Composable
    private fun AvatarDialog() {
        AlertDialog(text = {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    text = stringResource(id = R.string.choose_avatar),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                AvatarsList()
            }
        }, containerColor = MaterialTheme.colorScheme.background, onDismissRequest = {
            avatarsStates.forEach { it.value = false }
            showAvatarDialog.value = false
        }, confirmButton = {
            DefaultButton(
                text = stringResource(id = R.string.save).uppercase(), Modifier.padding(top = 6.dp)
            ) {
                userData.avatar = tappedAvatarNumber.toString()
                selectedAvatarNumber = tappedAvatarNumber.toString()
                showAvatarDialog.value = false
            }
            wasUserDataChanged.value = true
        })
    }

    @Composable
    private fun AvatarsList() {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(space = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
        ) {
            itemsIndexed(avatars) { i, avatar ->
                CreateAvatar(i, avatar)
            }
        }
    }

    @Composable
    private fun CreateAvatar(index: Int, avatarId: Int) {
        val borderColor = if (avatarsStates[index].value) {
            MaterialTheme.colorScheme.primary
        } else MaterialTheme.colorScheme.secondaryContainer

        Image(
            painterResource(id = avatarId),
            contentDescription = "avatar",
            modifier = Modifier
                .aspectRatio(1f)
                .clip(CircleShape)
                .border(2.dp, borderColor, CircleShape)
                .clickable {
                    avatarsStates.forEach { it.value = false }
                    avatarsStates[index].value = true
                    tappedAvatarNumber = index
                },
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
    }

    companion object {
        val avatars = arrayOf(
            R.drawable.ava_1,
            R.drawable.ava_2,
            R.drawable.ava_3,
            R.drawable.ava_4,
            R.drawable.ava_5,
            R.drawable.ava_6,
            R.drawable.ava_7,
            R.drawable.ava_8,
            R.drawable.ava_9,
            R.drawable.ava_10,
            R.drawable.ava_11,
            R.drawable.ava_12
        )
    }
}