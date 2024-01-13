package com.example.volchonok.services;

import static com.example.volchonok.RemoteInfoStorage.isWasSentRefreshRequest;
import static com.example.volchonok.RemoteInfoStorage.setWasSentRefreshRequest;
import static com.example.volchonok.services.enums.ServiceStringValue.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.volchonok.RemoteInfoStorage;
import com.example.volchonok.data.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UserInfoService extends AsyncTask<Void, Void, UserData> {
    protected OkHttpClient httpClient;
    protected Request request;
    protected Context ctx;
    protected SharedPreferences sPref;
    private final UserData userData;

    public UserInfoService(Context ctx) {
        this.ctx = ctx;
        sPref = ctx.getSharedPreferences(SHARED_PREFERENCES_NAME.getValue(), Context.MODE_PRIVATE);
        httpClient = new OkHttpClient();
        userData = RemoteInfoStorage.getUserData();
    }

    public UserData getUserInfo() {
        if (userData != null) return userData;

        String response = tryGetUserInfo();

        if (response == null && !isWasSentRefreshRequest()) {
            new RefreshTokenService(ctx).execute();
            setWasSentRefreshRequest(true);
            response = tryGetUserInfo();
        }


        try {
            return response == null ? null : new ObjectMapper().readValue(response, UserData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String tryGetUserInfo() {
        UserData userData = RemoteInfoStorage.getUserData();
        if (userData != null) {
            return new Gson().toJson(userData);
        }

        request = new Request.Builder()
                .url(USER_INFO_REQUEST_ADDRESS.getValue())
                .method(REQUEST_METHOD_GET.getValue(), null)
                .addHeader("Authorization", "Bearer " + sPref.getString(ACCESS_TOKEN_KEY.getValue(), ""))
                .build();

//        Log.d("TAG", "request info: " + Objects.requireNonNull(request.header("Authorization")).substring(7));

        try (Response response = httpClient.newCall(request).execute()) {

            ResponseBody responseBody = response.body();
            Map<String, Object> responseBodyAsMap = ServiceUtil.getJsonAsMap(responseBody.string());

//            Log.d("TAG", "user info response: " + response.code());

            double responseCode = Double.parseDouble(
                    String.valueOf(responseBodyAsMap.get(RESPONSE_STATUS_KEY.getValue()))
            );

            if (Math.abs(responseCode - 200.0) < 1e6) {
                return String.valueOf(responseBodyAsMap.get(RESPONSE_DATA_KEY.getValue()));
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } catch (NumberFormatException ignored) { }

        return null;
    }

    @Override
    protected UserData doInBackground(Void... voids) {
        return getUserInfo();
    }
}
