package com.example.volchonok.services;

import static com.example.volchonok.services.ServiceUtil.getJsonAsList;
import static com.example.volchonok.services.ServiceUtil.getJsonAsMap;
import static com.example.volchonok.services.enums.ServiceStringValue.COMPLETED_QUESTIONS_REQUEST_ADDRESS;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CompletedAnswersService extends GetService<Integer, List<Integer>> {

    public CompletedAnswersService(Context ctx) {
        super(ctx);
    }

    private List<Integer> getCompletedAnswers(int testId) {
        List<Integer> result = new ArrayList<>();
        String data = sendGetRequestToURL(COMPLETED_QUESTIONS_REQUEST_ADDRESS.getValue() + testId)
                .replace("}]},{", "}]}, {");

        Arrays.stream(data.substring(1, data.length() - 1).split(", ")).forEach(
                testAsString -> {

                    String answersAsString = String.valueOf(getJsonAsMap(testAsString).get("answers"));
                    List<JSONObject> answersAsList = getJsonAsList(answersAsString);

                    if (!answersAsList.isEmpty())
                        answersAsList.forEach(ans -> {

                            Map<String, Object> answerAsMap = getJsonAsMap(ans.toString());
                            result.add(Integer.parseInt(String.valueOf(answerAsMap.get("answer_id"))));
                        });
                }
        );

        return result;
    }

    @Override
    protected List<Integer> doInBackground(Integer... integers) {
        return getCompletedAnswers(integers[0]);
    }
}
