package com.example.volchonok.services;

import android.content.Context;
import android.content.SharedPreferences;
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

import static com.example.volchonok.services.enums.ServiceStringValue.*;

public class LoginService extends VolchonokService<String, Void, Double>{

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

        return sendHttpRequest(LOGIN_REQUEST_ADDRESS.getValue(), requestBody);
    }

    @Override
    protected Double doInBackground(String... voids) {
        if (voids.length != 2)
            throw new IllegalArgumentException("There are need two arguments! (login and password)");

        return login(voids[0], voids[1]);
    }
}
