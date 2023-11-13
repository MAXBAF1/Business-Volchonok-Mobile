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
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.invoke.TypeDescriptor;
import java.lang.reflect.Type;
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

public abstract class PostService<Params> extends AsyncTask<Params, Void, Double> {

    private OkHttpClient httpClient;
    private Request request;
    private Context ctx;
    protected static SharedPreferences sPref;

    protected PostService(Context ctx) {
        this.ctx = ctx;
        sPref = this.ctx.getSharedPreferences(
                SHARED_PREFERENCES_NAME.getValue(), Context.MODE_PRIVATE);
    }

    protected static Map<String, Object> getJsonAsMap(String json){
        Map<String, Object> result = new HashMap<>();

        try {
            JSONObject obj = new JSONObject(json);

            for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
                String key = it.next();
                result.put(key, obj.get(key));
            }
        } catch (JSONException e) {
            Log.e("TAG", "parser to map: " + json);
            throw new RuntimeException(json);
        }

        return result;
    }

    protected static <T extends Object> List<T> getJsonAsList(String json) {
        //TODO rebuild method with org.json.JSONArray and etc.
        return new Gson().fromJson(json, new TypeToken<List<T>>() {}.getType());
    }

    protected Double sendHttpRequest(String requestAddress, RequestBody requestBody, Map<String, String> headers) {
        httpClient = new OkHttpClient();

        Request.Builder builder = new Request.Builder()
                .url(requestAddress)
                .method(REQUEST_METHOD_POST.getValue(), requestBody);

        for (Map.Entry<String, String> e : headers.entrySet()) {
            builder.addHeader(e.getKey(), e.getValue());
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
            Log.d("TAG", "sendHttpRequest: " + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }

        return Double.NaN;
    }

    protected Double sendHttpRequest(String requestAddress, RequestBody requestBody) {
        return sendHttpRequest(requestAddress, requestBody, Map.of());
    }

    public static void saveTokensToPreferences(Map<String, Object> responseBodyAsMap) {
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
