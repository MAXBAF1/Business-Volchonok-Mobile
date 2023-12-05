package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.*;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.volchonok.data.AnswerData;
import com.example.volchonok.data.CourseData;
import com.example.volchonok.data.MessageData;
import com.example.volchonok.data.ModuleData;
import com.example.volchonok.data.NoteData;
import com.example.volchonok.data.QuestionData;
import com.example.volchonok.data.ReviewData;
import com.example.volchonok.data.TestData;
import com.example.volchonok.data.UserData;
import com.example.volchonok.enums.AuthorType;
import com.example.volchonok.enums.CourseDataAccessLevel;
import com.example.volchonok.enums.MessageType;
import com.example.volchonok.interfaces.ILesson;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseService extends GetService<Pair<CourseDataAccessLevel, List<CourseData>>, List<CourseData>> {
    protected final Gson gson;
    private final Map<String, List<String>> completedItems;

    public CourseService(Context ctx) {
        super(ctx);
        completedItems = new HashMap<>();
        gson = new Gson();
    }

    public List<CourseData> getCourses() {
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
        UserData emptyUser = new UserData();

        List<ReviewData> reviews = List.of(
                new ReviewData(emptyUser, ""),
                new ReviewData(emptyUser, ""),
                new ReviewData(emptyUser, "")
        );

        Map<String, Object> dataMap = ServiceUtil.getJsonAsMap(
                sendGetRequestToURL(COURSE_DATA_REQUEST_ADDRESS.getValue() + courseId)
        );

        courses.add(new CourseData(
                courseId,
                String.valueOf(dataMap.get(NAME_KEY.getValue())),
                new ArrayList<>(),
                String.valueOf(dataMap.get(DESCRIPTION_KEY.getValue())),
                reviews
        ));

    }

    private void fillModulesData(List<ModuleData> modules, int courseId) {

        List<Integer> modulesId = ServiceUtil.getJsonAsList(
                sendGetRequestToURL(COURSE_DATA_REQUEST_ADDRESS.getValue() + courseId + "/modules")
        );

        for (Integer moduleId : modulesId) {
            Map<String, Object> moduleDataMap = ServiceUtil.getJsonAsMap(
                    sendGetRequestToURL(MODULE_DATA_REQUEST_ADDRESS.getValue() + moduleId)
            );

            modules.add(new ModuleData(
                    moduleId,
                    String.valueOf(moduleDataMap.get("name")),
                    String.valueOf(moduleDataMap.get("description")),
                    new ArrayList<>(),
                    new ArrayList<>()
            ));
        }
    }

    private void fillNotesData(List<ILesson> notes, int moduleId) {
        List<Integer> moduleLessonsId = ServiceUtil.getJsonAsList(
                sendGetRequestToURL(MODULE_DATA_REQUEST_ADDRESS.getValue() + moduleId + "/lessons"));

        if (moduleLessonsId.size() != 0) {
            for (Integer moduleLessonId : moduleLessonsId) {
                Map<String, Object> noteDataMap = ServiceUtil.getJsonAsMap(
                        sendGetRequestToURL(LESSON_DATA_REQUEST_ADDRESS.getValue() + moduleLessonId));

                List<MessageData> messages = new ArrayList<>();

                try {
                    JSONObject chatText = new JSONObject(String.valueOf(noteDataMap.get("chat_text")));
                    JSONArray messageArray = chatText.getJSONArray("list");
                    for (int i = 0; i < messageArray.length(); i++) {
                        JSONObject currentMessage = messageArray.getJSONObject(i);

                        String author = currentMessage.getString("author");
                        String type = currentMessage.getString("type");

                        messages.add(new MessageData(
                                currentMessage.getString("text"),

                                author.equals(AuthorType.STUDENT.getDesc())
                                        ? AuthorType.STUDENT
                                        : AuthorType.WOLF,

                                type.equals(MessageType.TEXT.getDesc())
                                        ? MessageType.TEXT
                                        : type.equals(MessageType.VIDEO.getDesc())
                                        ? MessageType.VIDEO
                                        : MessageType.PICTURE,

                                currentMessage.getString("url")
                        ));
                    }
                } catch (JSONException e) {
                    Log.d("TAG", "json exception: " + noteDataMap.get("chat_text"), e);
                }

                notes.add(new NoteData(
                        Integer.parseInt(String.valueOf(noteDataMap.get("lesson_id"))),
                        String.valueOf(noteDataMap.get("name")),
                        String.valueOf(noteDataMap.get("description")),
                        String.valueOf(noteDataMap.get("duration")),
                        isItemCompleted(COMPLETED_NOTES_REQUEST_ADDRESS.getValue(), Double.valueOf(moduleLessonId)),
                        messages
                ));
            }
        }
    }

    private void fillTestsData(List<ILesson> tests, int lessonId) {
        List<Integer> moduleTestsId = ServiceUtil.getJsonAsList(
                sendGetRequestToURL(LESSON_DATA_REQUEST_ADDRESS.getValue() + lessonId + "/tests"));

        if (moduleTestsId.size() != 0) {
            for (Integer moduleTestId : moduleTestsId) {
                Map<String, Object> testDataMap = ServiceUtil.getJsonAsMap(
                        sendGetRequestToURL(TEST_DATA_REQUEST_ADDRESS.getValue() + moduleTestId));

                tests.add(new TestData(
                        moduleTestId,
                        String.valueOf(testDataMap.get("name")),
                        String.valueOf(testDataMap.get("description")),
                        String.valueOf(testDataMap.get("duration")),
                        isItemCompleted(COMPLETED_TESTS_REQUEST_ADDRESS.getValue(), Double.valueOf(moduleTestId)),
                        new ArrayList<>()
                ));
            }
        }
    }

    private void fillQuestionsData(List<QuestionData> questions, int testId) {

        List<Integer> testQuestions = ServiceUtil.getJsonAsList(
                sendGetRequestToURL(TEST_DATA_REQUEST_ADDRESS.getValue() + testId + "/questions")
        );

        if (testQuestions.size() != 0) {
            for (Integer testQuestionId : testQuestions) {

                Map<String, Object> q = ServiceUtil.getJsonAsMap(
                        sendGetRequestToURL(QUESTION_DATA_REQUEST_ADDRESS.getValue() + testQuestionId)
                );

                List<AnswerData> answers = new ArrayList<>();
                JSONArray answersArray = (JSONArray) q.get("answers");

                for (int i = 0; i < (answersArray != null ? answersArray.length() : 0); i++) {
                    try {
                        JSONObject answer = answersArray.getJSONObject(i);
                        answers.add(new AnswerData(
                                answer.getString("text"),
                                answer.getBoolean("is_right"),
                                false // TODO я тут false поставил
                        ));
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                questions.add(new QuestionData(
                        testQuestionId,
                        String.valueOf(q.get("text")),
                        answers,
                        String.valueOf(q.get("explanation"))
                ));
            }
        }
    }

    private boolean isItemCompleted(String requestAddress, Double noteId) {
        if (!completedItems.containsKey(requestAddress))
            completedItems.put(
                    requestAddress,
                    ServiceUtil.getJsonAsList(sendGetRequestToURL(requestAddress))
            );

        return completedItems.containsKey(requestAddress)
                && completedItems.get(requestAddress).contains(noteId.intValue());
    }


    @Override
    protected List<CourseData> doInBackground(Pair<CourseDataAccessLevel, List<CourseData>>... levels) {
        if (levels.length == 0) return getCourses();

        if (levels.length > 1)
            throw new IllegalArgumentException("Только один аргумент!");

        List<CourseData> courses = levels[0].second;

        switch (levels[0].first) {
            case ONLY_COURSES_DATA -> {
                courses = getCourses();
            }
            case MODULES_DATA -> {
                courses.forEach(course ->
                        fillModulesData(course.getModules(), course.getId())
                );
            }
            case NOTES_DATA -> {
                courses.forEach(course ->
                        course.getModules().forEach(module -> {
                                    fillNotesData(module.getLessonNotes(), module.getId());
                                }
                        )
                );
            }
            case TESTS_DATA -> {
                courses.forEach(course ->
                        course.getModules().forEach(module ->
                                module.getLessonNotes().forEach(note ->
                                        fillTestsData(module.getLessonTests(), ((NoteData)note).getId())
                                )
                        )
                );
            }
            case QUESTIONS_DATA -> {
                courses.forEach(course ->
                        course.getModules().forEach(module ->
                                module.getLessonTests().forEach(test -> {
                                    TestData testData = (TestData) test;
                                    fillQuestionsData(testData.getQuestions(), ((TestData) test).getId());
                                })
                        )
                );
            }
            case ALL_DATA -> {
                courses = getCourses();
                courses = doInBackground(new Pair<>(CourseDataAccessLevel.MODULES_DATA, courses));
                courses = doInBackground(new Pair<>(CourseDataAccessLevel.NOTES_DATA, courses));
                courses = doInBackground(new Pair<>(CourseDataAccessLevel.TESTS_DATA, courses));
                courses = doInBackground(new Pair<>(CourseDataAccessLevel.QUESTIONS_DATA, courses));
            }
        }

        return courses;
    }
}