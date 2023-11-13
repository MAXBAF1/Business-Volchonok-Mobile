package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.*;

import android.content.Context;
import android.content.SharedPreferences;

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

public class CourseService extends GetService<Void, List<CourseData>> {
    protected Gson gson;

    public CourseService(Context ctx) {
        super(ctx);
        gson = new Gson();
    }

    public List<CourseData> getCourses() { //FIXME get not all courses, by required
        List<CourseData> response = new ArrayList<>();

        String courses = sendGetRequestToURL(USER_COURSES_REQUEST_ADDRESS.getValue());

        if (courses == null) {
            new RefreshTokenService(ctx).execute();
            courses = sendGetRequestToURL(USER_COURSES_REQUEST_ADDRESS.getValue());
        }

        int[] coursesId = gson.fromJson(courses, int[].class);

        if (coursesId == null || coursesId.length == 0)
            return null;

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

        Map<String, Object> dataMap = ServiceUtil.getJsonAsMap(
                sendGetRequestToURL(COURSE_DATA_REQUEST_ADDRESS.getValue() + courseId)
        );
        List<Integer> modulesId = ServiceUtil.getJsonAsList(
                sendGetRequestToURL(COURSE_DATA_REQUEST_ADDRESS.getValue() + courseId + "/modules")
        );

        courseName = String.valueOf(dataMap.get(NAME_KEY.getValue()));
        courseDescription = String.valueOf(dataMap.get(DESCRIPTION_KEY.getValue()));

        if (modulesId != null) {
            for (Integer moduleId : modulesId) {
                modules.add(getModuleData(moduleId));
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

        Map<String, Object> moduleDataMap = ServiceUtil.getJsonAsMap(
                sendGetRequestToURL(MODULE_DATA_REQUEST_ADDRESS.getValue() + moduleId)
        );
        List<Integer> moduleNotesId = ServiceUtil.getJsonAsList(
                sendGetRequestToURL(MODULE_DATA_REQUEST_ADDRESS.getValue() + moduleId + "/lessons"));
        List<Integer> moduleTestsId = ServiceUtil.getJsonAsList(
                sendGetRequestToURL(MODULE_DATA_REQUEST_ADDRESS.getValue() + moduleId + "/tests"));

        if (moduleTestsId.size() != 0) {
            for (Integer moduleNoteId : moduleNotesId) {
                notes.add(getNoteData(moduleNoteId));
            }
        }

        if (moduleTestsId.size() != 0) {
            for (Integer moduleTestId : moduleTestsId) {
                tests.add(getTestData(moduleTestId));
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
        Map<String, Object> noteDataMap = ServiceUtil.getJsonAsMap(
                sendGetRequestToURL(LESSON_DATA_REQUEST_ADDRESS.getValue() + noteId));

        return new NoteData(
                String.valueOf(noteDataMap.get("name")),
                String.valueOf(noteDataMap.get("description")),
                String.valueOf(noteDataMap.get("duration")),
                isItemCompleted(LESSONS_REQUEST_ADDRESS.getValue(), noteId),
                String.valueOf(noteDataMap.get("chat_text"))
        );

    }

    private TestData getTestData(int testId) {
        Map<String, Object> testDataMap = ServiceUtil.getJsonAsMap(
                sendGetRequestToURL(TEST_DATA_REQUEST_ADDRESS.getValue() + testId));

        List<QuestionData> questions = new ArrayList<>();
        List<Integer> testQuestions = ServiceUtil.getJsonAsList(sendGetRequestToURL(TEST_DATA_REQUEST_ADDRESS.getValue() + testId + "/questions"));

        if (testQuestions.size() == 0)
            return null;

        testQuestions.forEach(q -> questions.add(getQuestionData(q)));

        return new TestData(
                String.valueOf(testDataMap.get("name")),
                String.valueOf(testDataMap.get("description")),
                String.valueOf(testDataMap.get("duration")),
                isItemCompleted(TESTS_REQUEST_ADDRESS.getValue(), testId),
                questions
        );
    }

    private QuestionData getQuestionData(int questionId) {
        Map<String, Object> q = ServiceUtil.getJsonAsMap(sendGetRequestToURL(QUESTION_DATA_REQUEST_ADDRESS.getValue() + questionId));

        List<AnswerData> answers = new ArrayList<>();
        JSONArray answersArray = (JSONArray) q.get("answers");

        for (int i = 0; i < (answersArray != null ? answersArray.length() : 0); i++) {
            try {
                JSONObject answer = answersArray.getJSONObject(i);
                answers.add(new AnswerData(
                        answer.getString("text"),
                        answer.getBoolean("is_right"), ""
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

    private boolean isItemCompleted(String requestAddress, double noteId) {
        List<String> completedItems = ServiceUtil.getJsonAsList(sendGetRequestToURL(requestAddress));

        return completedItems.size() != 0 && completedItems.contains(String.valueOf(noteId));
    }


    @Override
    protected List<CourseData> doInBackground(Void... voids) {
        return getCourses();
    }
}
