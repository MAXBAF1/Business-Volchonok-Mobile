package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.SHARED_PREFERENCES_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.volchonok.data.UserData;

import java.util.concurrent.ExecutionException;

public class CheckUserToken extends PostService<Void> {
    private final RefreshTokenService refreshTokenService;

    public CheckUserToken(Context ctx) {
        super(ctx);
        this.refreshTokenService = new RefreshTokenService(ctx);
    }

    private double checkToken() {
        UserData userData = new UserInfoService(ctx).getUserInfo();

        if (userData == null) {
            double refreshResponse = refreshTokenService.refresh();

            if (refreshResponse == -7000)
                return Double.NaN;

            return refreshResponse;
        }

        return 200;
    }

    @Override
    protected Double doInBackground(Void... voids) {
        return checkToken();
    }
}
