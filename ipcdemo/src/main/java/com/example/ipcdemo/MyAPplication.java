package com.example.ipcdemo;

import android.app.Application;
import android.util.Log;

public class MyAPplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String className = getApplicationInfo().className;
        Log.d("Application"+"1",className);
    }
}
