package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.ACCESS_TOKEN_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.LOGIN_REQUEST_ADDRESS;
import static com.example.volchonok.services.enums.ServiceStringValue.REFRESH_TOKENS_REQUEST_ADDRESS;
import static com.example.volchonok.services.enums.ServiceStringValue.REQUEST_METHOD_POST;
import static com.example.volchonok.services.enums.ServiceStringValue.RESPONSE_STATUS_KEY;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class PostService<In> extends AbstractService<In, Double> {

    public PostService(Context ctx) {
        super(ctx);
    }

    protected Double sendPostRequestToURL(String url, RequestBody requestBody, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url)
                .method(REQUEST_METHOD_POST.getValue(), requestBody);

        for (Map.Entry<String, String> e : headers.entrySet()) {
            builder.addHeader(e.getKey(), e.getValue());
        }

        request = builder.build();

        try (Response response = httpClient.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String responseBodyAsString = responseBody.string();
            Map<String, Object> responseBodyAsMap = ServiceUtil.getJsonAsMap(responseBodyAsString);

            Log.d("TAG", "response: " + response);

            if (response.code() == 403)
                return Double.NEGATIVE_INFINITY;

            double responseCode = Double.parseDouble(
                    String.valueOf(responseBodyAsMap.get(RESPONSE_STATUS_KEY.getValue()))
            );

            if (response.code() == 200 && List.of(LOGIN_REQUEST_ADDRESS.getValue(), REFRESH_TOKENS_REQUEST_ADDRESS.getValue()).contains(url)) {
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
