package com.example.volchonok;

import static com.example.volchonok.services.enums.ServiceStringValue.SHARED_PREFERENCES_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import com.example.volchonok.data.CourseData;
import com.example.volchonok.data.TestData;
import com.example.volchonok.data.UserData;
import com.example.volchonok.enums.CourseDataAccessLevel;
import com.example.volchonok.services.LoadCourseService;
import com.example.volchonok.services.enums.ServiceStringValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RemoteInfoStorage {
    private static UserData userData;
    private static SharedPreferences sharedPreferences;
    private static Context context;
    private static List<CourseData> coursesData;
    private static boolean wasSentRefreshRequest;

    static {
        coursesData = new ArrayList<>();
    }

    public static UserData getUserData() {
        return userData;
    }

    public static void setUserData(UserData userData) {
        RemoteInfoStorage.userData = userData;
    }

    public static List<CourseData> getCoursesData(Context context, CourseDataAccessLevel level) {
        boolean isLevelAllow = checkCourseDataLevel(level);

        if (!isLevelAllow) {
            try {
                coursesData = (List<CourseData>) new LoadCourseService(context)
                        .execute(new Pair(level, coursesData))
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                Log.d("TAG", "RemInfSto - getCoursesData error: ", e);
            }
        }

        return coursesData;
    }

    public static void setCoursesData(List<CourseData> coursesData) {
        RemoteInfoStorage.coursesData = coursesData;
    }

    public static boolean checkCourseDataLevel(CourseDataAccessLevel level) {
        switch (level) {
            case ONLY_COURSES_DATA -> {
                return coursesData.size() != 0;
            }
            case MODULES_DATA -> {
                return coursesData.stream().noneMatch(c -> c.getModules().isEmpty());
            }
            case NOTES_DATA -> {
                if (checkCourseDataLevel(CourseDataAccessLevel.MODULES_DATA)) {
                    return coursesData.stream().allMatch(c -> c.getModules()
                            .stream().noneMatch(m -> m.getLessonNotes().isEmpty()));
                } else {
                    return false;
                }
            }
            case TESTS_DATA -> {
                if (checkCourseDataLevel(CourseDataAccessLevel.MODULES_DATA)) {
                    return coursesData.stream().allMatch(c -> c.getModules()
                            .stream().noneMatch(m -> m.getLessonTests().isEmpty()));
                } else {
                    return false;
                }
            }
            case QUESTIONS_DATA -> {
                if (checkCourseDataLevel(CourseDataAccessLevel.TESTS_DATA)) {
                    return coursesData.stream().allMatch(c -> c.getModules()
                            .stream().allMatch(m -> m.getLessonTests()
                                    .stream().noneMatch(t -> t.getQuestions().isEmpty())));
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

    public static boolean isWasSentRefreshRequest() {
        return wasSentRefreshRequest;
    }

    public static void setContext(Context context) {
        if (RemoteInfoStorage.context == null) {
            RemoteInfoStorage.context = context;
            RemoteInfoStorage.sharedPreferences =
                    context.getSharedPreferences(SHARED_PREFERENCES_NAME.getValue(), Context.MODE_PRIVATE);
        }
    }

    public static void setWasSentRefreshRequest(boolean wasSentRefreshRequest) {
        RemoteInfoStorage.wasSentRefreshRequest = wasSentRefreshRequest;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}