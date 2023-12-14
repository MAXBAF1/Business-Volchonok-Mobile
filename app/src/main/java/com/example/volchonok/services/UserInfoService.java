package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.volchonok.RemoteInfoStorage;
import com.example.volchonok.data.UserData;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

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

        if (response == null) {
            new RefreshTokenService(ctx).execute();
            response = tryGetUserInfo();
        }

        return response == null
                ? null
                : new Gson().fromJson(response, UserData.class);
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

        try (Response response = httpClient.newCall(request).execute()) {

            ResponseBody responseBody = response.body();
            Map<String, Object> responseBodyAsMap = ServiceUtil.getJsonAsMap(responseBody.string());

            double responseCode = Double.parseDouble(
                    String.valueOf(responseBodyAsMap.get(RESPONSE_STATUS_KEY.getValue()))
            );

            if (Math.abs(responseCode - 200.0) < 1e6) {
                return String.valueOf(responseBodyAsMap.get(RESPONSE_DATA_KEY.getValue()));
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } catch (NumberFormatException ignored) {
            return null;
        }

        return null;
    }

    @Override
    protected UserData doInBackground(Void... voids) {
        return getUserInfo();
    }
}
