package com.example.volchonok;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.volchonok.data.CourseData;
import com.example.volchonok.data.TestData;
import com.example.volchonok.data.UserData;
import com.example.volchonok.enums.CourseDataAccessLevel;
import com.example.volchonok.services.CourseService;
import com.example.volchonok.services.UserInfoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;

public class RemoteInfoStorage {
    private static UserData userData;
    private static List<CourseData> coursesData;
    private static final Object locker = new Object();

    static {
        coursesData = new ArrayList<>();
    }

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

    public static List<CourseData> getCoursesData(Context context, CourseDataAccessLevel level) {
        synchronized (RemoteInfoStorage.class) {
            Log.d("TAG", "-----------check: " + !checkCourseDataLevel(level) + " " + level);
            if (!checkCourseDataLevel(level)) {
                try {
                    coursesData = (List<CourseData>) new CourseService(context)
                            .execute(new Pair(level, coursesData))
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.d("TAG", "updateCoursesData error: ", e);
                }
            }
            return coursesData;
        }
    }

    public static void setCoursesData(List<CourseData> coursesData) {
        RemoteInfoStorage.coursesData = coursesData;
    }

    private static synchronized boolean checkCourseDataLevel(CourseDataAccessLevel level) {
        switch (level) {
            case ONLY_COURSES_DATA -> {
                return coursesData.size() != 0;
            }
            case MODULES_DATA -> {
                return coursesData.get(0).getModules().size() != 0;
            }
            case NOTES_DATA -> {
                if (checkCourseDataLevel(CourseDataAccessLevel.MODULES_DATA)) {
                    return coursesData.get(0).getModules().get(0).getLessonNotes().size() != 0;
                } else {
                    return false;
                }
            }
            case TESTS_DATA -> {
                if (checkCourseDataLevel(CourseDataAccessLevel.MODULES_DATA)) {
                    return coursesData.get(0).getModules().get(0).getLessonTests().size() != 0;
                } else {
                    return false;
                }
            }
            case QUESTIONS_DATA -> {
                if (checkCourseDataLevel(CourseDataAccessLevel.TESTS_DATA)) {
                    return ((TestData) coursesData.get(0).getModules().get(0).getLessonTests().get(0)).getQuestions().size() != 0;
                } else {
                    return false;
                }
            }
            default -> { //ALL_DATA
                return checkCourseDataLevel(CourseDataAccessLevel.TESTS_DATA)
                        && checkCourseDataLevel(CourseDataAccessLevel.NOTES_DATA);
            }
        }
    }
}