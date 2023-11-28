package com.example.volchonok;

import android.content.Context;
import android.util.Pair;

import com.example.volchonok.data.CourseData;
import com.example.volchonok.data.UserData;
import com.example.volchonok.enums.CourseDataAccessLevel;
import com.example.volchonok.services.CourseService;
import com.example.volchonok.services.UserInfoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RemoteInfoStorage {
    private static UserData userData;
    private static List<CourseData> coursesData;

    public static UserData getUserData(Context context) {
        try {
            if (userData == null)
                userData = new UserInfoService(context).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            userData = new UserData();
        }

        return userData;
    }

    public static void setUserData(UserData userData) {
        RemoteInfoStorage.userData = userData;
    }

    public static List<CourseData> getCoursesData(Context context) {
        try {
            if (coursesData == null)
                coursesData = (List<CourseData>) new CourseService(context)
                    .execute(new Pair(CourseDataAccessLevel.ONLY_COURSES_DATA, new ArrayList<CourseData>()))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            coursesData = new ArrayList<>();
        }

        return coursesData;
    }

    public static void setCoursesData(List<CourseData> coursesData) {
        RemoteInfoStorage.coursesData = coursesData;
    }
}
