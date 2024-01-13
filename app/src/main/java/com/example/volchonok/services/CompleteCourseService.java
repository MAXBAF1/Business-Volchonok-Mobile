package com.example.volchonok.services;

import static com.example.volchonok.RemoteInfoStorage.getCoursesData;
import static com.example.volchonok.RemoteInfoStorage.getSharedPreferences;
import static com.example.volchonok.RemoteInfoStorage.setContext;
import static com.example.volchonok.services.enums.ServiceStringValue.COMPLETED_COURSES_REQUEST_ADDRESS;
import static com.example.volchonok.services.enums.ServiceStringValue.COMPLETED_MODULES_REQUEST_ADDRESS;
import static com.example.volchonok.services.enums.ServiceStringValue.CONTENT_TYPE_JSON;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.volchonok.RemoteInfoStorage;
import com.example.volchonok.data.ModuleData;
import com.example.volchonok.data.NoteData;
import com.example.volchonok.data.TestData;
import com.example.volchonok.enums.CourseDataAccessLevel;
import com.example.volchonok.interfaces.ILesson;
import com.example.volchonok.services.enums.ServiceStringValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CompleteCourseService extends PostService<Integer> {

    private final String accessToken;
    private final ServiceStringValue requestAddress;

    public CompleteCourseService(ServiceStringValue requestAddress, Context ctx) {
        super(ctx);
        this.requestAddress = requestAddress;
        setContext(ctx);
        accessToken = getSharedPreferences()
                .getString(ServiceStringValue.ACCESS_TOKEN_KEY.getValue(), "");
    }

    public boolean markCourseCompleted(List<Integer> ids) {
        boolean isExecuted = tryMarkCourseCompleted(ids);
        if (!isExecuted) {
            try {
                new RefreshTokenService(ctx).execute().get();
                isExecuted = tryMarkCourseCompleted(ids);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (isExecuted) {
            switch (requestAddress) {
                case COMPLETED_MODULES_REQUEST_ADDRESS -> {
                    getCoursesData(ctx, CourseDataAccessLevel.ONLY_COURSES_DATA)
                            .stream().filter(courseData -> new HashSet<>(courseData.getModules()
                                    .stream()
                                    .map(ModuleData::getId)
                                    .collect(Collectors.toList())).containsAll(ids)
                            ).forEach(course -> {
                                if (course.getModules().stream()
                                        .flatMap(m -> m.getLessonNotes().stream())
                                        .allMatch(ILesson::isCompleted)) {

                                    new CompleteCourseService(COMPLETED_COURSES_REQUEST_ADDRESS, ctx)
                                            .execute(course.getId());
                                }
                            });
                }
                case COMPLETED_NOTES_REQUEST_ADDRESS -> {
                    getCoursesData(ctx, CourseDataAccessLevel.ONLY_COURSES_DATA)
                            .forEach(course ->
                                    course.getModules().forEach(module -> {
                                        module.getLessonNotes().stream()
                                                .filter(note -> ids.contains(note.getId()))
                                                .forEach(note -> note.setCompleted(true));

                                        if (Stream.concat(module.getLessonTests().stream(),
                                                module.getLessonNotes().stream())
                                                        .allMatch(ILesson::isCompleted)) {

                                            new CompleteCourseService(COMPLETED_MODULES_REQUEST_ADDRESS, ctx)
                                                    .execute(module.getId());
                                        }
                                    })
                            );
                }
                case COMPLETED_TESTS_REQUEST_ADDRESS -> {
                    getCoursesData(ctx, CourseDataAccessLevel.ONLY_COURSES_DATA)
                            .forEach(course ->
                                    course.getModules().forEach(module -> {
                                        module.getLessonTests().stream()
                                                .filter(test -> ids.contains(test.getId()))
                                                .forEach(test -> test.setCompleted(true));

                                        if (Stream.concat(module.getLessonTests().stream(),
                                                        module.getLessonNotes().stream())
                                                .allMatch(ILesson::isCompleted)) {

                                            new CompleteCourseService(COMPLETED_MODULES_REQUEST_ADDRESS, ctx)
                                                    .execute(module.getId());
                                        }
                                    })
                            );
                }
            }
        }

        return isExecuted;
    }

    private boolean tryMarkCourseCompleted(List<Integer> ids) {
        RequestBody requestBody = RequestBody.create(
                String.format("{\"list\":%s}", ids.toString()),
                MediaType.get(CONTENT_TYPE_JSON.getValue())
        );
        Double responseCode = sendPostRequestToURL(
                requestAddress.getValue(),
                requestBody,
                Collections.singletonMap("Authorization", String.format("Bearer %s", accessToken))
        );

        return responseCode.intValue() == 200;
    }

    @Override
    protected Double doInBackground(Integer... ints) {
        return (double) (markCourseCompleted(Arrays.stream(ints).collect(Collectors.toList())) ? 200 : 400);
    }
}