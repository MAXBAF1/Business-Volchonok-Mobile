package com.example.volchonok.services;

import android.content.Context;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.example.volchonok.services.enums.ServiceStringValue.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

//only service class
class RefreshTokenService extends PostService<Void> {

    public RefreshTokenService(Context ctx) {
        super(ctx);
    }

    double refresh() {
        double x = Double.NaN;
        try {
            x = sendPostRequestToURL(
                    REFRESH_TOKENS_REQUEST_ADDRESS.getValue(),
                    RequestBody.create(
                            new ObjectMapper().writeValueAsString(
                                    Map.of(
                                            ACCESS_TOKEN_KEY.getValue(), sPref.getString(ACCESS_TOKEN_KEY.getValue(), ""),
                                            REFRESH_TOKEN_KEY.getValue(), sPref.getString(REFRESH_TOKEN_KEY.getValue(), "")
                                    )
                            ),
                            MediaType.get(CONTENT_TYPE_JSON.getValue())
                    ));
        } catch (JsonProcessingException e) {
            Log.d("TAG", "error" + e.getMessage());
        }
        return x;
    }

    @Override
    protected Double doInBackground(Void... voids) {
        return refresh();
    }
}
