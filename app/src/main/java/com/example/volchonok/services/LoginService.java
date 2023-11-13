package com.example.volchonok.services;

import android.content.Context;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.example.volchonok.services.enums.ServiceStringValue.*;

public class LoginService extends PostService<String> {

    public LoginService(Context ctx) {
        super(ctx);
    }

    private double login(String login, String password) {
        Map<String, String> requestBodyParameters = Map.of(
                LOGIN_KEY.getValue(), login,
                PASSWORD_KEY.getValue(), password
        );
        RequestBody requestBody = RequestBody.create(
                new Gson().toJson(requestBodyParameters, new TypeToken<Map>() {}.getType()),
                MediaType.get(CONTENT_TYPE_JSON.getValue())
        );

        return sendPostRequestToURL(LOGIN_REQUEST_ADDRESS.getValue(), requestBody);
    }

    @Override
    protected Double doInBackground(String... voids) {
        if (voids.length != 2)
            throw new IllegalArgumentException("There are need two arguments! (login and password)");

        return login(voids[0], voids[1]);
    }
}
