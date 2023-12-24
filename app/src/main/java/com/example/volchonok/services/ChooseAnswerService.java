package com.example.volchonok.services;

import android.content.Context;
import android.util.Log;

import com.example.volchonok.services.enums.ServiceStringValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ChooseAnswerService extends PostService<Map<Integer, List<Integer>>> {

    private final String accessToken;
    private final ServiceStringValue requestAddress;

    public ChooseAnswerService(Context ctx) {
        super(ctx);
        this.requestAddress = ServiceStringValue.COMPLETED_QUESTIONS_REQUEST_ADDRESS;
        accessToken = ctx
                .getSharedPreferences(ServiceStringValue.SHARED_PREFERENCES_NAME.getValue(), Context.MODE_PRIVATE)
                .getString(ServiceStringValue.ACCESS_TOKEN_KEY.getValue(), "");
    }

    public int chooseAnswers(RequestBody requestBody, int testId) {
        int executeCode = tryChooseAnswers(requestBody, testId);

        if (executeCode == 400) {
            try {
                new RefreshTokenService(ctx).execute().get();
                executeCode = tryChooseAnswers(requestBody, testId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return executeCode;
    }

    private int tryChooseAnswers(RequestBody requestBody, int testId) {
        Double responseCode = sendPostRequestToURL(
                requestAddress.getValue() + testId,
                requestBody,
                Collections.singletonMap("Authorization", String.format("Bearer %s", accessToken))
        );

        return responseCode.intValue();
    }

    @Override
    protected Double doInBackground(Map<Integer, List<Integer>>... maps) {
        StringBuilder requestBodyStringBuilder = new StringBuilder("{\"list\":[\n");
        int testId = maps[0].keySet().stream().findAny().orElse(0);

        for (Map.Entry<Integer, List<Integer>> e : maps[1].entrySet()) {
            requestBodyStringBuilder
                    .append("{\n\"question_id\":")
                    .append(e.getKey())
                    .append(",\n")
                    .append("\"answers\":\n")
                    .append(e.getValue().toString())
                    .append("\n}, ");
        }

        requestBodyStringBuilder.deleteCharAt(requestBodyStringBuilder.length() - 1)
                .deleteCharAt(requestBodyStringBuilder.length() - 1)
                .append("]\n}");

        return (double) chooseAnswers(RequestBody.create(requestBodyStringBuilder.toString(),
                        MediaType.get(ServiceStringValue.MEDIA_TYPE_JSON.getValue())),
                testId);
    }
}
