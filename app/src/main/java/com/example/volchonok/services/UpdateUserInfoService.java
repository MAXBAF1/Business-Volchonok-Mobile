package com.example.volchonok.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.volchonok.data.UserData;
import com.example.volchonok.services.enums.ServiceStringValue;

import java.util.Collections;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UpdateUserInfoService extends PostService<UserData> {

    public UpdateUserInfoService(Context ctx) {
        super(ctx);
    }

    private double update(UserData userData) {
        String accessToken = ctx
                .getSharedPreferences(ServiceStringValue.SHARED_PREFERENCES_NAME.getValue(),
                        Context.MODE_PRIVATE)
                .getString(ServiceStringValue.ACCESS_TOKEN_KEY.getValue(), "");

        return sendPostRequestToURL(
                ServiceStringValue.USER_INFO_REQUEST_ADDRESS.getValue(),
                RequestBody.create(
                        String.format(Locale.ROOT,
                                "{\"email\":\"%s\", \"phone\":\"%s\", \"avatar\":\"%d\"}",
                                userData.getEmail(), userData.getPhone(), userData.getAvatar()),
                        MediaType.get(ServiceStringValue.MEDIA_TYPE_JSON.getValue())
                ),
                Collections.singletonMap("Authorization", String.format("Bearer %s", accessToken))
        );
    }

    @Override
    protected Double doInBackground(UserData... data) {
        return update(data[0]);
    }
}
