package com.kailang.engassit.utils;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OkHttpCallback implements Callback {
    private final String TAG = "com.kailang.engassit";

    public String url;
    public String result;


    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.e(TAG, "URL: " + url);
        Log.e(TAG, "请求失败:" + e.toString());
        onFinish(false, e.toString());
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        result = response.body().string();
        Log.e(TAG, "URL: " + url);
        Log.e(TAG, "请求成功: " + result);
        onFinish(true, result);
    }

    public void onFinish(boolean status, String msg) {
    }
}

