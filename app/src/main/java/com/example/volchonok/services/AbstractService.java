package com.example.volchonok.services;

import static com.example.volchonok.services.enums.ServiceStringValue.SHARED_PREFERENCES_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public abstract class AbstractService<Par, Res> extends AsyncTask<Par, Void, Res> {

    protected static AbstractService instance;
    protected OkHttpClient httpClient;
    protected Request request;
    protected Context ctx;
    protected SharedPreferences sPref;

    protected AbstractService(Context ctx) {
        if (instance == null)
            instance = this;
        this.httpClient = new OkHttpClient();
        this.ctx = ctx;
        this.sPref = ctx.getSharedPreferences(
                SHARED_PREFERENCES_NAME.getValue(), Context.MODE_PRIVATE);
    }
}
