package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.ACCESS_TOKEN_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.MEDIA_TYPE_JSON;
import static com.example.volchonok.services.enums.ServiceStringValue.REFRESH_TOKEN_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.REQUEST_METHOD_GET;
import static com.example.volchonok.services.enums.ServiceStringValue.REQUEST_METHOD_POST;
import static com.example.volchonok.services.enums.ServiceStringValue.RESPONSE_DATA_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.RESPONSE_STATUS_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.SHARED_PREFERENCES_NAME;
import static com.example.volchonok.services.enums.ServiceStringValue.USER_INFO_REQUEST_ADDRESS;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.volchonok.data.UserData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.Header;

public class UserInfoService extends AsyncTask<Void, Void, UserData> {
    protected OkHttpClient httpClient;
    protected Request request;
    protected Context ctx;
    protected SharedPreferences sPref;

    public UserInfoService(Context ctx) {
        this.ctx = ctx;
        sPref = ctx.getSharedPreferences(SHARED_PREFERENCES_NAME.getValue(), Context.MODE_PRIVATE);
        httpClient = new OkHttpClient();
    }

    public UserData getUserInfo() {
        String response = tryGetUserInfo();

        if (response == null) {
            new RefreshTokenService(ctx).execute();
            response = tryGetUserInfo();
        }

        return new Gson().fromJson(response, UserData.class);
    }

    private String tryGetUserInfo() {
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
                return responseBodyAsMap.get(RESPONSE_DATA_KEY.getValue()).toString();
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected UserData doInBackground(Void... voids) {
        return getUserInfo();
    }
}
