package com.example.volchonok.services;

import android.content.Context;
import android.util.Log;

import com.example.volchonok.services.enums.ServiceStringValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    public boolean chooseAnswers(RequestBody requestBody, int testId) {
        boolean isExecuted = tryChooseAnswers(requestBody, testId);
        if (!isExecuted) {
            try {
                new RefreshTokenService(ctx).execute().get();
                isExecuted = tryChooseAnswers(requestBody, testId);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return isExecuted;
    }

    private boolean tryChooseAnswers(RequestBody requestBody, int testId) {
        Double responseCode = sendPostRequestToURL(
                requestAddress.getValue() + testId,
                requestBody,
                Collections.singletonMap("Authorization", String.format("Bearer %s", accessToken))
        );

        return responseCode.intValue() == 200;
    }

    public Double executeChooseAnswers(int testId, Map<Integer, List<Integer>> answers) {
        try {
            return new ChooseAnswerService(ctx).execute(
                    Map.of(testId, List.of()),
                    answers
            ).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
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

        return chooseAnswers(RequestBody.create(requestBodyStringBuilder.toString(),
                        MediaType.get(ServiceStringValue.MEDIA_TYPE_JSON.getValue())),
                testId) ? 200d : 400;
    }
}
