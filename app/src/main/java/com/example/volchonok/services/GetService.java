package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.ACCESS_TOKEN_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.REQUEST_METHOD_GET;
import static com.example.volchonok.services.enums.ServiceStringValue.RESPONSE_DATA_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.RESPONSE_STATUS_KEY;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class GetService<In, Out> extends AbstractService<In, Out> {

    protected GetService(Context ctx) {
        super(ctx);
    }

    protected String sendGetRequestToURL(String url) {
        request = new Request.Builder()
                .url(url)
                .method(REQUEST_METHOD_GET.getValue(), null)
                .addHeader("Authorization", "Bearer " + sPref.getString(ACCESS_TOKEN_KEY.getValue(), ""))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            Map<String, Object> responseBodyAsMap = ServiceUtil.getJsonAsMap(responseBody.string());

            if (response.code() == 403)
                return null;

            if (response.code() == 200) {
                return responseBodyAsMap.get(RESPONSE_DATA_KEY.getValue()).toString();
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }
}
