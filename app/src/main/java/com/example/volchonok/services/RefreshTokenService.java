package com.example.volchonok.services;

import android.content.Context;

import okhttp3.internal.http2.Header;

import static com.example.volchonok.services.enums.ServiceStringValue.*;

public class RefreshTokenService extends VolchonokService<Void, Void, Double> {

    public RefreshTokenService(Context ctx) {
        super(ctx);
    }

    private double refresh() {
        return sendHttpRequest(
                ACCESS_TOKEN_REQUEST_ADDRESS.getValue(),
                new Header(REFRESH_TOKEN_KEY.getValue(), sPref.getString(REFRESH_TOKEN_KEY.getValue(), ""))
        );
    }

    @Override
    protected Double doInBackground(Void... voids) {
        return refresh();
    }
}
