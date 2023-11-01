package com.example.volchonok.services.enums;

public enum ServiceStringValue {
    SERVER_ADDRESS("http://localhost:8080"),
    ACCESS_TOKEN_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/api/v1/auth/refresh"),
    LOGIN_REQUEST_ADDRESS(SERVER_ADDRESS.value + "/api/v1/auth/login"),
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
    RESPONSE_DATA_KEY("data");



    private String value;

    ServiceStringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
