package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.ACCESS_TOKEN_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.REFRESH_TOKEN_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.REQUEST_METHOD_POST;
import static com.example.volchonok.services.enums.ServiceStringValue.RESPONSE_DATA_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.RESPONSE_STATUS_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.SHARED_PREFERENCES_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.Header;

public abstract class VolchonokService<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    protected OkHttpClient httpClient;
    protected Request request;
    protected Context ctx;
    protected SharedPreferences sPref;

    protected VolchonokService(Context ctx) {
        this.ctx = ctx;
        sPref = ctx.getSharedPreferences(
                SHARED_PREFERENCES_NAME.getValue(), Context.MODE_PRIVATE);
    }

    protected Map<String, Object> getJsonAsMap(String json) {
        return new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
    }

    protected double sendHttpRequest(String requestAddress, RequestBody requestBody, Header... headers) {
        httpClient = new OkHttpClient();

        Request.Builder builder = new Request.Builder()
                .url(requestAddress)
                .method(REQUEST_METHOD_POST.getValue(), requestBody);

        for (Header header : headers) {
            builder.addHeader(header.name.toString(), header.value.toString());
        }
        request = builder.build();


        try (Response response = httpClient.newCall(request).execute()) {

            ResponseBody responseBody = response.body();
            Map<String, Object> responseBodyAsMap = getJsonAsMap(responseBody.string());

            if (Double.parseDouble(
                    String.valueOf(responseBodyAsMap.get(RESPONSE_STATUS_KEY.getValue()))
            ) == 200.0) {
                saveTokensToPreferences(responseBodyAsMap);
            }

            return Double.parseDouble(
                    String.valueOf(responseBodyAsMap.get(RESPONSE_STATUS_KEY.getValue()))
            );
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return Double.NaN;
    }

    protected double sendHttpRequest(String requestAddress, Header... headers) {
        return sendHttpRequest(requestAddress, null, headers);
    }

    protected void saveTokensToPreferences(Map<String, Object> responseBodyAsMap) {
        Map<String, Object> tokens = getJsonAsMap(
                String.valueOf(responseBodyAsMap.get(RESPONSE_DATA_KEY.getValue()))
        );

        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(ACCESS_TOKEN_KEY.getValue(),
                        String.valueOf(tokens.get(ACCESS_TOKEN_KEY.getValue())))
                .putString(REFRESH_TOKEN_KEY.getValue(),
                        String.valueOf(tokens.get(REFRESH_TOKEN_KEY.getValue())))
                .apply();
    }
}
