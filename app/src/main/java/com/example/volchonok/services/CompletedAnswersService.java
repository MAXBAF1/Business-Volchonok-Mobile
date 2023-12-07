package com.example.volchonok.services;

import static com.example.volchonok.RemoteInfoStorage.getCoursesData;
import static com.example.volchonok.services.enums.ServiceStringValue.COMPLETED_COURSES_REQUEST_ADDRESS;
import static com.example.volchonok.services.enums.ServiceStringValue.COMPLETED_MODULES_REQUEST_ADDRESS;
import static com.example.volchonok.services.enums.ServiceStringValue.COMPLETED_QUESTIONS_REQUEST_ADDRESS;
import static com.example.volchonok.services.enums.ServiceStringValue.CONTENT_TYPE_JSON;

import android.content.Context;
import android.util.Log;

import com.example.volchonok.data.ModuleData;
import com.example.volchonok.data.NoteData;
import com.example.volchonok.data.TestData;
import com.example.volchonok.data.UserData;
import com.example.volchonok.enums.CourseDataAccessLevel;
import com.example.volchonok.interfaces.ILesson;
import com.example.volchonok.services.enums.ServiceStringValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CompletedAnswersService extends GetService<Integer, List<Integer>> {

    public CompletedAnswersService(Context ctx) {
        super(ctx);
    }

    private List<Integer> getCompletedAnswers(int testId) {
        List<Integer> result = new ArrayList<>();
        String data = sendGetRequestToURL(COMPLETED_QUESTIONS_REQUEST_ADDRESS.getValue() + testId);
        //trim square braces
        Map<String, Object> dataMap = ServiceUtil.getJsonAsMap(data.substring(1, data.length() - 1));

        if (dataMap.isEmpty()) return result;

        try {
            JSONArray answers = new JSONArray(String.valueOf(dataMap.get("answers")));
            int i = 0;

            while (true) {
                JSONObject answer = answers.getJSONObject(i++);
                result.add(Integer.parseInt(answer.getString("answer_id")));

                if (false) break;
            }

        } catch (JSONException ignored) {}


        return result;
    }

    @Override
    protected List<Integer> doInBackground(Integer... integers) {
        return getCompletedAnswers(integers[0]);
    }
}
