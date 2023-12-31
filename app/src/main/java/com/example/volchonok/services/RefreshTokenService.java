package com.example.volchonok.services;

import android.content.Context;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.example.volchonok.services.enums.ServiceStringValue.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

//only service class
public class RefreshTokenService extends PostService<Void> {

    public RefreshTokenService(Context ctx) {
        super(ctx);
    }

    private double refresh() {
        Log.d("TAG", "refresh");
        double x = sendPostRequestToURL(
                ACCESS_TOKEN_REQUEST_ADDRESS.getValue(),
                RequestBody.create(
                        new Gson().toJson(
                                Map.of(
                                        REFRESH_TOKEN_KEY.getValue(),
                                        sPref.getString(REFRESH_TOKEN_KEY.getValue(), "")
                                ),
                                new TypeToken<HashMap<String, Object>>() {
                                }.getType()),
                        MediaType.get(CONTENT_TYPE_JSON.getValue())
                ));

        Log.d("TAG", "refresh: " + x);
        return x;
    }

    @Override
    protected Double doInBackground(Void... voids) {
        return refresh();
    }
}
