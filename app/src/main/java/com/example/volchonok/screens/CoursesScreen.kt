package com.example.volchonok.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volchonok.R
import com.example.volchonok.data.CourseData
import com.example.volchonok.data.UserData
import com.example.volchonok.screens.vidgets.StatusBar

class CoursesScreen {
    @Composable
    fun Init(userData: UserData, coursesList: Iterable<CourseData>) {
        Column(
            Modifier
                .padding(30.dp, 15.dp)
                .fillMaxSize()
        ) {
            StatusBar(userData).Create()
            Text(
                text = stringResource(id = R.string.course_greeting),
                modifier = Modifier.padding(top = 30.dp),
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.enthalpy298_regular))
            )
            Text(
                text = stringResource(id = R.string.what_learn),
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
            CoursesList(coursesList)
        }
    }

    @Composable
    private fun CoursesList(coursesList: Iterable<CourseData>) {
        Column(modifier = Modifier.padding(top = 20.dp)) {
            coursesList.forEach {
                CourseCard(courseData = it)
            }
        }
    }

    @Composable
    private fun CourseCard(courseData: CourseData) {
        ElevatedCard(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.course_background),
                    contentDescription = "course_background",
                    modifier = Modifier.size(328.dp, 162.dp)
                )
                Column(modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
                    Text(
                        text = courseData.name,
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.enthalpy298_regular)),
                        color = Color.White
                    )
                    Text(
                        text = courseData.description,
                        modifier = Modifier.padding(top = 10.dp),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        color = Color.White
                    )
                    Button(
                        onClick = { /* Обработка нажатия кнопки */ },
                        modifier = Modifier.padding(top = 30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_up_btn),
                            color = colorResource(id = R.color.main),
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                            //modifier = Modifier.padding(28.dp, 12.dp),
                        )
                    }
                }
            }
        }
    }

}