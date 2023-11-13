package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.volchonok.data.AnswerData;
import com.example.volchonok.data.CourseData;
import com.example.volchonok.data.ModuleData;
import com.example.volchonok.data.NoteData;
import com.example.volchonok.data.QuestionData;
import com.example.volchonok.data.ReviewData;
import com.example.volchonok.data.TestData;
import com.example.volchonok.data.UserData;
import com.example.volchonok.interfaces.ILesson;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CourseService extends AsyncTask<Void, Void, List<CourseData>> {
    protected OkHttpClient httpClient;
    protected Request request;
    protected Context ctx;
    protected SharedPreferences sPref;
    protected Gson gson;

    public CourseService(Context ctx) {
        this.ctx = ctx;
        sPref = ctx.getSharedPreferences(SHARED_PREFERENCES_NAME.getValue(), Context.MODE_PRIVATE);
        httpClient = new OkHttpClient();
        gson = new Gson();
    }

    public List<CourseData> getCourses() { //FIXME get not all courses, by required
        List<CourseData> response = new ArrayList<>();

        String courses = RESPONSE_TO_PATH(USER_COURSES_REQUEST_ADDRESS.getValue());

        if (courses == null) {
            new RefreshTokenService(ctx).execute();
            courses = RESPONSE_TO_PATH(USER_COURSES_REQUEST_ADDRESS.getValue());
        }

        int[] coursesId = gson.fromJson(courses, int[].class);

        if (coursesId == null || coursesId.length == 0) return null;

        for (int courseId : coursesId) {
            fillCoursesData(response, courseId);
        }

        return response;
    }

    private void fillCoursesData(List<CourseData> courses, int courseId) {
        String courseName, courseDescription;
        List<ModuleData> modules = new ArrayList<>();
        //FixMe
        UserData emptyUser = new UserData(-1, "", "", "", "", -1, -1, "");
        List<ReviewData> reviews = List.of(
                new ReviewData(emptyUser, ""),
                new ReviewData(emptyUser, ""),
                new ReviewData(emptyUser, "")
        );

        Map<String, Object> dataMap = PostService.getJsonAsMap(
                RESPONSE_TO_PATH(COURSE_DATA_REQUEST_ADDRESS.getValue() + courseId)
        );
        List<Double> modulesId = PostService.getJsonAsList(
                RESPONSE_TO_PATH(COURSE_DATA_REQUEST_ADDRESS.getValue() + courseId + "/modules")
        );

        courseName = String.valueOf(dataMap.get(NAME_KEY.getValue()));
        courseDescription = String.valueOf(dataMap.get(DESCRIPTION_KEY.getValue()));

        if (modulesId != null) {
            for (double moduleId : modulesId) {
                modules.add(getModuleData((int) moduleId));
            }
        }

        courses.add(new CourseData(
                courseName,
                modules,
                courseDescription,
                reviews
        ));

    }

    private ModuleData getModuleData(int moduleId) {
        List<ILesson> notes = new ArrayList<>();
        List<ILesson> tests = new ArrayList<>();

        Map<String, Object> moduleDataMap = PostService.getJsonAsMap(
                RESPONSE_TO_PATH(MODULE_DATA_REQUEST_ADDRESS.getValue() + moduleId)
        );
        List<Double> moduleNotesId = PostService.getJsonAsList(
                RESPONSE_TO_PATH(MODULE_DATA_REQUEST_ADDRESS.getValue() + moduleId + "/lessons"));
        List<Double> moduleTestsId = PostService.getJsonAsList(
                RESPONSE_TO_PATH(MODULE_DATA_REQUEST_ADDRESS.getValue() + moduleId + "/tests"));

        if (moduleTestsId != null) {
            for (double moduleNoteId : moduleNotesId) {
                notes.add(getNoteData((int) moduleNoteId));
            }
        }

        if (moduleTestsId != null) {
            for (double moduleTestId : moduleTestsId) {
                tests.add(getTestData((int) moduleTestId));
            }
        }

        return new ModuleData(
                "name", //String.valueOf(moduleDataMap.get("name")),
                "desc",       //String.valueOf(moduleDataMap.get("description")),
                notes,
                tests
        );
    }

    private NoteData getNoteData(int noteId) {
        Map<String, Object> noteDataMap = PostService.getJsonAsMap(
                RESPONSE_TO_PATH(LESSON_DATA_REQUEST_ADDRESS.getValue() + noteId));

        return new NoteData(
                String.valueOf(noteDataMap.get("name")),
                String.valueOf(noteDataMap.get("description")),
                String.valueOf(noteDataMap.get("duration")),
                getComplition(LESSONS_REQUEST_ADDRESS.getValue(), noteId),//TODO add request to user/lessons
                String.valueOf(noteDataMap.get("chat_text"))
        );

    }

    private TestData getTestData(int testId) {
        Map<String, Object> testDataMap = PostService.getJsonAsMap(
                RESPONSE_TO_PATH(TEST_DATA_REQUEST_ADDRESS.getValue() + testId));

        List<QuestionData> questions = new ArrayList<>();
        List<Double> testQuestions = PostService.getJsonAsList(RESPONSE_TO_PATH(TEST_DATA_REQUEST_ADDRESS.getValue() + testId + "/questions"));

        if (testQuestions == null)
            return null;

        testQuestions.forEach(e -> questions.add(getQuestionData(e.intValue())));

        return new TestData(
                String.valueOf(testDataMap.get("name")),
                String.valueOf(testDataMap.get("description")),
                String.valueOf(testDataMap.get("duration")),
                getComplition(TESTS_REQUEST_ADDRESS.getValue(), testId),
                questions
        );
    }

    private QuestionData getQuestionData(int questionId) {
        Map<String, Object> q = PostService.getJsonAsMap(RESPONSE_TO_PATH(QUESTION_DATA_REQUEST_ADDRESS.getValue() + questionId));

        List<AnswerData> answers = new ArrayList<>();
        JSONArray answersArray = (JSONArray) q.get("answers");

        for (int i = 0; i < (answersArray != null ? answersArray.length() : 0); i++) {
            try {
                JSONObject answer = answersArray.getJSONObject(i);
                answers.add(new AnswerData(
                        answer.getString("text"),
                        answer.getBoolean("is_right")
                ));
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }

        return new QuestionData(
                String.valueOf(q.get("text")),
                answers,
                String.valueOf(q.get("explanation"))
        );
    }

    private boolean getComplition(String requestAddress, double noteId) {
        List<String> completedNotes = PostService.getJsonAsList(
                RESPONSE_TO_PATH(requestAddress));

        return completedNotes != null && completedNotes.contains(String.valueOf(noteId));
    }

    private String RESPONSE_TO_PATH(String path) {
        request = new Request.Builder()
                .url(path)
                .method(REQUEST_METHOD_GET.getValue(), null)
                .addHeader("Authorization", "Bearer " + sPref.getString(ACCESS_TOKEN_KEY.getValue(), ""))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            ResponseBody responseBody = response.body();
            Map<String, Object> responseBodyAsMap = PostService.getJsonAsMap(responseBody.string());

            double responseCode = Double.parseDouble(
                    String.valueOf(responseBodyAsMap.get(RESPONSE_STATUS_KEY.getValue()))
            );

            if (Math.abs(responseCode - 200.0) < 1e6) {
                return responseBodyAsMap.get(RESPONSE_DATA_KEY.getValue()).toString();
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected List<CourseData> doInBackground(Void... voids) {
        return getCourses();
    }
}
