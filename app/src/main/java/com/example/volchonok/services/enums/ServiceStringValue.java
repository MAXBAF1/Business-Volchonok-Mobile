package com.example.volchonok.services.enums;

import androidx.annotation.NonNull;

public enum ServiceStringValue {
    SERVER_ADDRESS("https://business-volchonok.ru/mobile/api/v1"),
    ACCESS_TOKEN_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/auth/refresh"),
    LOGIN_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/auth/login"),
    USER_INFO_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/user/me"),
    USER_COURSES_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/user/courses"),
    COURSE_DATA_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/course/"),
    MODULE_DATA_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/module/"),
    LESSON_DATA_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/lesson/"),
    QUESTION_DATA_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/question/"),
    TEST_DATA_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/test/"),
    COMPLETED_NOTES_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/user/completed/lessons"),
    COMPLETED_COURSES_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/user/completed/courses"),
    COMPLETED_TESTS_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/user/completed/tests"),
    COMPLETED_MODULES_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/user/completed/modules"),
    COMPLETED_QUESTIONS_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/user/completed/questions/"),
    SHARED_PREFERENCES_NAME("tokens"),
    REQUEST_METHOD_POST("POST"),
    REQUEST_METHOD_GET("GET"),
    MEDIA_TYPE_JSON("application/json"),
    CONTENT_TYPE_JSON("application/json"),
    ACCESS_TOKEN_KEY("access_token"),
    LOGIN_KEY("login"),
    PASSWORD_KEY("password"),
    REFRESH_TOKEN_KEY("refresh_token"),
    RESPONSE_STATUS_KEY("status"),
    NAME_KEY("name"),
    SURNAME_KEY("surname"),
    FIRSTNAME_KEY("firstname"),
    ID_KEY("id"),
    DESCRIPTION_KEY("description"),
    DURATION_KEY("duration"),
    LESSON_ID_KEY("lesson_id"),
    TEXT_KEY("text"),
    AVATAR_KEY("avatar"),
    REVIEWS_KEY("reviews"),
    ANSWERS_KEY("answers"),
    IS_RIGHT_KEY("is_right"),
    EXPLANATION_KEY("explanation"),
    URL_KEY("url"),
    AUTHOR_KEY("author"),
    LIST_KEY("list"),
    TYPE_KEY("type"),
    CHAT_TEXT_KEY("chat_text"),
    RESPONSE_DATA_KEY("data");

    private final String value;

    ServiceStringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
