package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.ACCESS_TOKEN_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.ACCESS_TOKEN_REQUEST_ADDRESS;
import static com.example.volchonok.services.enums.ServiceStringValue.LOGIN_REQUEST_ADDRESS;
import static com.example.volchonok.services.enums.ServiceStringValue.REFRESH_TOKEN_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.REQUEST_METHOD_POST;
import static com.example.volchonok.services.enums.ServiceStringValue.RESPONSE_DATA_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.RESPONSE_STATUS_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.TEST_DATA_REQUEST_ADDRESS;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.volchonok.services.enums.ServiceStringValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class PostService<Params> extends AbstractService<Params, Double> {

    public PostService(Context ctx) {
        super(ctx);
    }

    protected Double sendPostRequestToURL(String url, RequestBody requestBody, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .method(REQUEST_METHOD_POST.getValue(), requestBody);

        for (Map.Entry<String, String> e : headers.entrySet()) {
            builder.addHeader(e.getKey(), e.getValue());
        }

        request = builder.build();

        try (Response response = httpClient.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            Map<String, Object> responseBodyAsMap = ServiceUtil.getJsonAsMap(responseBody.string());

            double responseCode = Double.parseDouble(
                    String.valueOf(responseBodyAsMap.get(RESPONSE_STATUS_KEY.getValue()))
            );

            if (Math.abs(responseCode - 200.0) < 1e6 && url.equals(LOGIN_REQUEST_ADDRESS.getValue())) {
                ServiceUtil.saveTokensToPreferences(responseBodyAsMap, sPref.edit());
            }

            return responseCode;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return Double.NaN;
    }

    protected Double sendPostRequestToURL(String url, RequestBody requestBody) {
        return sendPostRequestToURL(url, requestBody, Map.of());
    }
}
