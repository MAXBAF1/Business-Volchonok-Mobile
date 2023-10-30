package com.example.volchonok.services;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginService extends AsyncTask<String, Void, Double> {

    private static final String LOGIN_REQUEST_ADDRESS = "http://localhost:8080/api/v1/auth/login";
    private static final String LOGIN_KEY = "login";
    private static final String PASSWORD_KEY = "password";
    private static final String CONTENT_TYPE = "application/json";
    private static final String REQUEST_METHOD = "POST";
    private static final String LOGIN_STATUS_KEY = "status";
    private static OkHttpClient httpClient;
    private static Request request;

    private double login(String login, String password) {

        double loginResult = 200.0;

        Map<String, String> requestBodyParameters = Map.of(
                LOGIN_KEY, login,
                PASSWORD_KEY, password
        );
        RequestBody requestBody = RequestBody.create(
                new Gson().toJson(requestBodyParameters, new TypeToken<Map>(){}.getType()),
                MediaType.get(CONTENT_TYPE)
        );

        httpClient = new OkHttpClient();
        request = new Request.Builder()
                .url(LOGIN_REQUEST_ADDRESS)
                .method(REQUEST_METHOD, requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            ResponseBody responseBody = response.body();
            Map<String, Object> responseBodyAsMap = new Gson().fromJson(
                    responseBody.string(),
                    new TypeToken<HashMap<String, Object>>() {
                    }.getType()
            );

            Log.d("TAG", "Function login returns: " + responseBodyAsMap.get("status"));
            Log.d("TAG", "Function login returns: " + responseBodyAsMap.get("message"));

            loginResult = Double.parseDouble(String.valueOf(responseBodyAsMap.get(LOGIN_STATUS_KEY)));
        } catch (IOException | NullPointerException e){
            e.printStackTrace();
        }

        return loginResult;
    }

    @Override
    protected Double doInBackground(String... voids) {
        if (voids.length != 2)
            throw new IllegalArgumentException("There are need two arguments! (login and password)");

        return login(voids[0], voids[1]);
    }
}
