package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.ACCESS_TOKEN_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.REFRESH_TOKEN_KEY;
import static com.example.volchonok.services.enums.ServiceStringValue.RESPONSE_DATA_KEY;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ServiceUtil {

    protected static Map<String, Object> getJsonAsMap(String json){
        Map<String, Object> result = new HashMap<>();

        try {
            JSONObject obj = new JSONObject(json);

            for (Iterator<String> it = obj.keys(); it.hasNext();) {
                String key = it.next();
                result.put(key, obj.get(key));
            }
        } catch (JSONException e) {
            throw new RuntimeException(json);
        }

        return result;
    }

    protected static <T extends Object> List<T> getJsonAsList(String json) {
        List<T> result = new ArrayList<>();

        try {
            JSONArray arr = new JSONArray(json);

            for (int i = 0; i < arr.length(); i++) {
                result.add((T) arr.get(i));
            }
        } catch (JSONException e) {
            throw new RuntimeException(json);
        }

        return result;
    }

    public static void saveTokensToPreferences(Map<String, Object> responseBodyAsMap,
                                               SharedPreferences.Editor editor) {
        Map<String, Object> tokens = getJsonAsMap(
                String.valueOf(responseBodyAsMap.get(RESPONSE_DATA_KEY.getValue()))
        );

        editor.putString(ACCESS_TOKEN_KEY.getValue(),
                        String.valueOf(tokens.get(ACCESS_TOKEN_KEY.getValue())))
                .putString(REFRESH_TOKEN_KEY.getValue(),
                        String.valueOf(tokens.get(REFRESH_TOKEN_KEY.getValue())))
                .apply();
    }
}
