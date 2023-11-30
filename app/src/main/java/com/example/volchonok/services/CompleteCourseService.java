package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.COMPLETED_COURSES_REQUEST_ADDRESS;
import static com.example.volchonok.services.enums.ServiceStringValue.CONTENT_TYPE_JSON;

import android.content.Context;

import com.example.volchonok.services.enums.ServiceStringValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CompleteCourseService extends PostService<Integer> {

    private String accessToken;
    private ServiceStringValue requestAddress;

    public CompleteCourseService(ServiceStringValue requestAddress, Context ctx) {
        super(ctx);
        this.requestAddress = requestAddress;
        accessToken = ctx
                .getSharedPreferences(ServiceStringValue.SHARED_PREFERENCES_NAME.getValue(), Context.MODE_PRIVATE)
                .getString(ServiceStringValue.ACCESS_TOKEN_KEY.getValue(), "");
    }

    public boolean markCourseCompleted(int[] courseIds) {
        boolean isCorrect = tryMarkCourseCompleted(courseIds);
        if (!isCorrect) {
            try {
                new RefreshTokenService(ctx).execute().get();
                isCorrect = tryMarkCourseCompleted(courseIds);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return isCorrect;
    }

    private boolean tryMarkCourseCompleted(int[] courseIds) {
        RequestBody requestBody = RequestBody.create(
                String.format("{\"list\":%s}", Arrays.toString(courseIds)),
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
        return (double) (markCourseCompleted(Arrays.stream(ints).mapToInt(Integer::intValue).toArray()) ? 200 : 400);
    }
}
